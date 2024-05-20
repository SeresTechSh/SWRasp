package com.wenyao.swrasp.servlet;

import com.wenyao.swrasp.utils.ReflectionUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;

public class Response {
    private Object response;

    public Response(Object response) {
        this.response = response;
    }

    public String getHeader(String name) {
        return (String) ReflectionUtils.invokeMethod(response, ReflectionUtils.getCallerName(), name);
    }

    public Collection<String> getHeaderNames() {
        return (Collection<String>) ReflectionUtils.invokeMethod(response, ReflectionUtils.getCallerName());
    }

    public int getStatus() {
        return (int) ReflectionUtils.invokeMethod(response, ReflectionUtils.getCallerName());
    }

    public String getCharacterEncoding() {
        return (String) ReflectionUtils.invokeMethod(response, ReflectionUtils.getCallerName());
    }

    public OutputStream getOutputStream() {
        return (OutputStream) ReflectionUtils.invokeMethod(response, ReflectionUtils.getCallerName());
    }

    public PrintWriter getWriter() {
        return (PrintWriter) ReflectionUtils.invokeMethod(response, ReflectionUtils.getCallerName());
    }

    public void flushBuffer() throws IOException {
        ReflectionUtils.invokeMethod(response, ReflectionUtils.getCallerName());
    }

    public void resetBuffer() {
        ReflectionUtils.invokeMethod(response, ReflectionUtils.getCallerName());
    }

    public void reset() {
        ReflectionUtils.invokeMethod(response, ReflectionUtils.getCallerName());
    }

    public void setCharacterEncoding(String charset) {
        ReflectionUtils.invokeMethod(response, ReflectionUtils.getCallerName(), charset);
    }

    public void setContentLength(int len) {
        ReflectionUtils.invokeMethod(response, ReflectionUtils.getCallerName(), len);
    }

    public void setContentType(String type) {
        ReflectionUtils.invokeMethod(response, ReflectionUtils.getCallerName(), type);
    }

    public void setHeader(String name, String value) {
        ReflectionUtils.invokeMethod(response, ReflectionUtils.getCallerName(), name, value);
    }

    public void setStatus(int sc) {
        ReflectionUtils.invokeMethod(response, ReflectionUtils.getCallerName(), sc);
    }

    public void sendError(int sc) throws IOException {
        ReflectionUtils.invokeMethod(response, ReflectionUtils.getCallerName(), sc);
    }

}
