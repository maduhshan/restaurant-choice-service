package gov.sg.tech.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ControllerLoggerAspect {

    @Around("@annotation(ControllerLogger)")
    public Object logControllerMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = getMethodName(joinPoint);
        Object[] methodArgs = joinPoint.getArgs();
        log.info("Invoked controller method: {}, with args: {}", methodName, getArgumentsAsString(methodArgs));
        Object result = joinPoint.proceed();

        log.info("Controller operation success for method: {}, with response: {}", methodName, result);
        return result;
    }

    private String getMethodName(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        return className + "." + methodName;
    }

    private String getArgumentsAsString(Object[] arguments) {
        StringBuilder argumentsString = new StringBuilder("[");
        for (Object argument : arguments) {
            argumentsString.append(argument).append(", ");
        }
        if (arguments.length > 0) {
            argumentsString.setLength(argumentsString.length() - 2);
        }
        argumentsString.append("]");
        return argumentsString.toString();
    }
}
