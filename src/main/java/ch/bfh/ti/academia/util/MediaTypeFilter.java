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

/**
 * The class MediaTypeFilter is used to check the media type of HTTP requests.
 */
@WebFilter(urlPatterns = "/api/*")
public class MediaTypeFilter implements Filter {

	@Override
	public void init(FilterConfig config) { }

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String acceptHeader = httpRequest.getHeader("Accept");
		if (acceptHeader != null && !acceptHeader.contains("*/*")
				&& !acceptHeader.contains("application/json")) {
			httpResponse.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			return;
		}
		if (httpRequest.getMethod().equals("POST") || httpRequest.getMethod().equals("PUT")) {
			if (!request.getContentType().contains("application/json")) {
				httpResponse.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
				return;
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() { }
}
