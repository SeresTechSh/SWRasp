package com.wenyao.swrasp.check.plugin.xss;

import com.wenyao.swrasp.context.ContextCheck;
import com.wenyao.swrasp.context.RaspContext;
import com.wenyao.swrasp.context.RequestContext;
import com.wenyao.swrasp.report.model.HackReport;
import com.wenyao.swrasp.report.types.AttackType;
import com.wenyao.swrasp.servlet.Request;
import com.wenyao.swrasp.servlet.Response;

import java.util.regex.Pattern;

public class XssCheck implements com.wenyao.swrasp.check.RequestCheck {
    private static final Pattern[] patterns = new Pattern[]{
            Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("src[\r\n]*=[\r\n]*\\'(.*?)\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
    };

    public static boolean checkXss(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        for (Pattern pattern : patterns) {
            if (pattern.matcher(input).find()) {
                return true;
            }
        }

        return false;
    }


    @Override
    public void check(RaspContext raspContext, boolean endRequest, Request request, Response response) {
        if (endRequest) {
            return;
        }
        ContextCheck.getAffectedValues().forEach(value -> {
            if (checkXss(value)) {
                HackReport.reportHackAttack(AttackType.XSS);
            }
        });
    }
}
