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
    public ServiceResponse(String code) { this.code = code; }
    public ServiceResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
    public ServiceResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
