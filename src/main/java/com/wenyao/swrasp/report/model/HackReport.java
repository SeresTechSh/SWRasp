package com.wenyao.swrasp.report.model;

import com.wenyao.swrasp.context.RequestContext;
import com.wenyao.swrasp.report.types.AttackType;
import com.wenyao.swrasp.servlet.Request;
import com.wenyao.swrasp.servlet.Response;
import com.wenyao.swrasp.utils.IOUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;


@Slf4j
public class HackReport {
    public String requestId;
    public String userAgent;
    public String remoteAddr;
    public String requestUri;
    public String requestMethod;
    public String checkPluginName;
    public AttackType attackType;

    private HackReport() {

    }


    public String render() {
        try {
            String template = new String(IOUtils.readStream(HackReport.class.getResourceAsStream("/block.html")));
            template = template.replace("{{requestId}}", requestId);
            template = template.replace("{{request_path}}", requestUri);
            template = template.replace("{{requestIp}}", remoteAddr);
            return template;
        } catch (IOException e) {

        }
        return "block";
    }

    public static void reportHackAttack(AttackType attackType) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        HackReport hackReport = new HackReport();
        Request request = RequestContext.getRequest();
        Response response = RequestContext.getResponse();
        hackReport.requestId = request.getRequestId();
        hackReport.userAgent = request.getHeader("User-Agent");
        hackReport.remoteAddr = request.getRemoteAddr();
        hackReport.requestUri = request.getRequestURI();
        hackReport.requestMethod = request.getMethod();
        hackReport.checkPluginName = stackTraceElements[2].getClassName();
        hackReport.attackType = attackType;


        RequestContext.clear();

        log.warn("Hack Attack Detected: " + hackReport);
        response.setStatus(403);
        response.setContentType("text/html;charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.println(hackReport.render());
        printWriter.flush();
        printWriter.close();
        throw new Error("Hack Attack Detected");
    }

    @Override
    public String toString() {
        return "HackReport{" +
                "requestId='" + requestId + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", remoteAddr='" + remoteAddr + '\'' +
                ", requestUri='" + requestUri + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", checkPluginName='" + checkPluginName + '\'' +
                ", attackType=" + attackType +
                '}';
    }
}
