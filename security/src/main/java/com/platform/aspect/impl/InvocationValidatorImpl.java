package com.platform.aspect.impl;

import  com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.aspect.InvocationValidator;
import com.platform.model.RequestAction;
import com.platform.model.HandlerKey;
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
public class InvocationValidatorImpl {

    private ObjectMapper objectMapper;

    @Pointcut("@annotation(com.platform.aspect.InvocationValidator) && execution(* *(..))")
    public void pointCut() {
    }

    @Around("pointCut()")
    public void validate(ProceedingJoinPoint joinPoint) throws Throwable {
        HandlerKey[] configuredKeys = getConfiguredKeys(joinPoint);
        RequestAction inputAction = getInputAction(joinPoint);

        for (HandlerKey key : configuredKeys) {
            if (key.getActions().contains(inputAction)) {
                joinPoint.proceed();
                return;
            }
        }

        throw new IllegalArgumentException(
            String.format("Handler action : %s fails!Not supported by configuredKeys : %s!",
                inputAction, objectMapper.writeValueAsString(configuredKeys)));
    }

    private HandlerKey[] getConfiguredKeys(ProceedingJoinPoint joinPoint) {
        return ((MethodSignature) joinPoint.getSignature())
            .getMethod()
            .getAnnotation(InvocationValidator.class)
            .keys();
    }

    private RequestAction getInputAction(ProceedingJoinPoint joinPoint) {
        return (RequestAction) Arrays.stream(joinPoint.getArgs())
            .filter(RequestAction.class::isInstance)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No valid handler key were provided!"));
    }
}