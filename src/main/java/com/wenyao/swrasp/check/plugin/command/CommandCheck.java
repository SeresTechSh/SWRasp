package com.wenyao.swrasp.check.plugin.command;

import com.wenyao.swrasp.context.ContextCheck;
import com.wenyao.swrasp.context.RaspContext;
import com.wenyao.swrasp.lexer.LexerException;
import com.wenyao.swrasp.lexer.LexerHelper;
import com.wenyao.swrasp.lexer.Token;
import com.wenyao.swrasp.report.model.HackReport;
import com.wenyao.swrasp.report.types.AttackType;

import java.util.List;
import java.util.Set;

public class CommandCheck implements com.wenyao.swrasp.check.CommandCheck {
    @Override
    public void check(RaspContext raspContext, List<String> commandList) {
        Set<String> affectedValues = ContextCheck.getAffectedValues();
        String command = String.join(" ", commandList);

        if (affectedValues.contains(command)) {
            HackReport.reportHackAttack(AttackType.RCE);
        }

        try {
            List<Token> tokens = LexerHelper.lexer(command);
            if (tokens.size() > 3) {
                String interactShell = tokens.get(0).value;
                if (interactShell.equals("bash") || interactShell.equals("bsh") || interactShell.equals("sh") || interactShell.equals("cmd") || interactShell.equals("powershell")) {
                    String bashLine = tokens.get(2).value;
                    try {
                        List<Token> bashTokens = LexerHelper.lexer(bashLine);
                        for (Token token : bashTokens) {
                            if ((affectedValues.contains("|") || affectedValues.contains("&") || affectedValues.contains(";")) && affectedValues.contains(token.value)) {
                                HackReport.reportHackAttack(AttackType.RCE);
                            }
                        }
                    } catch (LexerException ignored) {

                    }
                }
            }


        } catch (LexerException e) {
            HackReport.reportHackAttack(AttackType.RCE);
        } catch (Exception ignored) {

        }


    }

}
