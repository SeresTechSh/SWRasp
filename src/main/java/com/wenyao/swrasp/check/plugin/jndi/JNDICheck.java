package com.wenyao.swrasp.check.plugin.jndi;

import com.wenyao.swrasp.check.SocketCheck;
import com.wenyao.swrasp.config.RaspConfig;
import com.wenyao.swrasp.context.RaspContext;
import com.wenyao.swrasp.report.model.HackReport;
import com.wenyao.swrasp.report.types.AttackType;
import com.wenyao.swrasp.utils.NetUtils;

import javax.naming.InitialContext;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class JNDICheck implements SocketCheck {
    @Override
    public void check(RaspContext raspContext, SocketAddress socketAddress) {
        InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;


        RaspConfig raspConfig = raspContext.getRaspConfig();
        if (raspConfig.enableCheckJNDIInjection) {
            if (RaspContext.getContextCheck().checkInCallStack("com.sun.jndi.ldap.Connection", null) || RaspContext.getContextCheck().checkInCallStack("sun.rmi.transport.tcp.TCPChannel", null)
                    || RaspContext.getContextCheck().checkInCallStack("javax.naming.InitialContext", null)) {
                if (raspConfig.jndiCheckConfig.enableWhiteList) {
                    if (!raspConfig.jndiCheckConfig.whitelist.contains(inetSocketAddress.getAddress().getHostAddress())) {
                        HackReport.reportHackAttack(AttackType.RCE);
                    }
                }
                if (raspConfig.jndiCheckConfig.enableBlockExtranet) {
                    if (NetUtils.isPublicAddress(inetSocketAddress)) {
                        HackReport.reportHackAttack(AttackType.RCE);
                    }
                }
            }
        }

    }
}
