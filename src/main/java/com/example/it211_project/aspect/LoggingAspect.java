package com.example.it211_project.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around(
            "execution(* com.example.it211_project.controller..*(..)) " +
                    "|| execution(* com.example.it211_project.service..*(..))"
    )
    public Object logControllerAndServiceMethods(
            ProceedingJoinPoint joinPoint
    ) throws Throwable {

        String className = joinPoint
                .getSignature()
                .getDeclaringType()
                .getSimpleName();

        String methodName = joinPoint
                .getSignature()
                .getName();

        long startTime = System.currentTimeMillis();

        log.info("START {}.{}", className, methodName);

        try {
            Object result = joinPoint.proceed();

            long duration = System.currentTimeMillis() - startTime;

            log.info(
                    "SUCCESS {}.{} - {} ms",
                    className,
                    methodName,
                    duration
            );

            return result;

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;

            log.error(
                    "ERROR {}.{} - {} ms - {}",
                    className,
                    methodName,
                    duration,
                    e.getMessage()
            );

            throw e;
        }
    }
}