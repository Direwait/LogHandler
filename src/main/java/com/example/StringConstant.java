package com.example.logs;

public final class StringPatterns {
    private StringPatterns(){ }

    public static final String BALANCE = "balance inquiry %количество%";
    public static final String TRANSFER = "transferred %количество% to %пользователь%";
    public static final String WITHDREW = "withdrew %количество% ";

    //990 получено user001 от user002
    //[2025-05-10 10:03:23] user001 recived 990.00 from user002


    //закинуть в transactions_user информацию о перевод про каждого user
    //после сортировки по дате добавить строку о подсчете денег
    //[2025-05-10 11:00:03] user001 final balance 1196.92
}
