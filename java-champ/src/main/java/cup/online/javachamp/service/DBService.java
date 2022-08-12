package cup.online.javachamp.service;

import org.springframework.http.HttpStatus;

public interface DBService<T> {

    HttpStatus create(T t);
}
