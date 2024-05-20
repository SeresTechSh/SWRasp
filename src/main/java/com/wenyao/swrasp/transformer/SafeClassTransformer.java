package com.wenyao.swrasp.transformer;

import com.wenyao.swrasp.context.RaspContext;
import com.wenyao.swrasp.hook.ClassHook;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.lang.instrument.*;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class SafeClassTransformer implements ClassFileTransformer {
    private RaspContext raspContext;
    private ArrayList<ClassHook> classHooks;
    private ClassPool classPool;
    private HashSet<ClassLoader> classLoaderClassPaths = new HashSet<>();
    private HashMap<String, byte[]> hookClassCacheMap = new HashMap<>();


    public SafeClassTransformer(RaspContext raspContext) {
        super();
        this.raspContext = raspContext;
        this.classHooks = this.raspContext.getClassHookList();
        this.classPool = new ClassPool(true);
        this.classPool.appendSystemPath();
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        className = className.replace("/", ".");

        if (hookClassCacheMap.containsKey(className)) {
            return hookClassCacheMap.get(className);
        }

        if (!classLoaderClassPaths.contains(loader)) {
            classPool.appendClassPath(new LoaderClassPath(loader));
            classLoaderClassPaths.add(loader);
        }

        HashSet<String> superClassNames = new HashSet<>();
        superClassNames.add(className);

        try {
            CtClass ctClass = classPool.getCtClass(className);
            CtClass tmpClass = ctClass;
            while (tmpClass != null) {
                superClassNames.add(tmpClass.getName());

                CtClass[] interfaces = tmpClass.getInterfaces();
                while (interfaces != null && interfaces.length != 0) {
                    ArrayList<CtClass> nextInterfaces = new ArrayList<>();
                    for (CtClass anInterface : interfaces) {
                        if (!superClassNames.contains(anInterface.getName())) {
                            superClassNames.add(anInterface.getName());
                            nextInterfaces.addAll(new CopyOnWriteArrayList<>(anInterface.getInterfaces()));
                        }
                    }
                    interfaces = nextInterfaces.toArray(new CtClass[nextInterfaces.size()]);
                }

                tmpClass = tmpClass.getSuperclass();
            }
        } catch (NotFoundException e) {
//            log.error(e.getMessage());
        }


        for (String curClassName : superClassNames) {
            for (ClassHook classHook : classHooks) {
                for (String hookClassName : classHook.getHookClassNames()) {
                    if (curClassName.equals(hookClassName)) {
                        try {
                            byte[] newClassBytes = classHook.hook(classPool, loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
                            if (newClassBytes != null && newClassBytes.length > 0) {
                                hookClassCacheMap.put(className, newClassBytes);
                                return newClassBytes;
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    }
                }
            }
        }
        return classfileBuffer;
    }
}
