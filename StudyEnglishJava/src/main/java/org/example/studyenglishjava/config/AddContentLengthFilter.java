package org.example.studyenglishjava.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
public class AddContentLengthFilter extends OncePerRequestFilter {
    private static final int MAX_CACHE_SIZE = 1024 * 1024 * 1024; // 1MB
    // logger
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AddContentLengthFilter.class);
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 根据请求路径或其他条件决定是否应用此过滤器
        return request.getServletPath().startsWith("/app/words/list");
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException {

        ContentCachingResponseWrapper cacheResponseWrapper;
        if (!(response instanceof ContentCachingResponseWrapper)) {
            cacheResponseWrapper = new ContentCachingResponseWrapper(response);
        } else {
            cacheResponseWrapper = (ContentCachingResponseWrapper) response;
        }

        filterChain.doFilter(request, cacheResponseWrapper);

        logger.info("getContentSize : " + cacheResponseWrapper.getContentSize());
        if (cacheResponseWrapper.getContentSize() <= MAX_CACHE_SIZE) {
            cacheResponseWrapper.copyBodyToResponse();
        } else {
            // 对于大型响应，不设置 Content-Length，而是使用分块传输编码
            response.setHeader("Transfer-Encoding", "chunked");
            StreamUtils.copy(cacheResponseWrapper.getContentInputStream(), response.getOutputStream());
        }
    }
}