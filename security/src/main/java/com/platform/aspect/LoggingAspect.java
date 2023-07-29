package com.platform.aspect;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.aspect.annotation.Mask;
import com.platform.exception.SecurityException;
import com.platform.model.dto.PlatformClientRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
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

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class LoggingAspect {

    private ObjectMapper mapper;

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
            AnnotationUtils.findAnnotation(PlatformClientRequest.class, Mask.class);
            log.info(format, mapping.path(), mapping.method(), mapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            log.error("Error while converting", e);
        }
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
                                    .orElseThrow(() -> SecurityException.builder()
                                                                        .httpStatus(INTERNAL_SERVER_ERROR)
                                                                        .message("Invalid or missing annotation!")
                                                                        .build());
    }
}
