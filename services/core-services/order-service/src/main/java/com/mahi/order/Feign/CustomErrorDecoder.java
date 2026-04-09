package com.mahi.order.Feign;

import com.mahi.order.exception.OrderServiceException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus responseStatus = HttpStatus.valueOf(response.status());

        if (responseStatus.is4xxClientError()) {
            if (response.status() == 404) {
                return new OrderServiceException("Resource not found for method key: " + methodKey);
            }
            return new OrderServiceException("Client error occurred: " + responseStatus.getReasonPhrase());
        } else if (responseStatus.is5xxServerError()) {
            return new OrderServiceException("External service error: " + responseStatus.getReasonPhrase());
        }

        return defaultErrorDecoder.decode(methodKey, response);
    }
}
