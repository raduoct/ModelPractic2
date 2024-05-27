package com.example.modelpractic2;

import androidx.annotation.NonNull;

public class BpiInformation {

    private final String code;
    private final String rate;
    private final String description;
    private final String rate_float;

    public BpiInformation(String code, String rate, String description, String rate_float) {
        this.code = code;
        this.rate = rate;
        this.description = description;
        this.rate_float = rate_float;
    }

    public String getCode() {
        return code;
    }

    public String getRate() {
        return rate;
    }

    public String getDescription() {
        return description;
    }

    public String getRate_float() {
        return rate_float;
    }

    @NonNull
    @Override
    public String toString() {
        return "BpiInformation{" +
                "temperature='" + code + '\'' +
                ", windSpeed='" + rate + '\'' +
                ", condition='" + description + '\'' +
                ", pressure='" + rate_float + '\'' +
                '}';
    }

}