package com.wenyao.swrasp.utils;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class NetUtils {
    public static boolean isPublicAddress(InetSocketAddress inetSocketAddress) {
        InetAddress address = inetSocketAddress.getAddress();

        if (address == null) {
            return false;
        }

        if (address.isLoopbackAddress()) {
            return false;
        }

        if (address.isLinkLocalAddress()) {
            return false;
        }

        if (address.isSiteLocalAddress()) {
            return false;
        }

        return true;
    }
}
