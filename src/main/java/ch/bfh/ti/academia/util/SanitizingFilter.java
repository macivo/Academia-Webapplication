/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.util;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_NOT_ACCEPTABLE;


/**
 * Used to sanitize potential codeinjections (HTML/ Javascript). Requests that contain certain characters will be denied
 */
@WebFilter(urlPatterns = "/api/*")
public class SanitizingFilter implements Filter {

    private static final String HTML_PATTERN = "[<>$()=;/]"; //Regex: characters that get rejected by filter

    @Override
    public void init(FilterConfig config) { }

    /**
     * Filter-method for sanitizing filter
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            if (httpRequest.getMethod().equals("POST") || httpRequest.getMethod().equals("PUT")) {
                request = new RequestWrapper(httpRequest);
                String result = ((RequestWrapper) request).getBody();
                Pattern pattern = Pattern.compile(HTML_PATTERN);
                Matcher matcher = pattern.matcher(result);
                if (matcher.find()) {
                    httpResponse.setStatus(SC_NOT_ACCEPTABLE);
                    return;
                }
            }
            chain.doFilter(request, response);
        } catch (Exception ex) {
            httpResponse.setStatus(SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Destroy-method
     */
    @Override
    public void destroy() { }
}
