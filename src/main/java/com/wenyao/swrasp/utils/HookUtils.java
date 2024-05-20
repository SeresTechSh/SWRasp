package com.wenyao.swrasp.utils;

public class HookUtils {
    public static String getHookCode(String checkName, String[] argsType, String... args) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Class.forName(\"com.wenyao.swrasp.context.StaticContextCheck\",true,ClassLoader.getSystemClassLoader()).getMethod(\"").append(checkName).append("\",new Class[]{");
        for (String argType : argsType) {
            stringBuilder.append(argType).append(".class,");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("}).invoke(null,new Object[]{");
        for (String arg : args) {
            stringBuilder.append(arg).append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("});");
        return stringBuilder.toString();
    }
}
