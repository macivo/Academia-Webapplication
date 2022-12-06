/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.person;

import ch.bfh.ti.academia.util.ConnectionManager;
import ch.bfh.ti.academia.util.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.servlet.http.HttpServletResponse.*;

/**
 * The class StudentServlet provides REST endpoints for the administration of students.
 */
@WebServlet(urlPatterns = "/api/students/*")
public class StudentServlet extends HttpServlet {
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String JSON_MEDIA_TYPE = "application/json";
    private static final Logger LOGGER = Logger.getLogger(StudentRepository.class.getName());
    private final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

    /**
     * Get-method for getting student/s
     * If id of a student in the path, then get a student from id
     * If no id in the path, then get all students
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = ConnectionManager.getConnection(true);
        StudentRepository repository = new StudentRepository(connection);
        try {
            if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
                LOGGER.info("Getting all students");
                List<Student> students = repository.findAll();
                response.setStatus(SC_OK);
                response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                objectMapper.writeValue(response.getOutputStream(), students);
            } else { //in case URL is /api/students/studentId find the student with this studentId
                LOGGER.info("Getting one student");
                try {
                    int id = Integer.parseInt(request.getPathInfo().substring(1));
                    LOGGER.info("Getting student with id " + id);
                    Student student = repository.findStudentById(id);
                    if (student == null) {
                        response.sendError(SC_NOT_FOUND);
                        return;
                    }
                    response.setStatus(SC_OK);
                    response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                    objectMapper.writeValue(response.getOutputStream(), student);
                } catch (NumberFormatException ex) {
                    response.sendError(SC_NOT_FOUND);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
        } finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * Post-method for creating a new student
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = ConnectionManager.getConnection(false);
        StudentRepository studentRepository = new StudentRepository(connection);
        PersonRepository personRepository = new PersonRepository(connection);
        Person user = (Person) request.getAttribute("user");

        //only administrator can create a new student
        if (user.getRole().equals("administrator")) {
        try {
            //read json and build Object student
            Student student = objectMapper.readValue(request.getInputStream(), Student.class);
            if (student == null || student.getStudent_id() != null) {
                response.sendError(SC_BAD_REQUEST);
                return;
            }
            if (studentRepository.isExistingMatNr(student.getMatriculation_number())) {
                response.sendError(SC_CONFLICT);
                return;
            }
            LOGGER.info("Adding student with matriculation number " + student.getMatriculation_number());
            //fill table person with data
            long personal_id = personRepository.persist(student);
            //fill table student with data and FK personal_id
            long student_id = studentRepository.persist(student, personal_id);
            student.setPersonal_id(personal_id);
            student.setStudent_id(student_id);
            response.setStatus(SC_CREATED);
            response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
            objectMapper.writeValue(response.getOutputStream(), student);
            ConnectionManager.commit(connection);
        } catch (NumberFormatException ex) {
            response.sendError(SC_NOT_FOUND);
        } catch (JsonParseException ex) {
            LOGGER.info(ex.getMessage());
            response.sendError(SC_BAD_REQUEST);
        } catch (SQLException ex) {
            ConnectionManager.rollback(connection);
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
        } finally {
            ConnectionManager.close(connection);
        }
        } else {
            response.setStatus(SC_FORBIDDEN);
        }
    }

    /**
     * Put-method for updating a new student - needs a student-id in request path
     */
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = ConnectionManager.getConnection(false);
        StudentRepository studentRepository = new StudentRepository(connection);
        PersonRepository personRepository = new PersonRepository(connection);
        Person user = (Person) request.getAttribute("user");

        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                return;
            }
            long id = Integer.parseInt(pathInfo.substring(1));
            // only admin can change student or student by himself, if student id to change belongs to personal id
            if ((user.getRole().equals("administrator"))
                    || (user.getRole().equals("student")
                    && (studentRepository.hasPersonThisStudentId(user.getPersonal_id(), id)))) {
                Student student = objectMapper.readValue(request.getInputStream(), Student.class);
                if (student == null || student.getStudent_id() == null || student.getStudent_id() != id) {
                    response.sendError(SC_BAD_REQUEST);
                    return;
                }
                LOGGER.info("Updating student with id " + id);
                boolean successByPerson = personRepository.update(student, student.getStudent_id());
                boolean successByStudent = studentRepository.update(student);
                response.setStatus(successByPerson && successByStudent ? SC_NO_CONTENT : SC_NOT_FOUND);
                ConnectionManager.commit(connection);
            } else {
                response.setStatus(SC_FORBIDDEN);
            }
        } catch (NumberFormatException ex) {
            response.sendError(SC_NOT_FOUND);
        } catch (JsonParseException ex) {
            LOGGER.info(ex.getMessage());
            response.sendError(SC_BAD_REQUEST);
        } catch (SQLException ex) {
            ConnectionManager.rollback(connection);
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
        } finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * Delete-method for deleting a student - needs a student id in request path
     */
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = ConnectionManager.getConnection(false);
        StudentRepository studentRepository = new StudentRepository(connection);
        PersonRepository personRepository = new PersonRepository(connection);
        Person user = (Person) request.getAttribute("user");

        // only administrator can delete a student
        if (user.getRole().equals("administrator")) {
        try {
            int id = Integer.parseInt(request.getPathInfo().substring(1));
            Student student = new Student();
            student = studentRepository.findStudentById(id);
            LOGGER.info("Deleting student with id " + student.getStudent_id());
            // importance delete in the table 'student' before in the table 'person'
            boolean successByStudent = studentRepository.delete(student.getStudent_id());
            boolean successByPerson = personRepository.delete(student.getPersonal_id());
            response.setStatus(successByStudent && successByPerson ? SC_NO_CONTENT : SC_NOT_FOUND);
            ConnectionManager.commit(connection);
        } catch (NumberFormatException ex) {
            response.sendError(SC_NOT_FOUND);
        } catch (SQLException ex) {
            ConnectionManager.rollback(connection);
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
        } finally {
            ConnectionManager.close(connection);
        }
        } else {
            response.setStatus(SC_FORBIDDEN);
        }
    }
}
