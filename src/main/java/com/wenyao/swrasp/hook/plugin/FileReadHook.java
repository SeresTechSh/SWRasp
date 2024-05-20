package com.wenyao.swrasp.hook.plugin;

import com.wenyao.swrasp.hook.ClassHook;
import com.wenyao.swrasp.utils.HookUtils;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;

import java.security.ProtectionDomain;

public class FileReadHook implements ClassHook {
    private static final String[] HOOK_CLASS_NAMES = new String[]{"java.io.FileInputStream"};

    @Override
    public String[] getHookClassNames() {
        return HOOK_CLASS_NAMES;
    }

    @Override
    public byte[] hook(ClassPool classPool, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws Exception {
        CtClass ctClass = classPool.get(className);
        CtConstructor[] ctConstructors = ctClass.getConstructors();
        for (CtConstructor ctConstructor : ctConstructors) {
            ctConstructor.insertAfter(HookUtils.getHookCode("checkReadFile", new String[]{"java.io.FileInputStream"}, "$0"));
        }
        byte[] newClassBuffer = ctClass.toBytecode();
        ctClass.detach();
        return newClassBuffer;
    }
}