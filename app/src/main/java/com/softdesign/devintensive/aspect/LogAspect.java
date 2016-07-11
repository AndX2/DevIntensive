package com.softdesign.devintensive.aspect;

import android.util.Log;

import com.softdesign.devintensive.utils.ConstantManager;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by savos on 03.07.2016.
 */
@Aspect
public class LogAspect {
    public final String LOG_TAG = "AspectLogTag";

    @Pointcut("execution(void com.softdesign.devintensive.ui.activities.*Activity*.*(..))")
    public void onActivitiesPublicMethod() {
    }

    @Before("onActivitiesPublicMethod()")
    public void doBeforeOnPublicMethod(JoinPoint joinPoint) {
        StringBuilder builder = new StringBuilder();
        for (Object arg:joinPoint.getArgs()) {
            if (arg != null) builder.append(arg.toString());
        }
        Log.d(LOG_TAG, joinPoint.getSignature().getName() + " args: " + builder.toString());
    }
}
