package com.wenyao.swrasp.check;

import com.wenyao.swrasp.context.RaspContext;
import com.wenyao.swrasp.report.model.HackReport;

import java.net.SocketAddress;

public interface SocketCheck {
    void check(RaspContext raspContext, SocketAddress socketAddress);
}
