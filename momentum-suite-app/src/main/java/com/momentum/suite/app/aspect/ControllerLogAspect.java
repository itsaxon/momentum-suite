package com.momentum.suite.app.aspect;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * Controller 层日志切面
 * <p>
 * 使用 AOP 记录 Controller 方法的请求信息、入参、出参和执行耗时 。
 * </p>
 *
 * @author itsaxon
 * @version v1.0 2025/09/27
 */
@Aspect
@Component
@Slf4j
public class ControllerLogAspect {

    /**
     * 定义切点，匹配 app.controller 包下的所有公共方法
     */
    @Pointcut("execution(public * com.momentum.suite.app.controller..*.*(..))")
    public void controllerLogPointcut() {
        // 此方法仅作为切点标识，无具体实现
    }

    /**
     * 环绕通知，功能最强大，可以控制方法是否执行
     *
     * @param joinPoint 连接点
     * @return 方法执行结果
     * @throws Throwable 异常
     */
    @Around("controllerLogPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 执行目标方法
        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        log.info("-> Response: {}", JSONUtil.toJsonStr(result));
        log.info("-> Cost    : {} ms",  endTime - startTime);

        return result;
    }

    /**
     * 前置通知，在目标方法执行前调用
     *
     * @param joinPoint 连接点
     */
    @Before("controllerLogPointcut()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

        log.info("================  Request Log Start  ================");
        log.info("-> URL         : {}", request.getRequestURL().toString());
        log.info("-> HTTP Method : {}", request.getMethod());
        log.info("-> Client IP   : {}", request.getRemoteAddr());
        log.info("-> Class Method: {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        log.info("-> Args        : {}", JSONUtil.toJsonStr(joinPoint.getArgs()));
    }

    /**
     * 后置通知，在目标方法执行后调用（无论是否发生异常）
     */
    @After("controllerLogPointcut()")
    public void doAfter() {
        log.info("================   Request Log End   ================");
    }

    /**
     * 异常通知，在目标方法抛出异常后调用
     *
     * @param joinPoint 连接点
     * @param e         异常
     */
    @AfterThrowing(pointcut = "controllerLogPointcut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("!! Exception in Method: {}.{}, Cause: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                e.getMessage(),
                e
        );
    }
}
