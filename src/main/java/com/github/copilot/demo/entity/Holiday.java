package com.github.copilot.demo.entity;

import org.springframework.format.annotation.DateTimeFormat;

public class Holiday {
    private String countryCode;
    private String countryDesc;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String holidayDate;
    private String holidayName;

    // 构造方法
    public Holiday(String countryCode, String countryDesc, String holidayDate, String holidayName) {
        this.countryCode = countryCode;
        this.countryDesc = countryDesc;
        this.holidayDate = holidayDate;
        this.holidayName = holidayName;
    }

    // getter和setter方法
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryDesc() {
        return countryDesc;
    }

    public void setCountryDesc(String countryDesc) {
        this.countryDesc = countryDesc;
    }

    public String getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(String holidayDate) {
        this.holidayDate = holidayDate;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public String getKey() {
        return countryCode + "-" + holidayDate;
    }

    @Override
    public String toString() {
        return "Holiday{" +
                "countryCode='" + countryCode + '\'' +
                ", countryDesc='" + countryDesc + '\'' +
                ", holidayDate='" + holidayDate + '\'' +
                ", holidayName='" + holidayName + '\'' +
                '}';
    }
}

