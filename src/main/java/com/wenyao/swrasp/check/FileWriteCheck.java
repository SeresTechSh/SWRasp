package com.wenyao.swrasp.check;

import com.wenyao.swrasp.context.RaspContext;
import com.wenyao.swrasp.report.model.HackReport;

public interface FileWriteCheck {
    void check(RaspContext raspContext, String path);
}
