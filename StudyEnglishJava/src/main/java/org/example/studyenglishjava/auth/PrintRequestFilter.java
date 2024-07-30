package org.example.studyenglishjava.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.example.studyenglishjava.config.AddContentLengthFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class PrintRequestFilter extends OncePerRequestFilter {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PrintRequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info(" PrintRequestFilter : " + request.getMethod() + " : " + request.getRequestURL());
        request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
            logger.info(" PrintRequestFilter Header : " + headerName + " : " + request.getHeader(headerName));
        });
        filterChain.doFilter(request, response);
    }
}
