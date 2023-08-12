package com.platform.aspect;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.aspect.annotation.Mask;
import com.platform.exception.BackendException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    private final ObjectMapper mapper;

    public LoggingAspect(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Pointcut("@annotation(com.platform.aspect.annotation.IOLogger) && execution(* *(..))")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void logInput(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RequestMapping mapping = getRequestMapping(method);
        Map<String, Object> parameters = getParameters(joinPoint);

        log("==Input==> path(s): {}, method(s): {}, arguments: {}", mapping, parameters);
    }

    @AfterReturning(pointcut = "pointCut()", returning = "object")
    public void logOutput(JoinPoint joinPoint, Object object) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RequestMapping mapping = getRequestMapping(method);

        log("<==Output== path(s): {}, method(s): {}, returning: {}", mapping, object);
    }

    private void log(String format, RequestMapping mapping, Object object) {
        try {
            String value = mapper.writeValueAsString(object);
            log.info(format, mapping.path(), mapping.method(), value);
        } catch (JsonProcessingException e) {
            log.error("Error while converting", e);
        }
    }

    private List<String> getMaskedFields(JoinPoint joinPoint) {
        return Arrays.stream(joinPoint.getArgs().getClass().getDeclaredFields())
                     .filter(this::constainsMask)
                     .map(Field::getName)
                     .toList();
    }

    private boolean constainsMask(Field field) {
        return Arrays.stream(field.getDeclaredAnnotations())
                     .anyMatch(Mask.class::isInstance);
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
        return AnnotatedElementUtils.findAllMergedAnnotations(method, RequestMapping.class)
                                    .stream()
                                    .findFirst()
                                    .orElseThrow(() -> new BackendException("Invalid or missing annotation!", INTERNAL_SERVER_ERROR));
    }
}
