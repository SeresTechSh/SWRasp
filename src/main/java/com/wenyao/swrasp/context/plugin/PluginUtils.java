package com.wenyao.swrasp.context.plugin;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.Set;

public class PluginUtils {

    public static <T> Set<Class<? extends T>> getPlugins(String packageName, Class<T> interfaceClass) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(Scanners.SubTypes)
                .filterInputsBy(new FilterBuilder().includePackage(packageName)));

        return reflections.getSubTypesOf(interfaceClass);
    }
}
