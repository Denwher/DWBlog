package com.dengwei.aspect;

import com.alibaba.fastjson.JSON;
import com.dengwei.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author Denwher
 * @version 1.0
 */
@Component
@Aspect
@Slf4j
public class LogAspect {

    //定义切点：所有加上@SystemLog注解的方法都是切点
    @Pointcut("@annotation(com.dengwei.annotation.SystemLog)")
    public void pt(){}

    //绑定切入点和通知间的关系：表示在切入点方法前执行共性功能
    @Around("pt()")
    //共性功能（通知）
    public Object printLog(ProceedingJoinPoint pjp) throws Throwable {
        Object ret = null;
        try {
            handleBefore(pjp);
            ret = pjp.proceed();
            handleAfter(ret);
        } finally {
            // 结束后换行
            log.info("=======End=======" + System.lineSeparator());
        }
        return ret;
    }

    private void handleBefore(ProceedingJoinPoint jp){
        //xxxxHolder一般是用threadlocal保存在当前的线程中，类似于SecurityContextHolder
        //RequestContextHolder中保存了request对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        //获取被增强方法上的注解对象
        SystemLog systemLog = getSystemLog(jp);

        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL            : {}", request.getRequestURL());
        // 打印描述信息
        log.info("BusinessName   : {}", systemLog.businessName());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}", jp.getSignature().getDeclaringTypeName(),
                                            ((MethodSignature)jp.getSignature()).getName());
        // 打印请求的 IP
        log.info("IP             : {}", request.getRemoteHost());
        // 打印请求入参
        log.info("Request Args   : {}", JSON.toJSONString(jp.getArgs()));
    }

    private SystemLog getSystemLog(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        return signature.getMethod().getAnnotation(SystemLog.class);
    }

    private void handleAfter(Object obj){
        // 打印出参
        log.info("Response       : {}", JSON.toJSONString(obj));
    }
}
