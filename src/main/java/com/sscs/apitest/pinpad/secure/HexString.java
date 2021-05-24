package com.sscs.apitest.pinpad.secure;

public class HexString {

    
    public static String padRight(String s, int len, char c){
        StringBuilder d = new StringBuilder(s);
        while (d.length() < len)
            d.append(c);
        return d.toString();        
    }
    
    public static String stringToHex(String s) {
        byte[] stringBytes = s.getBytes();

        return HexString.byteToHex(stringBytes);
    }

    public static String byteToHex(byte buffer[]) {
        return HexString.byteToHex(buffer, 0, buffer.length);
    }

    public static String byteToHex(byte buffer) {
        byte[] b = new byte[1];
        b[0] = buffer;
        return HexString.byteToHex(b, 0, b.length);
    }

    public static String byteToHex(byte buffer[], int startOffset, int length) {
        StringBuilder hexString = new StringBuilder(2 * length);
        int endOffset = startOffset + length;

        for (int i = startOffset; i < endOffset; i++) {
            HexString.appendHexPair(buffer[i], hexString);
        }

        return hexString.toString();
    }

    public static String hexToString(String hexString) throws NumberFormatException {
        byte[] bytes = HexString.hexToByte(hexString);

        return new String(bytes);
    }

    public static byte[] hexToByte(String hexString) throws NumberFormatException {
        int length = hexString.length();
        byte[] buffer = new byte[(length + 1) / 2];
        boolean evenByte = true;
        byte nextByte = 0;
        int bufferOffset = 0;

        // If given an odd-length input string, there is an implicit
        // leading '0' that is not being given to us in the string.
        // In that case, act as if we had processed a '0' first.
        // It's sufficient to set evenByte to false, and leave nextChar
        // as zero which is what it would be if we handled a '0'.
        if ((length % 2) == 1) {
            evenByte = false;
        }

        for (int i = 0; i < length; i++) {
            char c = hexString.charAt(i);
            int nibble; // A "nibble" is 4 bits: a decimal 0..15

            if ((c >= '0') && (c <= '9')) {
                nibble = c - '0';
            } else if ((c >= 'A') && (c <= 'F')) {
                nibble = c - 'A' + 0x0A;
            } else if ((c >= 'a') && (c <= 'f')) {
                nibble = c - 'a' + 0x0A;
            } else {
                throw new NumberFormatException("Invalid hex digit '" + c + "'.");
            }

            if (evenByte) {
                nextByte = (byte) (nibble << 4);
            } else {
                nextByte += (byte) nibble;
                buffer[bufferOffset++] = nextByte;
            }

            evenByte = !evenByte;
        }

        return buffer;
    }

    public static byte[] str2byte(String s){
        byte[] b = new byte[s.length()];
        for(int i=0; i<b.length; i++){
            b[i] = (byte)HexString.parseToDec(s.charAt(i));
        }
        return b;
    }
    
    private static void appendHexPair(byte b, StringBuilder hexString) {
        char highNibble = kHexChars[(b & 0xF0) >> 4];
        char lowNibble = kHexChars[b & 0x0F];

        hexString.append(highNibble);
        hexString.append(lowNibble);
    }

    public static char parseToChar(int ch) {
        switch (ch) {
            case 10:
                return 'A';
            case 11:
                return 'B';
            case 12:
                return 'C';
            case 13:
                return 'D';
            case 14:
                return 'E';
            case 15:
                return 'F';
            default:
                return String.valueOf(ch).charAt(0);
        }
    }

    public static int parseToDec(char ch) {
        switch (ch) {
            case 'A':
                return 10;
            case 'B':
                return 11;
            case 'C':
                return 12;
            case 'D':
                return 13;
            case 'E':
                return 14;
            case 'F':
                return 15;
            default:
                return Integer.parseInt("" + ch);
        }
    }
    private static final char kHexChars[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
}
