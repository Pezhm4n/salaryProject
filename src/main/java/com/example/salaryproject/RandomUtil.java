package com.example.salaryproject;

import java.util.Random;

import java.security.SecureRandom;

public class RandomUtil {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static SecureRandom getInstance() {
        return SECURE_RANDOM;
    }
}