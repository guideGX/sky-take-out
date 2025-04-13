package com.sky.aspect;

import com.sky.annotation.AutoFill;

import com.sky.constant.AutoFillConstant;

import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 统一拦截加了AutoFill注解的方法
 * 自定义切面，通过反射为公共字段赋值
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {

    }

    /**
     * 前置通知，在通知中为公共字段的参数赋值
     */
    @Before("autoFillPointCut()")
    //通过连接点，JoinPoint获取拦截参数的类型
    public void atuoFill(JoinPoint joinPoint) {
        log.info("开始公共字段自动填充.....");
        //获取到当前被拦截的方法上的数据库操作类型

        //getSignature获得签名
        //这里在aop的场景下，用的aop中的对象，与反射相比更加高效
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        //获得方法上的注解对象，这里aop和反射混用
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();
        //获取到当前被拦截方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            return;
        }
        Object entity= args[0];
        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        //根据当前不同的操作类型，为对应的属性通过反射来赋值
        if(operationType == OperationType.INSERT) {
            try {
                //反射
                Method setCreateUser = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setCreateTime = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setCreateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if(operationType == OperationType.UPDATE) {
            try {
                Method setUpdateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
