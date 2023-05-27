package com.platform.authorizationserver.aspect.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.authorizationserver.aspect.InvocationValidator;
import com.platform.authorizationserver.model.HandlerAction;
import com.platform.authorizationserver.model.HandlerKey;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 *
 * <p>Author : Teodor Maeli</p>
 */

@Aspect
@Slf4j
@Component
@AllArgsConstructor
public class InvocationValidatorImpl {

    private ObjectMapper objectMapper;

    @Pointcut("@annotation(com.platform.authorizationserver.aspect.InvocationValidator) && execution(* *(..))")
    public void pointCut() {
    }

    @Around("pointCut()")
    public void validate(ProceedingJoinPoint joinPoint) throws Throwable {
        HandlerKey[] configuredKeys = getConfiguredKeys(joinPoint);
        HandlerAction inputAction = getInputAction(joinPoint);

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

    private HandlerAction getInputAction(ProceedingJoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof HandlerAction action) {
                return action;
            }
        }

        throw new IllegalArgumentException("No valid handler key were provided!");
    }
}
