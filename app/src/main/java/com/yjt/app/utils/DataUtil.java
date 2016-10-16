package com.yjt.app.utils;


import java.util.Random;

public class DataUtil {

    private static DataUtil mDataUtil;
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private DataUtil() {
        // cannot be instantiated
    }

    public static synchronized DataUtil getInstance() {
        if (mDataUtil == null) {
            mDataUtil = new DataUtil();
        }
        return mDataUtil;
    }

    public static void releaseInstance() {
        if (mDataUtil != null) {
            mDataUtil = null;
        }
    }

    public char[] encodeHex(byte[] data) {
        return encodeHex(data, true);
    }

    public char[] encodeHex(byte[] data, boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    public String encodeHexStr(byte[] data) {
        return encodeHexString(data, true);
    }

    public String encodeHexString(byte[] data, boolean toLowerCase) {
        return encodeHexString(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    private String encodeHexString(byte[] data, char[] toDigits) {
        return String.valueOf(encodeHex(data, toDigits));
    }

    private char[] encodeHex(byte[] data, char[] toDigits) {
        int    l   = data.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

    public byte[] decodeHex(char[] data) {
        int len = data.length;
        if ((len & 0x01) != 0) {
            throw new RuntimeException("Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }
        return out;
    }

    private int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    public String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int    v  = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public byte[] hexStringToByte(String hex) {
        int    len    = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] chars  = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(chars[pos]) << 4 | toByte(chars[pos + 1]));
        }
        return result;
    }

    private int byteToInt(byte b, byte c) {
        short s0 = (short) (c & 0xff);
        short s1 = (short) (b & 0xff);
        s1 <<= 8;
        return (short) (s0 | s1);
    }

    private byte[] intToByte(int res) {
        byte[] targets = new byte[2];
        targets[1] = (byte) (res & 0xff);// 最低位
        targets[0] = (byte) ((res >> 8) & 0xff);// 次低位
        return targets;
    }


    private byte toByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public int randInt(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }
}
