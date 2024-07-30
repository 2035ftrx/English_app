package org.example.studyenglishjava.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.example.studyenglishjava.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {
    private final ObjectMapper objectMapper;
    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    public ResponseAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response
    ) {
        if (body instanceof ResponseEntity) {
            return body;
        }
        // logger.debug("beforeBodyWrite : " + body);
        if (body instanceof String) {
            try {
                return objectMapper.writeValueAsString(new ApiResponse<>(1, true, "success", body));
            } catch (JsonProcessingException e) {
                return new ApiResponse<>(0, false, "failure", body);
            }
        }
        if (body instanceof ApiResponse) {
            // response.getHeaders().add("Content-Length", String.valueOf(resource.contentLength()));
            return body;
        }
        return new ApiResponse<>(1, true, "success", body);
    }
}