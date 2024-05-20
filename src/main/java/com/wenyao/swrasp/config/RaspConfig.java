package com.wenyao.swrasp.config;

public class RaspConfig {
    public boolean enableCheckSQLInjection = true;
    public boolean enableCheckCommandInjection = true;
    public boolean enableCheckJNDIInjection = true;
    public boolean enableCheckFileUpload = true;
    public boolean enableCheckXssFilter = true;
    public boolean enableCheckAnyFileRead = true;
    public boolean enableBlockWebShell = true;
    public JNDICheckConfig jndiCheckConfig = new JNDICheckConfig();
}
