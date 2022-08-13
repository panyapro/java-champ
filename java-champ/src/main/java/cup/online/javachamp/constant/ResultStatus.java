package cup.online.javachamp.constant;

import org.springframework.http.HttpStatus;

public enum ResultStatus {

    OK(HttpStatus.OK),
    ACCEPTED(HttpStatus.CREATED),
    NOT_FOUND(HttpStatus.NOT_FOUND),
    NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE);

    private final HttpStatus httpStatus;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    ResultStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
