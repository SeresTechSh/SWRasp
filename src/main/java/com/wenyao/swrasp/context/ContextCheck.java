package com.wenyao.swrasp.context;

import com.wenyao.swrasp.check.*;
import com.wenyao.swrasp.context.plugin.PluginUtils;
import com.wenyao.swrasp.servlet.Request;
import com.wenyao.swrasp.servlet.Response;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class ContextCheck {
    private RaspContext raspContext;
    @Getter
    private final ArrayList<CommandCheck> commandCheckList = new ArrayList<>();
    @Getter
    private final ArrayList<FileReadCheck> fileReadCheckList = new ArrayList<>();
    @Getter
    private final ArrayList<FileWriteCheck> fileWriteCheckList = new ArrayList<>();
    @Getter
    private final ArrayList<RequestCheck> requestCheckList = new ArrayList<>();
    @Getter
    private final ArrayList<SocketCheck> socketCheckList = new ArrayList<>();
    @Getter
    private final ArrayList<SQLCheck> sqlCheckList = new ArrayList<>();

    public ContextCheck(RaspContext raspContext) {
        super();
        this.raspContext = raspContext;
        this.loadCheck();
    }

    private void loadCheck() {
        PluginUtils.getPlugins("com.wenyao.swrasp.check", CommandCheck.class).forEach(plugin -> {
            try {
                CommandCheck commandCheck = plugin.newInstance();
                this.commandCheckList.add(commandCheck);
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("Failed to load plugin: " + plugin.getName(), e);
            }
        });

        PluginUtils.getPlugins("com.wenyao.swrasp.check", FileReadCheck.class).forEach(plugin -> {
            try {
                FileReadCheck fileReadCheck = plugin.newInstance();
                this.fileReadCheckList.add(fileReadCheck);
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("Failed to load plugin: " + plugin.getName(), e);
            }
        });

        PluginUtils.getPlugins("com.wenyao.swrasp.check", FileWriteCheck.class).forEach(plugin -> {
            try {
                FileWriteCheck fileWriteCheck = plugin.newInstance();
                this.fileWriteCheckList.add(fileWriteCheck);
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("Failed to load plugin: " + plugin.getName(), e);
            }
        });

        PluginUtils.getPlugins("com.wenyao.swrasp.check", RequestCheck.class).forEach(plugin -> {
            try {
                RequestCheck requestCheck = plugin.newInstance();
                this.requestCheckList.add(requestCheck);
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("Failed to load plugin: " + plugin.getName(), e);
            }
        });

        PluginUtils.getPlugins("com.wenyao.swrasp.check", SocketCheck.class).forEach(plugin -> {
            try {
                SocketCheck socketCheck = plugin.newInstance();
                this.socketCheckList.add(socketCheck);
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("Failed to load plugin: " + plugin.getName(), e);
            }
        });

        PluginUtils.getPlugins("com.wenyao.swrasp.check", SQLCheck.class).forEach(plugin -> {
            try {
                SQLCheck sqlCheck = plugin.newInstance();
                this.sqlCheckList.add(sqlCheck);
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("Failed to load plugin: " + plugin.getName(), e);
            }
        });
        log.info("Loaded " + commandCheckList.size() + " command checks");
        log.info("Loaded " + fileReadCheckList.size() + " file read checks");
        log.info("Loaded " + fileWriteCheckList.size() + " file write checks");
        log.info("Loaded " + requestCheckList.size() + " request checks");
        log.info("Loaded " + socketCheckList.size() + " socket checks");
        log.info("Loaded " + sqlCheckList.size() + " sql checks");
    }

    public boolean checkInCallStack(String className, String methodName) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            if (className != null && methodName != null) {
                if (stackTraceElement.getClassName().equals(className)) {
                    return true;
                }
            } else if (className != null) {
                if (stackTraceElement.getClassName().equals(className)) {
                    return true;
                }
            } else if (methodName != null) {
                if (stackTraceElement.getMethodName().equals(methodName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isInRequestThreadContext() {
        return RequestContext.getRequest() != null;
    }

    public static Set<String> getAffectedValues() {
        HashSet<String> affectedValues = new HashSet<String>();
        Request request = RequestContext.getRequest();
        if (request != null) {
            request.getParameterMap().forEach((key, value) -> {
                affectedValues.add(key);
                affectedValues.addAll(new CopyOnWriteArrayList<>(value));
            });

            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                affectedValues.add(headerName);
                Enumeration<String> headerValues = request.getHeaders(headerName);
                while (headerValues.hasMoreElements()) {
                    affectedValues.add(headerValues.nextElement());
                }
            }
            affectedValues.add(request.getRemoteHost());
            affectedValues.add(request.getRemoteAddr());
        }
        return affectedValues;
    }


    public void checkEndRequest(Object requestObj, Object responseObj) {
        Request request = new Request(requestObj);
        Response response = new Response(responseObj);
        requestCheckList.forEach(check -> check.check(raspContext, true, request, response));
        RequestContext.clear();
    }

    public void checkRequest(Object requestObj, Object responseObj) {
        Request request = new Request(requestObj);
        Response response = new Response(responseObj);
        RequestContext.setRequestThreadLocal(request, response);
        requestCheckList.forEach(check -> check.check(raspContext, false, request, response));
    }

    public void checkCommand(java.lang.ProcessBuilder processBuilder) {
        commandCheckList.forEach(check -> check.check(raspContext, processBuilder.command()));
    }

    public void checkSQL(String sql) {
        sqlCheckList.forEach(check -> check.check(raspContext, sql));
    }

    public void checkSocket(SocketAddress socketAddress) {
        socketCheckList.forEach(check -> check.check(raspContext, socketAddress));
    }

    public void checkReadFile(String fileName) {
        fileReadCheckList.forEach(check -> check.check(raspContext, fileName));
    }

    public void checkWriteFile(String fileName) {
        fileWriteCheckList.forEach(check -> check.check(raspContext, fileName));
    }

}
