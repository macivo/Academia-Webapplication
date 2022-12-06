/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.teach;

import ch.bfh.ti.academia.module.ModuleRepository;
import ch.bfh.ti.academia.person.Person;
import ch.bfh.ti.academia.modulerun.ModuleRun;
import ch.bfh.ti.academia.person.Student;
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
 * The class TeachingServlet provides REST endpoints for the administration of teacher view.
 */
@WebServlet(urlPatterns = {"/api/teaching/*", "/api/profteaching/*", "/api/studentsmodule/*"})
public class TeachingServlet extends HttpServlet {

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String JSON_MEDIA_TYPE = "application/json";
    private static final Logger LOGGER = Logger.getLogger(ModuleRepository.class.getName());
    private final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

    /**
     * Get-method for getting all teachers for a running module,
     * all running modules for a teacher and the enrolled students for a running module
     * It has three possible paths for the get methode
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = ConnectionManager.getConnection(true);
        TeachingRepository repository = new TeachingRepository(connection);
        Person user = (Person) request.getAttribute("user");
        try {
            if (request.getPathInfo() == null) {
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                return;
            } else {
                if (request.getServletPath().equals("/api/profteaching")) {
                    if (user.getRole().equals("administrator")) {
                        try {
                            int id = Integer.parseInt(request.getPathInfo().substring(1));
                            LOGGER.info("Getting teachers for module run with id " + id);
                            List<Teaching> teachings = repository.findProfsForModuleRuns(id);
                            if (teachings == null) {
                                response.sendError(SC_NOT_FOUND);
                                return;
                            }
                            response.setStatus(SC_OK);
                            response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                            objectMapper.writeValue(response.getOutputStream(), teachings);
                        } catch (NumberFormatException ex) {
                            response.sendError(SC_NOT_FOUND);
                        }
                    } else {
                        response.setStatus(SC_FORBIDDEN);
                    }
                }
                if (request.getServletPath().equals("/api/teaching")) {
                    if (user.getRole().equals("professor")) {
                        try {
                            int id = (int) user.getPersonal_id();
                            LOGGER.info("Getting running module for prof with id " + id);
                            List<ModuleRun> moduleRuns = repository.findModuleRunsForProf(id);
                            if (moduleRuns == null) {
                                response.sendError(SC_NOT_FOUND);
                                return;
                            }
                            response.setStatus(SC_OK);
                            response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                            objectMapper.writeValue(response.getOutputStream(), moduleRuns);
                        } catch (NumberFormatException ex) {
                            response.sendError(SC_NOT_FOUND);
                        }
                    } else {
                        response.setStatus(SC_FORBIDDEN);
                    }
                }
                if (request.getServletPath().equals("/api/studentsmodule")) {
                    if (user.getRole().equals("professor")) {
                        try {
                            //the moduleRunId is id
                            int id = Integer.parseInt(request.getPathInfo().substring(1));

                            if (!repository.isPersonTeacherOfModuleRun(user.getPersonal_id(), (long) id)) {
                                response.setStatus(SC_FORBIDDEN);
                                return;
                            }
                            LOGGER.info("Getting students enrolled for module" + id);
                            List<Student> moduleRuns = repository.findStudentsForModule(id);
                            if (moduleRuns == null) {
                                response.sendError(SC_NOT_FOUND);
                                return;
                            }
                            response.setStatus(SC_OK);
                            response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                            objectMapper.writeValue(response.getOutputStream(), moduleRuns);
                        } catch (NumberFormatException ex) {
                            response.sendError(SC_NOT_FOUND);
                        }
                    } else {
                        response.setStatus(SC_FORBIDDEN);
                    }
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
     * Post-method for creating a new dataset in table teaching
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = ConnectionManager.getConnection(false);
        TeachingRepository repository = new TeachingRepository(connection);
        Person user = (Person) request.getAttribute("user");
        System.out.println(request.getServletPath());
        if (!(user.getRole().equals("administrator"))) {
            response.setStatus(SC_FORBIDDEN);
            return;
        }
        try {
            if (request.getServletPath().equals("/api/profteaching")) {
                Teaching teaching = objectMapper.readValue(request.getInputStream(), Teaching.class);
                if (teaching == null) {
                    response.sendError(SC_BAD_REQUEST);
                    return;
                }
                long id = repository.persist(teaching);
                LOGGER.info("Adding teaching with teachingID " + teaching.getTeachingId());
                System.out.println(id);
                teaching.setTeachingId(id);
                response.setStatus(SC_CREATED);
                response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                objectMapper.writeValue(response.getOutputStream(), teaching);
                ConnectionManager.commit(connection);
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
     * Delete-method for deleting a dataset in table teaching
     * Needs an id of teaching in the path for requesting
     */
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = ConnectionManager.getConnection(false);
        TeachingRepository repository = new TeachingRepository(connection);
        Person user = (Person) request.getAttribute("user");
        if (!(user.getRole().equals("administrator"))) {
            response.setStatus(SC_FORBIDDEN);
            return;
        }
        try {
            if (request.getServletPath().equals("/api/profteaching")) {
                int id = Integer.parseInt(request.getPathInfo().substring(1));
                LOGGER.info("Deleting teaching with id " + id);
                boolean success = repository.delete(id);
                response.setStatus(success ? SC_NO_CONTENT : SC_NOT_FOUND);
                ConnectionManager.commit(connection);
            }
        } catch (NumberFormatException ex) {
            response.sendError(SC_NOT_FOUND);
        } catch (SQLException ex) {
            ConnectionManager.rollback(connection);
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
        } finally {
            ConnectionManager.close(connection);
        }
    }
}
