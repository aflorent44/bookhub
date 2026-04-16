package fr.bookhub.utility;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceResponse<T>{
    private String code;
    private String message;
    private T data;

    public ServiceResponse() {}
    public ServiceResponse(ApiCode apiCode) {
        this.code = apiCode.code();
        this.message = apiCode.message();
    }
    public ServiceResponse(ApiCode apiCode, T data) {
        this.code = apiCode.code();
        this.message = apiCode.message();
        this.data = data;
    }
}
