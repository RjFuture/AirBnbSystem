package com.onlinebookingsystem.airbnbsystem.advice;


import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {


    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

        // wrap only if the controller class Or The method has @ResponseResultBody
        return (returnType.hasMethodAnnotation(ResponseResultBody.class) ||
                AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseResultBody.class));
    }

    @Override
    public @Nullable Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType,
                                            MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                            ServerHttpRequest request, ServerHttpResponse response) {

        if(body instanceof ApiResponse<?>) {
            return body;
        }
        return new ApiResponse<>(body);
    }
}

