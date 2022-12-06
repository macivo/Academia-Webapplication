/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.module;

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
 * The class ModulesServlet provides REST endpoints for the administration of modules.
 */
@WebServlet(urlPatterns = "/api/modules/*")
public class ModuleServlet extends HttpServlet {

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String JSON_MEDIA_TYPE = "application/json";
    private static final Logger LOGGER = Logger.getLogger(ModuleRepository.class.getName());
    private final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

    /**
     * Get-method for getting module/s
     * If id of a module in the path, then get a module from id
     * if no id in the path, then get all modules
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = ConnectionManager.getConnection(true);
        ModuleRepository repository = new ModuleRepository(connection);
        try {
            if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
                LOGGER.info("Getting all modules");
                List<Module> modules = repository.findAllWithRunningAndCoord();
                response.setStatus(SC_OK);
                response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                objectMapper.writeValue(response.getOutputStream(), modules);
            } else {
                try {
                    int id = Integer.parseInt(request.getPathInfo().substring(1));
                    LOGGER.info("Getting module with id " + id);
                    Module module = repository.findById(id);
                    if (module == null) {
                        response.sendError(SC_NOT_FOUND);
                        return;
                    }
                    response.setStatus(SC_OK);
                    response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                    objectMapper.writeValue(response.getOutputStream(), module);
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
     * Post-method for creating a new module
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = ConnectionManager.getConnection(false);
        ModuleRepository repository = new ModuleRepository(connection);
        Person user = (Person) request.getAttribute("user");

        if (user.getRole().equals("administrator")) {
            try {
                Module module = objectMapper.readValue(request.getInputStream(), Module.class);

                if (module == null || module.getId() != null) {
                    response.sendError(SC_BAD_REQUEST);
                    return;
                }
                if (!(repository.isThisPersonProf(module.getCoordinator()))) {
                    response.sendError(SC_FORBIDDEN);
                    return;
                }
                LOGGER.info("Adding module with number " + module.getNumber());
                long id = repository.persist(module);
                System.out.println(id);
                module.setId(id);
                module.setRunning(false);
                response.setStatus(SC_CREATED);
                response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                objectMapper.writeValue(response.getOutputStream(), module);
                ConnectionManager.commit(connection);
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
     * A put-method for updating a module
     */
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = ConnectionManager.getConnection(false);
        ModuleRepository repository = new ModuleRepository(connection);
        Person user = (Person) request.getAttribute("user");

        if (user.getRole().equals("administrator") || (user.getRole().equals("professor"))) {
            try {
                String pathInfo = request.getPathInfo();
                if (pathInfo == null || pathInfo.equals("/")) {
                    response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    return;
                }
                int id = Integer.parseInt(pathInfo.substring(1));
                Module module = objectMapper.readValue(request.getInputStream(), Module.class);

                if ((user.getRole().equals("professor")
                        && !(repository.isPersonModuleCoordinator(user.getPersonal_id(), (long) id)))) {
                    response.setStatus(SC_FORBIDDEN);
                    return;
                }

                if (module == null || module.getId() == null || module.getId() != id) {
                    response.sendError(SC_BAD_REQUEST);
                    return;
                }

                LOGGER.info("Updating module with id " + id);
                boolean success = repository.update(module);
                response.setStatus(success ? SC_NO_CONTENT : SC_NOT_FOUND);
                response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                objectMapper.writeValue(response.getOutputStream(), module);
                ConnectionManager.commit(connection);
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
        } else {
            response.setStatus(SC_FORBIDDEN);
        }
    }

    /**
     * Delete-method for deleting a module
     * Need a module id in the path for requesting
     */

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = ConnectionManager.getConnection(false);
        ModuleRepository repository = new ModuleRepository(connection);
        Person user = (Person) request.getAttribute("user");

        if (user.getRole().equals("administrator")) {
            try {
                int id = Integer.parseInt(request.getPathInfo().substring(1));
                LOGGER.info("Deleting module with id " + id);
                boolean success = repository.delete(id);
                response.setStatus(success ? SC_NO_CONTENT : SC_NOT_FOUND);
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
