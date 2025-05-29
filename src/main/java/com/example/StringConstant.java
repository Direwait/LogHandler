package com.example;

import java.util.regex.Pattern;

public final class StringConstant {
    public static final String PATH_OF_LOGS = "src\\main\\java\\com\\example\\logs";
    public static final String PATH_OF_USERS_TRANSACTIONS = "src\\main\\java\\com\\example\\logs\\transactions_by_users";
    public static final Pattern LOG_DATE = Pattern.compile("\\[(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})\\]");
    public static final Pattern RECEIVED = Pattern.compile("received (\\d+\\.\\d{2})");
    public static final Pattern BALANCE = Pattern.compile("balance inquiry (\\d+\\.\\d{2})");
    public static final Pattern WITHDREW = Pattern.compile("withdrew (\\d+\\.\\d{2})");
    public static final Pattern TRANSFERRED = Pattern.compile("transferred (\\d+\\.\\d{2})");
    public static final Pattern TRANSFERRED_FULL = Pattern.compile("\\[(.*?)\\] (\\w+) transferred (\\d+\\.\\d{2}) to (\\w+)");

    private StringConstant() {
    }

}
