package com.platform.aspect.audit;

import com.platform.exception.PlatformBackendException;
import com.platform.helper.JsonMasker;
import lombok.RequiredArgsConstructor;
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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Logs input and output of method invocation.
 * Since 1.0.
 */

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditedAspect {

  private final JsonMasker masker;

  @Pointcut("@within(com.platform.aspect.audit.Audited)")
  public void pointCut() {
  }

  @Before("pointCut()")
  public void logInput(JoinPoint joinPoint) {
    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
    RequestMapping mapping = getRequestMapping(method);
    String masked = masker.mask(getParameters(joinPoint));

    log("==Input==> path(s): {}, method(s): {}, arguments: {}", mapping, masked);
  }

  @AfterReturning(pointcut = "pointCut()", returning = "object")
  public void logOutput(JoinPoint joinPoint, Object object) {
    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
    RequestMapping mapping = getRequestMapping(method);
    String masked = masker.mask(object);

    log("<==Output== path(s): {}, method(s): {}, returning: {}", mapping, masked);
  }

  private void log(String format, RequestMapping mapping, String value) {
    log.info(format, mapping.path(), mapping.method(), value);
  }

  private Map<String, Object> getParameters(JoinPoint joinPoint) {
    CodeSignature signature = (CodeSignature) joinPoint.getSignature();
    String[] parameterNames = signature.getParameterNames();

    HashMap<String, Object> parameters = new HashMap<>();

    for (int i = 0; i < parameterNames.length; i++) {
      parameters.put(parameterNames[i], joinPoint.getArgs()[i]);
    }

    return parameters;
  }

  private RequestMapping getRequestMapping(Method method) {
    return
        AnnotatedElementUtils
            .findAllMergedAnnotations(method, RequestMapping.class)
            .stream()
            .findFirst()
            .orElseThrow(() -> new PlatformBackendException()
                .setMessage("Invalid or missing annotation!")
                .setHttpStatus(INTERNAL_SERVER_ERROR));

  }
}
