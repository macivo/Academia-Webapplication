/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.modulerun;

import ch.bfh.ti.academia.module.ModuleRepository;
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
 * The class ModulesRunServlet provides REST endpoints for the administration of modules_run.
 */
@WebServlet(urlPatterns = "/api/moduleruns/*")
public class ModuleRunServlet extends HttpServlet {

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String JSON_MEDIA_TYPE = "application/json";
    private static final Logger LOGGER = Logger.getLogger(ModuleRepository.class.getName());
    private final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

    /**
     * Get-method for getting modulerun/s
     * If id of a module in the path, then get a modulerun from id
     * if no id in the path, then get all moduleruns
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = ConnectionManager.getConnection(true);
        ModuleRunRepository repository = new ModuleRunRepository(connection);
        try {
            System.out.println("PATHINFO = " + request.getPathInfo());
            if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
                LOGGER.info("Getting all module-runs");
                List<ModuleRun> moduleRuns = repository.findAll();
                response.setStatus(SC_OK);
                response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                objectMapper.writeValue(response.getOutputStream(), moduleRuns);
            } else {
                try {
                    int id = Integer.parseInt(request.getPathInfo().substring(1));
                    LOGGER.info("Getting module-run with id " + id);
                    ModuleRun moduleRun = repository.findById(id);
                    if (moduleRun == null) {
                        System.out.println("*** Module not found");
                        response.sendError(SC_NOT_FOUND);
                        return;
                    }
                    response.setStatus(SC_OK);
                    response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                    objectMapper.writeValue(response.getOutputStream(), moduleRun);
                } catch (NumberFormatException ex) {
                    response.sendError(SC_NOT_FOUND);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
        } finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * Post-method for creating a new modulerun
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = ConnectionManager.getConnection(false);
        ModuleRunRepository repository = new ModuleRunRepository(connection);
        Person user = (Person) request.getAttribute("user");

        if (user.getRole().equals("administrator")) {
            try {
                ModuleRun moduleRun = objectMapper.readValue(request.getInputStream(), ModuleRun.class);

                if (moduleRun == null || moduleRun.getId() != null) {
                    response.sendError(SC_BAD_REQUEST);
                    return;
                }
                long id = repository.persist(moduleRun);
                moduleRun.setId(id);
                LOGGER.info("Adding module-run with number " + moduleRun.getId());
                response.setStatus(SC_CREATED);
                response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                objectMapper.writeValue(response.getOutputStream(), moduleRun);
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
     * A put-method for updating a modulerun
     */
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = ConnectionManager.getConnection(false);
        ModuleRunRepository repository = new ModuleRunRepository(connection);
        Person user = (Person) request.getAttribute("user");

        if (user.getRole().equals("administrator")) {
            try {
                String pathInfo = request.getPathInfo();
                if (pathInfo == null || pathInfo.equals("/")) {
                    response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    return;
                }
                int id = Integer.parseInt(pathInfo.substring(1));
                ModuleRun moduleRun = objectMapper.readValue(request.getInputStream(), ModuleRun.class);
                moduleRun.setId((long) id);

                LOGGER.info("Updating module-run with id " + id);
                boolean success = repository.update(moduleRun);
                response.setStatus(success ? SC_NO_CONTENT : SC_NOT_FOUND);
                response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                objectMapper.writeValue(response.getOutputStream(), moduleRun);
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
     * Delete-method for deleting a modulerun
     */
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = ConnectionManager.getConnection(false);
        ModuleRunRepository repository = new ModuleRunRepository(connection);
        Person user = (Person) request.getAttribute("user");

        if (user.getRole().equals("administrator")) {
            try {
                int id = Integer.parseInt(request.getPathInfo().substring(1));
                LOGGER.info("Deleting module-run with id " + id);
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
