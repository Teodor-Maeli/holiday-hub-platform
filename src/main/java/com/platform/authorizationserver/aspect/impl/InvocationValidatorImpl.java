package com.platform.authorizationserver.aspect.impl;

import com.platform.authorizationserver.aspect.InvocationValidator;
import com.platform.authorizationserver.behavioral.HandlerContext.HandlerKey;
import java.util.List;
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
@Component
public class InvocationValidatorImpl {

    @Pointcut("@annotation(com.platform.authorizationserver.aspect.InvocationValidator) && execution(* *(..))")
    public void validator() {
    }

    @Around("validator()")
    public void validate(ProceedingJoinPoint joinPoint) throws Throwable {
        List<HandlerKey> configuredKeys = getConfiguredKeys(joinPoint);
        HandlerKey inputKey = getInputKey(joinPoint);

        if (configuredKeys.contains(inputKey)) {
            joinPoint.proceed();
            return;
        }

        throw new IllegalArgumentException("Handler key not eligible for required action!");
    }

    private static List<HandlerKey> getConfiguredKeys(ProceedingJoinPoint joinPoint) {
        return List.of(((MethodSignature) joinPoint.getSignature())
            .getMethod()
            .getAnnotation(InvocationValidator.class)
            .keys());
    }

    private HandlerKey getInputKey(ProceedingJoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof HandlerKey key) {
                return key;
            }
        }

        throw new IllegalArgumentException("No valid handler key were provided!");
    }
}
