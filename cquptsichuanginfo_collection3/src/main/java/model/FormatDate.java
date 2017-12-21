package model;

import java.util.Date;

public class FormatDate {
    String info;

    public FormatDate(String info) {
        this.info = info;
    }

    public Date getDate() {
        int year = Integer.valueOf(info) / 10000;
        int month = Integer.valueOf(info) / 100 % 100;
        int date = Integer.valueOf(info) % 100;
        Date Date = new Date(year, month, date);
        return Date;
    }
}

