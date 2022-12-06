/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.util;

import ch.bfh.ti.academia.person.Person;
import ch.bfh.ti.academia.person.PersonRepository;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.servlet.http.HttpServletResponse.*;


/**
 * The class AuthenticationFilter is used to check the authentication of the user of the HTTP requests
 * and gives the credentials to the servlet.
 */
@WebFilter(urlPatterns = {"/api/*"})

public class AuthenticationFilter implements Filter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String AUTH_SCHEME = "Basic";

    private static final Logger LOGGER = Logger.getLogger(PersonRepository.class.getName());


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * Filter-method for authentication filter
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        Connection connection = ConnectionManager.getConnection(true);
        try {
            String authHeader = httpRequest.getHeader(AUTH_HEADER);
            String[] headerTokens = authHeader.split(" ");
            if (!headerTokens[0].equals(AUTH_SCHEME)) throw new Exception();
            byte[] decoded = Base64.getDecoder().decode(headerTokens[1]);
            String[] credentials = new String(decoded, StandardCharsets.UTF_8).split(":");

            //validate credentials and throw exception if invalid
            String user = credentials[0];
            String password = credentials[1];

            //Check if password fits username
            PersonRepository repository = new PersonRepository(connection);
            LOGGER.info("Getting person from a username " + user);
            Person person = repository.findPersonByUsername(user);
            if (person == null) {
                httpResponse.sendError(SC_UNAUTHORIZED);
                return;
            }
            if (!person.getPassword().equals(password)) {
                System.out.println("wrong password" + password);
                httpResponse.setStatus(SC_UNAUTHORIZED);
                return;
            }
            if (person.getPassword().equals(password)) {
                httpRequest.setAttribute("user", person);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            httpResponse.setStatus(SC_INTERNAL_SERVER_ERROR);
            return;
        } catch (Exception ex) {
            System.out.println("wrong password EX" + ex.getMessage());
            httpResponse.setStatus(SC_UNAUTHORIZED);
            return;
        } finally {
            ConnectionManager.close(connection);
        }
        chain.doFilter(request, response);
    }

    /**
     * Destroy-method
     */
    @Override
    public void destroy() {

    }
}
