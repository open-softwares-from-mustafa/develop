package com.brokerage.tool;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MoneyTool {
    public static boolean isIbanValid (String iban) {
        return iban.trim().length() == 26 && iban.trim().substring(2, iban.length()).matches("[0-9]+");
    }
}
