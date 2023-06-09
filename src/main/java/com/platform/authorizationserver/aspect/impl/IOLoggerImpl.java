package com.platform.authorizationserver.aspect.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.authorizationserver.aspect.IOLogger;
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
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 27.05.2023.
 *
 * <p>Logs input and output of method invocation.</p>
 *
 * <p>Author : Teodor Maeli</p>
 */

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class IOLoggerImpl {

    private ObjectMapper mapper;

    @Pointcut("@annotation(com.platform.authorizationserver.aspect.IOLogger) && execution(* *(..))")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void logInput(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        boolean mask = method.getAnnotation(IOLogger.class).mask();
        RequestMapping mapping = getRequestMapping(method);

        Map<String, Object> parameters = getParameters(joinPoint);

        log("==Input==> path(s): {}, method(s): {}, arguments: {} ", mapping, parameters, mask);
    }

    @AfterReturning(pointcut = "pointCut()", returning = "object")
    public void logOutput(JoinPoint joinPoint, Object object) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        boolean mask = method.getAnnotation(IOLogger.class).mask();
        RequestMapping mapping = getRequestMapping(method);

        log("<==Output== path(s): {}, method(s): {}, returning: {}", mapping, object, mask);
    }

    private void log(String format, RequestMapping mapping, Object object, boolean shouldMask) {
        try {
            log.info(format,
                mapping.path(), mapping.method(),
                maskIfRequired(shouldMask, mapper.writeValueAsString(object)));
        } catch (JsonProcessingException e) {
            log.error("Error while converting", e);
        }
    }

    private Object maskIfRequired(boolean shouldMask, String args) {
        if (shouldMask) {
            if (args == null) {
                return "NULL";
            }
            return args.replaceAll(".(?=.{2})", "x");
        }
        return args;
    }

    private Map<String, Object> getParameters(JoinPoint joinPoint) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();

        HashMap<String, Object> map = new HashMap<>();

        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < parameterNames.length; i++) {
            map.put(parameterNames[i], joinPoint.getArgs()[i]);
        }

        return map;
    }

    private RequestMapping getRequestMapping(Method method) {
        return AnnotatedElementUtils.findAllMergedAnnotations(method, RequestMapping.class)
            .stream()
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid or missing annotation!"));
    }
}
