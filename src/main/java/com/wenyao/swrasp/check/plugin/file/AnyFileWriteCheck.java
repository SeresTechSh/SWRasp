package com.wenyao.swrasp.check.plugin.file;

import com.wenyao.swrasp.context.ContextCheck;
import com.wenyao.swrasp.context.RaspContext;
import com.wenyao.swrasp.report.model.HackReport;
import com.wenyao.swrasp.report.types.AttackType;
import com.wenyao.swrasp.utils.FileUtils;

public class AnyFileWriteCheck implements com.wenyao.swrasp.check.FileWriteCheck {
    @Override
    public void check(RaspContext raspContext, String path) {
        if (ContextCheck.isInRequestThreadContext()) {
            if (ContextCheck.isInRequestThreadContext()) {
                if (FileUtils.checkBadFile(path)) {
                    HackReport.reportHackAttack(AttackType.Leak);
                }
                int sl = path.split("/").length;
                if (sl > 2) {
                    for (String affectedValue : ContextCheck.getAffectedValues()) {
                        if (affectedValue.equalsIgnoreCase(path)) {
                            HackReport.reportHackAttack(AttackType.Leak);
                        }
                    }
                }
            }
        }
    }
}
