package com.wenyao.swrasp.hook.plugin;

import com.wenyao.swrasp.hook.ClassHook;
import com.wenyao.swrasp.utils.HookUtils;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.security.ProtectionDomain;

public class RequestHook implements ClassHook {
    private static final String[] HOOK_CLASS_NAMES = new String[]{"jakarta.servlet.http.HttpServlet", "javax.servlet.http.HttpServlet"};

    public RequestHook() {

    }

    @Override
    public String[] getHookClassNames() {
        return HOOK_CLASS_NAMES;
    }

    @Override
    public byte[] hook(ClassPool classPool, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws Exception {
        CtClass ctClass = classPool.get(className);
        boolean isNewApi = className.startsWith("jakarta.");
        String desc = "(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;)V";
        if (isNewApi) {
            desc = desc.replace("javax", "jakarta");
        }

        CtMethod ctMethod = ctClass.getMethod("service", desc);

        ctMethod.insertBefore(HookUtils.getHookCode("checkRequest", new String[]{"java.lang.Object", "java.lang.Object"}, "$1", "$2"));
        ctMethod.insertAfter(HookUtils.getHookCode("checkEndRequest", new String[]{"java.lang.Object", "java.lang.Object"}, "$1", "$2"));

        byte[] newClassBuffer = ctClass.toBytecode();
        ctClass.detach();
        return newClassBuffer;
    }
}
