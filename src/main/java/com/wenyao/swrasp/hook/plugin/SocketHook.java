package com.wenyao.swrasp.hook.plugin;

import com.wenyao.swrasp.hook.ClassHook;
import com.wenyao.swrasp.utils.HookUtils;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.security.ProtectionDomain;

public class SocketHook implements ClassHook {
    private static final String[] HOOK_CLASS_NAMES = new String[]{"java.net.Socket"};

    public SocketHook() {
    }

    @Override
    public String[] getHookClassNames() {
        return HOOK_CLASS_NAMES;
    }

    @Override
    public byte[] hook(ClassPool classPool, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws Exception {
        CtClass ctClass = classPool.get(className);
        CtMethod ctMethod = ctClass.getMethod("connect", "(Ljava/net/SocketAddress;I)V");
        ctMethod.insertBefore(HookUtils.getHookCode("checkSocket", new String[]{"java.net.SocketAddress"}, "$1"));
        byte[] newClassBuffer = ctClass.toBytecode();
        ctClass.detach();
        return newClassBuffer;
    }
}
