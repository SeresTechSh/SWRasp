package com.wenyao.swrasp.check.plugin.header;

import com.wenyao.swrasp.context.RaspContext;
import com.wenyao.swrasp.servlet.Request;
import com.wenyao.swrasp.servlet.Response;

public class PreRequestHandle implements com.wenyao.swrasp.check.RequestCheck {

    @Override
    public void check(RaspContext raspContext, boolean endRequest, Request request, Response response) {
        if (endRequest) {
            return;
        }
        response.setHeader("X-Protected-By", "SWRASP-" + raspContext.getVersion());
        response.setHeader("X-Request-ID", request.getRequestId());
    }
}
