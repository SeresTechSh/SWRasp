package com.wenyao.swrasp.check.plugin.file;

import com.wenyao.swrasp.context.ContextCheck;
import com.wenyao.swrasp.context.RaspContext;
import com.wenyao.swrasp.report.model.HackReport;
import com.wenyao.swrasp.report.types.AttackType;
import com.wenyao.swrasp.utils.FileUtils;

public class WebShellUploadCheck implements com.wenyao.swrasp.check.FileWriteCheck {
    @Override
    public void check(RaspContext raspContext, String path) {
        if (ContextCheck.isInRequestThreadContext()) {
            if (path.endsWith(".jsp") || path.endsWith(".php") || path.endsWith(".asp") || path.endsWith(".aspx")) {
                HackReport.reportHackAttack(AttackType.RCE);
            }
        }
    }
}
