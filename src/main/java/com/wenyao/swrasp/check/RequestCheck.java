package com.wenyao.swrasp.check;

import com.wenyao.swrasp.context.RaspContext;
import com.wenyao.swrasp.servlet.Request;
import com.wenyao.swrasp.servlet.Response;

public interface RequestCheck {
    void check(RaspContext raspContext, boolean endRequest, Request request, Response response);
}
