package com.platform.aspect;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.platform.exception.BackendException;
import com.platform.util.JsonMaskUtils;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 27.05.2023.
 *
 * <p>Logs input and output of method invocation.</p>
 * Since 1.0.
 *
 * <p>Author : Teodor Maeli</p>
 */

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("@annotation(com.platform.aspect.annotation.IOLogger) && execution(* *(..))")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void logInput(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RequestMapping mapping = getRequestMapping(method);
        String masked = JsonMaskUtils.mask(getParameters(joinPoint));

        log("==Input==> path(s): {}, method(s): {}, arguments: {}", mapping, masked);
    }

    @AfterReturning(pointcut = "pointCut()", returning = "o")
    public void logOutput(JoinPoint joinPoint, Object o) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RequestMapping mapping = getRequestMapping(method);
        String masked = JsonMaskUtils.mask(o);

        log("<==Output== path(s): {}, method(s): {}, returning: {}", mapping, masked);
    }

    private void log(String format, RequestMapping mapping, String value) {
        log.info(format, mapping.path(), mapping.method(), value);
    }

    private Map<String, Object> getParameters(JoinPoint joinPoint) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();
        HashMap<String, Object> parameters = new HashMap<>();
        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < parameterNames.length; i++) {
            parameters.put(parameterNames[i], joinPoint.getArgs()[i]);
        }

        return parameters;
    }

    private RequestMapping getRequestMapping(Method method) {
        return AnnotatedElementUtils
            .findAllMergedAnnotations(method, RequestMapping.class)
            .stream()
            .findFirst()
            .orElseThrow(() -> new BackendException("Invalid or missing annotation!", INTERNAL_SERVER_ERROR));

    }
}
