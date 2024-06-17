package com.hutech.lab02;
import lombok. AllArgsConstructor;

@AllArgsConstructor public enum Provider {
    LOCAL("Local"),
    GOOGLE("Google");
    public final String value;
}