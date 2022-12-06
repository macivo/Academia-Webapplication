/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.enroll;

import ch.bfh.ti.academia.person.Person;
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
 * The class EnrollServlet provides REST endpoints for the administration of enrollment.
 */
@WebServlet(urlPatterns = "/api/enrollments/*")
public class EnrollServlet extends HttpServlet {

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String JSON_MEDIA_TYPE = "application/json";
    private static final Logger LOGGER = Logger.getLogger(EnrollServlet.class.getName());
    private final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
    private final int PATH_MODULE_RUN_ID = 8;
    private final int PATH_STUDENT_ID = 9;


    /**
     * Get-method for getting enrollment/s
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = ConnectionManager.getConnection(true);
        EnrollRepository repository = new EnrollRepository(connection);
        Person user = (Person) request.getAttribute("user");
        try {
            if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                return;
            } else if (request.getPathInfo().contains("module")) {
                //get the module run id

                int id = Integer.parseInt(request.getPathInfo().substring(PATH_MODULE_RUN_ID));
                //teacher can only get the enrolled students for a module run he is teaching
                System.out.println("Hello" + repository.isPersonTeacherOfModuleRun(user.getPersonal_id(), (long) id));
                if ((user.getRole().equals("professor"))
                        && (repository.isPersonTeacherOfModuleRun(user.getPersonal_id(), (long) id))) {
                    LOGGER.info("Getting enrollments form module id " + id);
                    List<Enroll> enrolls = repository.findByModuleRunId(id);
                    response.setStatus(SC_OK);
                    response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                    objectMapper.writeValue(response.getOutputStream(), enrolls);
                } else {
                    response.setStatus(SC_FORBIDDEN);
                }
            } else if (request.getPathInfo().contains("student")) {
                //get the student id
                int id = Integer.parseInt(request.getPathInfo().substring(PATH_STUDENT_ID));
                //student can only get his own grades
                if ((user.getRole().equals("student"))
                        && (repository.hasPersonThisStudentId(user.getPersonal_id(), (long) id))) {
                    LOGGER.info("Getting enrollments form student id " + id);
                    List<Enroll> enrolls = repository.findByStudentId(id);
                    response.setStatus(SC_OK);
                    response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                    objectMapper.writeValue(response.getOutputStream(), enrolls);
                } else {
                    response.setStatus(SC_FORBIDDEN);
                }
            } else {
                //admin can only search the enrollment from id
                if ((user.getRole().equals("administrator"))) {
                    int id = Integer.parseInt(request.getPathInfo().substring(1));
                    LOGGER.info("Getting enrollments form id " + id);
                    Enroll enroll = repository.findById(id);
                    if (enroll == null) {
                        response.sendError(SC_NOT_FOUND);
                        return;
                    }
                    response.setStatus(SC_FOUND);
                    response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                    objectMapper.writeValue(response.getOutputStream(), enroll);
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
     * Put-method for updating an enrollment - needs an id in request path
     */
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Person user = (Person) request.getAttribute("user");
        if ((user.getRole().equals("student"))) {
            response.setStatus(SC_FORBIDDEN);
            return;
        }

        Connection connection = ConnectionManager.getConnection(false);
        EnrollRepository repository = new EnrollRepository(connection);

        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                return;
            }
            //get the enrollment id
            int id = Integer.parseInt(pathInfo.substring(1));
            //Teacher can only assign grades if he is teacher for this enrollment id
            if ((user.getRole().equals("administrator")
                    || repository.isPersonTeacherForThisEnrollment((long) id, user.getPersonal_id()))) {
                Enroll enroll = objectMapper.readValue(request.getInputStream(), Enroll.class);
                if (enroll == null || enroll.getId() == 0 || enroll.getId() != id) {
                    response.sendError(SC_BAD_REQUEST);
                    return;
                }
                LOGGER.info("Updating enrollment with id " + id);
                boolean success = repository.update(enroll);
                response.setStatus(success ? SC_NO_CONTENT : SC_NOT_ACCEPTABLE);
                response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                objectMapper.writeValue(response.getOutputStream(), enroll);
                ConnectionManager.commit(connection);
            } else {
                response.setStatus(SC_FORBIDDEN);
            }
        } catch (NumberFormatException ex) {
            response.sendError(SC_NOT_FOUND);
        } catch (JsonParseException ex) {
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
     * Post-method for inserting a new enrollment
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Person user = (Person) request.getAttribute("user");
        Connection connection = ConnectionManager.getConnection(false);
        EnrollRepository repository = new EnrollRepository(connection);

        try {
            Enroll enroll = objectMapper.readValue(request.getInputStream(), Enroll.class);
            if (enroll == null) {
                response.sendError(SC_BAD_REQUEST);
                return;
            }
            // admin can enroll every student - student can only enroll himself
            if ((user.getRole().equals("administrator")
                    || ((user.getRole().equals("student"))
                    && (repository.hasPersonThisStudentId(user.getPersonal_id(), enroll.getStudent_id()))))) {
                LOGGER.info("Adding enrollment with studentId " + enroll.getStudent_id());
                long id = repository.persist(enroll);
                System.out.println(id);
                enroll.setId(id);
                enroll.setRunning(false);
                response.setStatus(SC_CREATED);
                response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                objectMapper.writeValue(response.getOutputStream(), enroll);
                ConnectionManager.commit(connection);
            } else {
                response.setStatus(SC_FORBIDDEN);
            }
        } catch (SQLException ex) {
            ConnectionManager.rollback(connection);
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
        } catch (JsonParseException ex) {
            response.sendError(SC_BAD_REQUEST);
        } finally {
            ConnectionManager.close(connection);
        }

    }

    /**
     * Delete-method for deleting an enrollment - needs an enrollment-id in request path
     */
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = ConnectionManager.getConnection(false);
        EnrollRepository repository = new EnrollRepository(connection);
        Person user = (Person) request.getAttribute("user");
        int id = Integer.parseInt(request.getPathInfo().substring(1));
        try {
            if ((user.getRole().equals("administrator")
                    || ((user.getRole().equals("student"))
                    && (repository.hasPersonThisStudentId(user.getPersonal_id(), (long) id))))) {
                LOGGER.info("Deleting enroll with id " + id);
                boolean success = repository.delete(id);
                response.setStatus(success ? SC_NO_CONTENT : SC_NOT_FOUND);
                ConnectionManager.commit(connection);
            } else {
                response.setStatus(SC_FORBIDDEN);
            }
        } catch (SQLException ex) {
            ConnectionManager.rollback(connection);
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
        } catch (NumberFormatException ex) {
            response.sendError(SC_NOT_FOUND);
        } finally {
            ConnectionManager.close(connection);
        }
    }
}
