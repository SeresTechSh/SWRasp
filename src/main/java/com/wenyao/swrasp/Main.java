package com.wenyao.swrasp;

import com.wenyao.swrasp.context.RaspContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) {
        RaspContext raspContext = RaspContext.getInstance();
        log.info("Rasp Version: " + raspContext.version);
        System.out.println("java -javaagent:swrasp.jar -jar target/webapp.jar");
    }
}