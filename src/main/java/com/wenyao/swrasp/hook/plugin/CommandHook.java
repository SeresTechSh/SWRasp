package com.wenyao.swrasp.hook.plugin;

import com.wenyao.swrasp.hook.ClassHook;
import com.wenyao.swrasp.utils.HookUtils;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.security.ProtectionDomain;

public class CommandHook implements ClassHook {
    private static final String[] HOOK_CLASS_NAMES = new String[]{"java.lang.ProcessBuilder"};

    @Override
    public String[] getHookClassNames() {
        return HOOK_CLASS_NAMES;
    }


    @Override
    public byte[] hook(ClassPool classPool, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws Exception {
        CtClass ctClass = classPool.get(className);
        CtMethod startMethod = ctClass.getMethod("start", "()Ljava/lang/Process;");

        startMethod.insertBefore(HookUtils.getHookCode("checkCommand", new String[]{"java.lang.ProcessBuilder"}, "$0"));
        byte[] newClassBuffer = ctClass.toBytecode();
        ctClass.detach();
        return newClassBuffer;
    }
}
