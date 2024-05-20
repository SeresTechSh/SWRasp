package com.wenyao.swrasp.hook;

import javassist.ClassPool;

import java.security.ProtectionDomain;

public interface ClassHook {
    String[] getHookClassNames();

    default boolean canHook(String className) {
        for (String hookClassName : getHookClassNames()) {
            if (className.equals(hookClassName)) {
                return true;
            }
        }
        return false;
    }

    byte[] hook(ClassPool classPool, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer)throws Exception;
}
