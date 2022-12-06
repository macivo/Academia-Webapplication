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
import java.util.logging.Logger;

/**
 * The class LoggingFilter is used to log HTTP requests and their response status.
 */
@WebFilter(urlPatterns = "/api/*")
public class LoggingFilter implements Filter {

	private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());

	@Override
	public void init(FilterConfig config) { }

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String message = httpRequest.getMethod() + " " + httpRequest.getRequestURI();
		if (httpRequest.getQueryString() != null) {
			message += "?" + httpRequest.getQueryString();
		}
		LOGGER.info("Receiving request: " + message);
		chain.doFilter(request, response);
		LOGGER.info("Returning response status: " + httpResponse.getStatus());
	}

	@Override
	public void destroy() { }
}
