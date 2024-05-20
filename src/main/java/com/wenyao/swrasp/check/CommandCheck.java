package com.wenyao.swrasp.check;

import com.wenyao.swrasp.context.RaspContext;
import com.wenyao.swrasp.report.model.HackReport;

import java.util.List;

public interface CommandCheck {
    void check(RaspContext raspContext, List<String> commandList);
}
