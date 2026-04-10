package fr.bookhub.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceResponse<T>{
    private String code;
    private String message;
    private T data;

    public ServiceResponse() {}
    public ServiceResponse(String code) {}
    public ServiceResponse(String code, String message) {}
    public ServiceResponse(String code, String message, T data) {}
}
