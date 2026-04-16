package fr.bookhub.utility;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final ApiCode apiCode;

    public ApiException(ApiCode apiCode) {
        super(apiCode.message());
        this.apiCode = apiCode;
    }

    public ApiCode getApiCode() {
        return apiCode;
    }
}