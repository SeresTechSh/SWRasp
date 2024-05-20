package com.wenyao.swrasp.hook.plugin;

import com.wenyao.swrasp.hook.ClassHook;
import com.wenyao.swrasp.utils.HookUtils;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;

import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashSet;

public class SQLHook implements ClassHook {
    public static final String[] HOOK_CLASS_NAMES = new String[]{"java.sql.Statement"};
    private HashSet<CtMethod> hookMethods = new HashSet<CtMethod>();

    public SQLHook() {

    }

    @Override
    public String[] getHookClassNames() {
        return HOOK_CLASS_NAMES;
    }

    @Override
    public byte[] hook(ClassPool classPool, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws Exception {
        CtClass ctClass = classPool.get(className);
        HashSet<CtMethod> methods = new HashSet<>();

        for (CtMethod method : ctClass.getMethods()) {
            if (!Modifier.isAbstract(method.getModifiers()) && !Modifier.isNative(method.getModifiers())) {
                if (method.getName().startsWith("execute") && method.getParameterTypes().length >= 1) {
                    if (method.getParameterTypes()[0].getName().equals("java.lang.String")) {
                        if (!hookMethods.contains(method)) {
                            hookMethods.add(method);
                            methods.add(method);
                        }
                    }

                }
            }
        }

        for (CtMethod method : methods) {
            method.insertBefore(HookUtils.getHookCode("checkSQL", new String[]{"java.lang.String"}, "$1"));
        }

        byte[] newClassBuffer = ctClass.toBytecode();
        ctClass.detach();
        return newClassBuffer;
    }
}
