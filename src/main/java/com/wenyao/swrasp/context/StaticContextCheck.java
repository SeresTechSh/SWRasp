package com.wenyao.swrasp.context;

import com.wenyao.swrasp.utils.ReflectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.SocketAddress;

public class StaticContextCheck {
    public static void checkRequest(Object request, Object response) {
        RaspContext.getContextCheck().checkRequest(request, response);
    }
    public static void checkEndRequest(Object request, Object response) {
        RaspContext.getContextCheck().checkEndRequest(request, response);
    }

    public static void checkCommand(java.lang.ProcessBuilder processBuilder) {
        RaspContext.getContextCheck().checkCommand(processBuilder);
    }

    public static void checkSQL(String sql) {
        RaspContext.getContextCheck().checkSQL(sql);
    }

    public static void checkSocket(SocketAddress socketAddress) {
        RaspContext.getContextCheck().checkSocket(socketAddress);
    }

    public static void checkReadFile(FileInputStream file) {
        String path = (String) ReflectionUtils.getField(file, "path");
        if (path != null) {
            RaspContext.getContextCheck().checkReadFile(path);
        }
    }
    public static void checkWriteFile(FileOutputStream file) {
        String path = (String) ReflectionUtils.getField(file, "path");
        if (path != null) {
            RaspContext.getContextCheck().checkWriteFile(path);
        }
    }
}
