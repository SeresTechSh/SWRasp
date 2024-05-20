package com.wenyao.swrasp.servlet;

import com.wenyao.swrasp.utils.ReflectionUtils;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.UUID;

public class Request {
    private Object request;
    @Getter
    private String requestId;

    public Request(Object request) {
        this.request = request;
        this.requestId = UUID.randomUUID().toString();
    }

    public String getHeader(String name) {
        return (String) ReflectionUtils.invokeMethod(request, ReflectionUtils.getCallerName(), name);
    }

    public Enumeration<String> getHeaders(String name) {
        return (Enumeration<String>) ReflectionUtils.invokeMethod(request, ReflectionUtils.getCallerName(), name);
    }

    public Enumeration<String> getHeaderNames() {
        return (Enumeration<String>) ReflectionUtils.invokeMethod(request, ReflectionUtils.getCallerName());
    }

    public String getMethod() {
        return (String) ReflectionUtils.invokeMethod(request, ReflectionUtils.getCallerName());
    }

    public String getParameter(String name) {
        return (String) ReflectionUtils.invokeMethod(request, ReflectionUtils.getCallerName(), name);
    }

    public String[] getParameterValues(String name) {
        return (String[]) ReflectionUtils.invokeMethod(request, ReflectionUtils.getCallerName(), name);
    }

    public String getQueryString() {
        return (String) ReflectionUtils.invokeMethod(request, ReflectionUtils.getCallerName());
    }

    public String getRequestURI() {
        return (String) ReflectionUtils.invokeMethod(request, ReflectionUtils.getCallerName());
    }

    public String getRemoteAddr() {
        return (String) ReflectionUtils.invokeMethod(request, ReflectionUtils.getCallerName());
    }

    public String getRemoteHost() {
        return (String) ReflectionUtils.invokeMethod(request, ReflectionUtils.getCallerName());
    }

    public int getRemotePort() {
        return (int) ReflectionUtils.invokeMethod(request, ReflectionUtils.getCallerName());
    }

    public String getLocalAddr() {
        return (String) ReflectionUtils.invokeMethod(request, ReflectionUtils.getCallerName());
    }

    public String getLocalName() {
        return (String) ReflectionUtils.invokeMethod(request, ReflectionUtils.getCallerName());
    }

    public int getLocalPort() {
        return (int) ReflectionUtils.invokeMethod(request, ReflectionUtils.getCallerName());
    }


    public int getContentLength() {
        return (int) ReflectionUtils.invokeMethod(request, ReflectionUtils.getCallerName());
    }

    public long getContentLengthLong() {
        return (long) ReflectionUtils.invokeMethod(request, ReflectionUtils.getCallerName());
    }

    public String getContentType() {
        return (String) ReflectionUtils.invokeMethod(request, ReflectionUtils.getCallerName());
    }

    public InputStream getInputStream() throws IOException {
        return (InputStream) ReflectionUtils.invokeMethod(request, ReflectionUtils.getCallerName());
    }


    public Enumeration<String> getParameterNames() {
        return (Enumeration<String>) ReflectionUtils.invokeMethod(request, ReflectionUtils.getCallerName());
    }

    public Map<String, String[]> getParameterMap() {
        return (Map<String, String[]>) ReflectionUtils.invokeMethod(request, ReflectionUtils.getCallerName());
    }

}
