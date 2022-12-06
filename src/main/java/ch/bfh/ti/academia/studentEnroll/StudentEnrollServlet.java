/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.studentEnroll;

import ch.bfh.ti.academia.enroll.Enroll;
import ch.bfh.ti.academia.module.ModuleRepository;
import ch.bfh.ti.academia.person.Person;
import ch.bfh.ti.academia.util.ConnectionManager;
import ch.bfh.ti.academia.util.ObjectMapperFactory;
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
 * The class StudentEnrollServlet provides REST endpoints for the enrollment to modules.
 */
@WebServlet(urlPatterns = "/api/studentEnroll/*")
public class StudentEnrollServlet extends HttpServlet {

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String JSON_MEDIA_TYPE = "application/json";
    private static final Logger LOGGER = Logger.getLogger(ModuleRepository.class.getName());
    private final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

    /**
     * Get-method for getting enrollments
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = ConnectionManager.getConnection(true);
        StudentEnrollRepository repository = new StudentEnrollRepository(connection);
        Person user = (Person) request.getAttribute("user");

        if (user.getRole().equals("student")) {
            try {
                if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
                    LOGGER.info("Getting all running modules");
                    List<StudentEnroll> studentEnrolls = repository.findAllRunningModulesWithoutTeacher();
                    response.setStatus(SC_OK);
                    response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                    objectMapper.writeValue(response.getOutputStream(), studentEnrolls);
                } else {
                    int id = (int) user.getPersonal_id();
                    LOGGER.info("Getting enrolled running modules for student with personal id " + id);
                    List<Enroll> studentEnrolls = repository.findEnrolledModulesByPersonaId(id);
                    response.setStatus(SC_OK);
                    response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                    objectMapper.writeValue(response.getOutputStream(), studentEnrolls);
                    if (studentEnrolls == null) {
                        response.sendError(SC_NOT_FOUND);
                        return;
                    }
                    response.setStatus(SC_OK);
                    response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                    objectMapper.writeValue(response.getOutputStream(), studentEnrolls);
                }
            } catch (SQLException ex) {
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






