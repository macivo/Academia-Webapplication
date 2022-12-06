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
@WebServlet(urlPatterns = "/api/persons/*")
public class PersonServlet extends HttpServlet {
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String JSON_MEDIA_TYPE = "application/json";
    private static final Logger LOGGER = Logger.getLogger(PersonRepository.class.getName());
    private final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
    private final int PATH_USERNAME = 7;
    /**
     * Get-request for getting person/s
     * If username of a person in the path, then get a person from the username
     * If no username in the path, then get all persons (the function not in use, but maybe in the future)
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = ConnectionManager.getConnection(true);
        PersonRepository repository = new PersonRepository(connection);
        try {
            if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                return;
            }
            if (request.getPathInfo().equals("/profs")) {

                LOGGER.info("Getting all profs");
                Person user = (Person) request.getAttribute("userData");
                LOGGER.info("Getting all profs");
                List<Person> profs = repository.getAllProfs();
                response.setStatus(SC_OK);
                response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                objectMapper.writeValue(response.getOutputStream(), profs);
            } else if (request.getPathInfo().contains("login")) {

                String username = request.getPathInfo().substring(PATH_USERNAME);
                LOGGER.info("Getting person from a username " + username);
                Person person = repository.findPersonByUsername(username);
                if (person == null) {
                    response.sendError(SC_NOT_FOUND);
                    return;
                }
                response.setStatus(SC_OK);
                response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                objectMapper.writeValue(response.getOutputStream(), person);
            } else {
                int id = Integer.parseInt(request.getPathInfo().substring(1));
                LOGGER.info("Getting person by id " + id);
                Person person = repository.findById(id);
                if (person == null) {
                    response.sendError(SC_NOT_FOUND);
                    return;
                }
                response.setStatus(SC_OK);
                response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                objectMapper.writeValue(response.getOutputStream(), person);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
        } catch (NumberFormatException ex) {
            response.sendError(SC_NOT_FOUND);
        } finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * Post-method for creating a new person (do not use for student)
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Person user = (Person) request.getAttribute("user");

        //only administrator can create a new person
        if (user.getRole().equals("administrator")) {
            Connection connection = ConnectionManager.getConnection(false);
            PersonRepository repository = new PersonRepository(connection);
            try {
                //read json and build Object student
                Person person = objectMapper.readValue(request.getInputStream(), Person.class);
                if (person == null) {
                    response.sendError(SC_BAD_REQUEST);
                    return;
                }
                LOGGER.info("Adding a new person");
                long id = repository.persist(person);
                person.setPersonal_id(id);
                response.setStatus(SC_CREATED);
                response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                objectMapper.writeValue(response.getOutputStream(), person);
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
     * Put-method for updating a person
     */
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Person user = (Person) request.getAttribute("user");

        //only administrator can create a new person
        if (user.getRole().equals("administrator")) {
            Connection connection = ConnectionManager.getConnection(false);
            PersonRepository repository = new PersonRepository(connection);
            try {
                String pathInfo = request.getPathInfo();
                Person person = objectMapper.readValue(request.getInputStream(), Person.class);
                if (person == null || person.getPersonal_id() == 0) {
                    response.sendError(SC_BAD_REQUEST);
                    return;
                }
                LOGGER.info("Updating person with id " + person.getPersonal_id());
                boolean success = repository.update(person);
                response.setStatus(success ? SC_NO_CONTENT : SC_NOT_ACCEPTABLE);
                response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
                objectMapper.writeValue(response.getOutputStream(), person);
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
     * Delete-method for deleting a person - needs a person-id in request path
     */
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Person user = (Person) request.getAttribute("user");

        //only administrator can create a new person
        if (user.getRole().equals("administrator")) {
            Connection connection = ConnectionManager.getConnection(false);
            PersonRepository repository = new PersonRepository(connection);
            try {
                int id = Integer.parseInt(request.getPathInfo().substring(1));
                LOGGER.info("Deleting person with id " + id);
                boolean success = repository.delete(id);
                response.setStatus(success ? SC_NO_CONTENT : SC_NOT_FOUND);
                ConnectionManager.commit(connection);
            } catch (SQLException ex) {
                ConnectionManager.rollback(connection);
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
                response.setStatus(SC_INTERNAL_SERVER_ERROR);
            } catch (NumberFormatException ex) {
                response.sendError(SC_NOT_FOUND);
            } finally {
                ConnectionManager.close(connection);
            }
        } else {
            response.setStatus(SC_FORBIDDEN);
        }
    }
}
