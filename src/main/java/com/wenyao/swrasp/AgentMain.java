package com.wenyao.swrasp;

import com.wenyao.swrasp.context.RaspContext;
import com.wenyao.swrasp.hook.ClassHook;
import com.wenyao.swrasp.transformer.SafeClassTransformer;
import com.wenyao.swrasp.utils.IOUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;

@Slf4j
public class AgentMain {
    public static void premain(String agentArgs, Instrumentation inst) {
        log.info("SWRasp premain");
        log.info("RaspContext init");
        RaspContext raspContext = RaspContext.getInstance();

        raspContext.setInstrumentation(inst);

        log.info("Register Rasp ClassTransformer");
        SafeClassTransformer safeClassTransformer = new SafeClassTransformer(raspContext);
        inst.addTransformer(safeClassTransformer);

        for (ClassHook classHook : raspContext.getClassHookList()) {
            for (String hookClassName : classHook.getHookClassNames()) {
                try {
                    Class clazz = Class.forName(hookClassName, true, Thread.currentThread().getContextClassLoader());
                    byte[] bytes = IOUtils.readStream(clazz.getResourceAsStream(clazz.getSimpleName() + ".class"));
                    ClassDefinition classDefinition = new ClassDefinition(clazz, bytes);
                    inst.redefineClasses(classDefinition);
                } catch (Throwable ignored) {

                }
            }
        }
    }
}
