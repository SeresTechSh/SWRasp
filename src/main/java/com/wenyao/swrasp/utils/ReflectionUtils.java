package com.wenyao.swrasp.utils;

import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


@Slf4j
public class ReflectionUtils {
    private static Unsafe unsafe;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static Method getMethod(Class clazz, String methodName, Class<?>... parameterTypes) {
        while (clazz != null) {
            try {
                Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

    public static Method getMethod(Object obj, String methodName, Class<?>... parameterTypes) {
        return getMethod(obj.getClass(), methodName, parameterTypes);
    }

    public static Object invokeMethod(Object obj, String methodName, Class<?>[] parameterTypes, Object... args) {
        try {
            Method method = getMethod(obj, methodName, parameterTypes);
            return method.invoke(obj, args);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static Object invokeMethod(Object obj, String methodName, Object... args) {
        Class[] parameterTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        try {
            Method method = getMethod(obj, methodName, parameterTypes);
            return method.invoke(obj, args);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static Object invokeMethod(Object obj, String methodName) {
        try {
            Method method = getMethod(obj, methodName);
            return method.invoke(obj);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static Object getField(Object obj, String fieldName) {
        try {
            Class clazz = obj.getClass();
            while (clazz != null) {
                try {
                    java.lang.reflect.Field field = clazz.getDeclaredField(fieldName);
                    if (unsafe != null) {
                        return unsafe.getObject(obj, unsafe.objectFieldOffset(field));
                    }
                    field.setAccessible(true);
                    return field.get(obj);
                } catch (NoSuchFieldException e) {
                    clazz = clazz.getSuperclass();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static String getCallerName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return stackTrace[2].getMethodName();
    }
}
