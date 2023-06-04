package com.ems.utils;

import java.security.SecureRandom;
import java.util.Random;

public class VerCodeGenerateUtil {

    private static final Random RANDOM = new SecureRandom();
    //    生成 6 位数的随机数字
    public static String generateVerCode() {
        long symbols = System.currentTimeMillis();
        String SYMBOLS = String.valueOf(symbols);
        //	如果是六位，就生成大小为 6 的数组
        char[] numbers = new char[6];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(numbers);
    }
}
