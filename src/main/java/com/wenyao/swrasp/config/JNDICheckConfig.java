package com.wenyao.swrasp.config;

import java.util.ArrayList;

public class JNDICheckConfig {
    public boolean enableCheckJNDIInjection = true;
    public boolean enableWhiteList = false;
    public ArrayList<String> whitelist = new ArrayList<String>();
    public boolean enableBlockExtranet = true;

    public JNDICheckConfig() {
        whitelist.add("127.0.0.1");
        whitelist.add("localhost");
    }
}
