package com.wenyao.swrasp.utils;

public class FileUtils {
    public static boolean checkBadFile(String path) {
        if (path.contains("/etc/passwd")) {
            return true;
        }
        if (path.contains("../../../")) {
            return true;
        }
        if (path.contains("..\\..\\..\\")) {
            return true;
        }
        return false;
    }
}
