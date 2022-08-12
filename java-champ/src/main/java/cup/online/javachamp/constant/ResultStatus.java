package cup.online.javachamp.constant;

import org.springframework.http.HttpStatus;

public enum ResultStatus {

    OK(HttpStatus.OK),
    ACCEPTED(HttpStatus.ACCEPTED),
    NOT_FOUND(HttpStatus.NOT_FOUND);

    private final HttpStatus httpStatus;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    ResultStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
