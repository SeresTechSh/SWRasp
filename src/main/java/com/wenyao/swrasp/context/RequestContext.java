package com.wenyao.swrasp.context;

import com.wenyao.swrasp.servlet.Request;
import com.wenyao.swrasp.servlet.Response;

public class RequestContext {
    private RaspContext raspContext;
    private static ThreadLocal<Request> requestThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<Response> responseThreadLocal = new ThreadLocal<>();


    public static void setRequestThreadLocal(Request requestThreadLocal, Response response) {
        RequestContext.requestThreadLocal.set(requestThreadLocal);
        responseThreadLocal.set(response);
    }

    public static Request getRequest() {
        return requestThreadLocal.get();
    }

    public static Response getResponse() {
        return responseThreadLocal.get();
    }

    public static void clear() {
        requestThreadLocal.remove();
        responseThreadLocal.remove();
    }
}
