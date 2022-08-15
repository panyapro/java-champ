package cup.online.javachamp.service;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Component
public class RestRequestIdInterceptor implements HandlerInterceptor {

    private String REQUEST_ID_HEADER = "X-Request-Id";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceID = UUID.randomUUID().toString();
        if (nonNull(request.getHeader(REQUEST_ID_HEADER)) && !request.getHeader(REQUEST_ID_HEADER).isEmpty()) {
            traceID = request.getHeader(REQUEST_ID_HEADER);
        }
        MDC.put("requestId", traceID);
        return true;
    }
}