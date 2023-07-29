package com.platform.aspect;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.aspect.annotation.InvocationValidator;
import com.platform.exception.SecurityException;
import com.platform.model.AuthHandlerKey;
import com.platform.model.RequestAction;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 27.05.2023.
 *
 * <p>Validates handler keys against RestController endpoints.</p>
 * Since 1.0.
 *
 * <p>Author : Teodor Maeli</p>
 */

@Aspect
@Component
@AllArgsConstructor
public class ValidatorAspect {

    private ObjectMapper objectMapper;

    @Pointcut("@annotation(com.platform.aspect.annotation.InvocationValidator) && execution(* *(..))")
    public void pointCut() {
    }

    @Around("pointCut()")
    public void validate(ProceedingJoinPoint joinPoint) throws Throwable {
        AuthHandlerKey[] configuredKeys = getConfiguredKeys(joinPoint);
        RequestAction inputAction = getInputAction(joinPoint);

        for (AuthHandlerKey key : configuredKeys) {
            if (key.getActions().contains(inputAction)) {
                joinPoint.proceed();
                return;
            } else {
                throw SecurityException.builder()
                                       .action(inputAction)
                                       .httpStatus(BAD_REQUEST)
                                       .message("Action invalid or not configured with handler keys!")
                                       .build();
            }
        }
    }

    private AuthHandlerKey[] getConfiguredKeys(ProceedingJoinPoint joinPoint) {
        return ((MethodSignature) joinPoint.getSignature())
            .getMethod()
            .getAnnotation(InvocationValidator.class)
            .keys();
    }

    private RequestAction getInputAction(ProceedingJoinPoint joinPoint) {
        return (RequestAction) Arrays.stream(joinPoint.getArgs())
                                     .filter(RequestAction.class::isInstance)
                                     .findFirst()
                                     .orElseThrow(() -> SecurityException.builder()
                                                                         .httpStatus(BAD_REQUEST)
                                                                         .message("No valid handler key were provided!")
                                                                         .build());
    }
}