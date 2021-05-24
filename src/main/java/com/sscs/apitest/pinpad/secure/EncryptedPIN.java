/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sscs.apitest.pinpad.secure;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptedPIN {

    private static EncryptedPIN instance;

    private EncryptedPIN() {
    }

    public static EncryptedPIN getInstance() {
        if (instance == null) {
            instance = new EncryptedPIN();
        }
        return instance;
    }

    
//    public synchronized byte[] encrypt(String key, byte[] data) throws Exception {
//        SecretKeySpec sksSpec = new SecretKeySpec(HexString.hexToByte(key), "DES");
//        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
//        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, sksSpec);
//        byte[] ciphertext = cipher.doFinal(data);
//        return ciphertext;
//    }   
    
    /**
     * encrypt
     */
    public synchronized byte[] encrypt(String key, String plaintext) throws Exception {
        SecretKeySpec sksSpec = new SecretKeySpec(HexString.hexToByte(key), "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, sksSpec);
        byte[] ciphertext = cipher.doFinal(HexString.hexToByte(plaintext));
        return ciphertext;
    }
    
    /**
     * decrypt
     */
    public synchronized byte[] decrypt(String key, byte[] ciphertext) throws Exception {
        SecretKeySpec sksSpec = new SecretKeySpec(HexString.hexToByte(key), "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, sksSpec);
        byte[] plain = cipher.doFinal(ciphertext);
        return plain;
    }

    public String encryptToHex(String key, String plaintext) throws Exception {
        if (key.length() == 16) {
            return HexString.byteToHex(encrypt(key, plaintext));
        } else if (key.length() == 32) {
            String key1 = key.substring(0, 16);
            String key2 = key.substring(16);
            String data = plaintext;
            data = encryptToHex(key1, data);
            data = decryptToHex(key2, data);
            data = encryptToHex(key1, data);
            return data;
        } else {
            throw new Exception("Key length is not valid.");
        }
    }

    public String decryptToHex(String key, String ciphertext) throws Exception {
        if (key.length() == 16) {
            return HexString.byteToHex(decrypt(key, HexString.hexToByte(ciphertext)));
        } else if (key.length() == 32) {
            String key1 = key.substring(0, 16);
            String key2 = key.substring(16);
            String data = ciphertext;
            data = decryptToHex(key1, data);
            data = encryptToHex(key2, data);
            data = decryptToHex(key1, data);
            return data;
        } else {
            throw new Exception("Key length is not valid. key["+key+"] ciphertext["+ciphertext+"]");
        }
    }

    public String translateData(String ciphertext, String srcKey, String destKey) throws Exception {
        String plaintext = this.decryptToHex(srcKey, ciphertext);
        return this.encryptToHex(destKey, plaintext);
    }

    public String generateRandomKey(int size) throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(System.currentTimeMillis());
        return HexString.byteToHex(random.generateSeed(size / 2));
    }

    public String addPadF(String value, int algo) {
        switch (algo) {
            case PINP:
                return value + "FFFFFFFFFFFFFFFF".substring(value.length());
            case ANSI:
                value = format.format(value.length()) + value;
                return value + "FFFFFFFFFFFFFFFF".substring(value.length());
            default:
                throw new SecurityException("Unknown algorithm[" + algo + "].");
        }
    }

    public String removePadF(String value, int algo) {
        if (value.indexOf("F") < 0) {
            throw new SecurityException("Invalid Pin Format.");
        }
        switch (algo) {
            case PINP:
                return value.substring(0, value.indexOf("F"));
            case ANSI:
                int len = Integer.parseInt(value.substring(0, 2));
                value = value.substring(2);
                return value.substring(0, len);
            default:
                throw new SecurityException("Unknown algorithm[" + algo + "].");
        }
    }
    
    public String removePINFormat(String pan,String pin,int algo) throws Exception{
        String decryptPin = pin;
        switch (algo) {
            case PINP:
                decryptPin = removePadF(decryptPin, algo);
                return decryptPin;
            case ANSI:
                String acctNum;
                acctNum = pan.substring(0, pan.length() - 1);
                acctNum = acctNum.substring(acctNum.length() - 12);
                acctNum = "0000" + acctNum;
                decryptPin = xor(decryptPin, acctNum);
                decryptPin = removePadF(decryptPin, algo);
                return decryptPin;
            default:
                throw new SecurityException("Unknown algorithm[" + algo + "].");
        }        
    }
    
    public String encryptPin(String key, String pan, String pin, int algo) throws Exception {
        pin = addPadF(pin, algo);
        switch (algo) {
            case PINP:
                return encryptToHex(key, pin);
            case ANSI:
                String acctNum;
                acctNum = pan.substring(0, pan.length() - 1);
                acctNum = acctNum.substring(acctNum.length() - 12);
                acctNum = "0000" + acctNum;
                pin = xor(pin, acctNum);
                return encryptToHex(key, pin);
            default:
                throw new SecurityException("Unknown algorithm[" + algo + "].");
        }
    }

    public String decryptPin(String key, String pan, String pin, int algo) throws Exception {
        String decryptPin = decryptToHex(key, pin);
        return removePINFormat(pan, decryptPin, algo);
    }

    public String xor(String data1, String data2) {
        if (data1.length() != data2.length()) {
            throw new SecurityException("Invalid length[" + data1.length() + ", " + data2.length() + "].");
        }
        int ch;
        String ret = "";
        for (int i = 0; i < data1.length(); i++) {
            ch = HexString.parseToDec(data1.charAt(i)) ^ HexString.parseToDec(data2.charAt(i));

            ret += HexString.parseToChar(ch);
        }
        return ret;
    }

    public int getAlgorithmCode(String str) {
        if ("ANSI".equals(str.toUpperCase().trim())) {
            return ANSI;
        } else if ("PINP".equals(str.toUpperCase().trim())) {
            return PINP;
        }
        return -1;
    }

    public String generatePassword(String data) throws NoSuchAlgorithmException, DigestException {
        if (data == null) {
            return null;
        }
        String password = "";
        byte[] abyte0 = data.getBytes();
        MessageDigest messagedigest = MessageDigest.getInstance("SHA-1");
        messagedigest.reset();
        messagedigest.update(abyte0);
        byte abyte1[] = messagedigest.digest();
        for (int i = 0; i < abyte1.length; i++) {
            int j = abyte1[i] & 0xff;
            if (j < 36) {
                password = password + "0";
            }
            password = password + Integer.toString(j, 36);
        }

        return password;
    }
    
    //----------- CVV -----------------
    public String calculateCVV(String key,String pan,String expiredate,String serviceCode) throws Exception{
        String cvv = "000";
        /**
         * 1 ) Extract data & Place into 128-bit field padded to the right with binary zeros
         */
        String data = pan + expiredate + serviceCode;
        data = HexString.padRight(data, 32, '0');
        
        /**
         * 2)  Split field into two 64-bit blocks
         */
        String block1 = data.substring(0, 16);
        String block2 = data.substring(16);
        
        /**
         * 3) Encrypt: Block 1 using Key A, XOR the result with Block 2, 
         */
        String keyA = key.substring(0,16);
        String keyB = key.substring(16);
        data = encryptToHex(keyA, block1);
        data = xor(data, block2);
        
        /**
         * 4) then encrypt the XOR result with Key A,
         *    Decrypt the result of step 5 with Key B
         *    Encrypt the result of step 6 with Key A
         */
        data = encryptToHex(keyA, data);
        data = decryptToHex(keyB, data);
        data = encryptToHex(keyA, data);        
        
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();
        for(int i=0; i<data.length(); i++){
            if(Character.isDigit(data.charAt(i))){
                s1.append(data.charAt(i));
            }else{
                s2.append(data.charAt(i));
            }
        }
        String s = s2.toString();
        for(int i=0; i<s.length(); i++){
            s1.append(HexString.parseToDec(s.charAt(i)) - 10);
        }
        
        cvv = s1.substring(0, 3);
        
        return cvv;
    }
    
    public String calculateCVV2(String key,String pan,String expiredate) throws Exception{
        return calculateCVV(key, pan, expiredate, "000");
    }
    
    public String calculateICVV(String key,String pan,String expiredate) throws Exception{
        return calculateCVV(key, pan, expiredate, "999");
    }    
    
    public static final int PINP = 3;
    public static final int ANSI = 1;
    private DecimalFormat format = new DecimalFormat("00");
}
