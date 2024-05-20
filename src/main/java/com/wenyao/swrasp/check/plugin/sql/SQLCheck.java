package com.wenyao.swrasp.check.plugin.sql;

import com.wenyao.swrasp.context.ContextCheck;
import com.wenyao.swrasp.lexer.LexerException;
import com.wenyao.swrasp.lexer.LexerHelper;
import com.wenyao.swrasp.lexer.Token;
import com.wenyao.swrasp.report.model.HackReport;
import com.wenyao.swrasp.report.types.AttackType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLCheck implements com.wenyao.swrasp.check.SQLCheck {
    private static final Pattern SQL_KEYWORDS_PATTERN = Pattern.compile("\\b(select|or|and|update|insert)\\b|[=()|&]", Pattern.CASE_INSENSITIVE);

    public static boolean containsSQLKeywords(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        Matcher matcher = SQL_KEYWORDS_PATTERN.matcher(input);
        return matcher.find();
    }

    @Override
    public void check(com.wenyao.swrasp.context.RaspContext raspContext, String sql) {
        if (ContextCheck.isInRequestThreadContext()) {
            Set<String> affectedValues = ContextCheck.getAffectedValues();
            Set<String> affectedValues2 = new HashSet<>();
            AtomicInteger affectedCount = new AtomicInteger();
            affectedValues.stream().forEach(affectedValue -> {
                if (affectedValue.equalsIgnoreCase(sql)) {
                    HackReport.reportHackAttack(AttackType.Inject);
                }

                if (containsSQLKeywords(affectedValue)) {
                    if (affectedValue.contains(" ") || affectedValue.contains("\t") || affectedValue.contains("\n") || affectedValue.contains("\r") || affectedValue.contains("/") || affectedValue.contains("\\")) {
                        if (sql.contains(affectedValue)) {
                            affectedCount.getAndIncrement();
                            affectedValues2.add(affectedValue);
                        }
                    }
                }
            });
            AtomicInteger affected2Count = new AtomicInteger();
            try {
                List<Token> tokens = LexerHelper.lexer(sql);
                if (affectedCount.get() > 0) {
                    for (Token token : tokens) {
                        if (token.type == Token.Type.STRING) {
                            affectedValues2.forEach(affectedValue -> {
                                if (token.value.equalsIgnoreCase(affectedValue)) {
                                    affected2Count.getAndIncrement();
                                }
                            });

                        }
                    }
                }
            } catch (LexerException e) {
                HackReport.reportHackAttack(AttackType.Inject);
            }
            if (affected2Count.get() != affectedCount.get()) {
                HackReport.reportHackAttack(AttackType.Inject);
            }

        }
    }
}
