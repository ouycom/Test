/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sscs.apitest.pinpad.secure;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author kim_zud
 */
public class PinpadSecurity {
    
    
    public static synchronized String generateRandomKey(int keylen) throws NoSuchAlgorithmException, Exception{
        return EncryptedPIN.getInstance().generateRandomKey(keylen);
    }
    public static synchronized String encryptToHex(String key, String data) throws Exception {
        return EncryptedPIN.getInstance().encryptToHex(key, data);
    }

    public static synchronized String decryptToHex(String key, String encdata) throws Exception {
        return EncryptedPIN.getInstance().decryptToHex(key, encdata);
    }
    
    public static synchronized String getKvc(String key, int kcvlen) throws Exception{
        final String data = "1111111111111111";
        String kvc = EncryptedPIN.getInstance().encryptToHex(key, data);
        return kvc.substring(0, kcvlen);
        
    }

    public static synchronized String encryptPinAnsi(String key, String pan, String pin) throws Exception {
        return EncryptedPIN.getInstance().encryptPin(key, pan, pin, EncryptedPIN.ANSI);
    }

//    public static synchronized String encryptWithInternalKey(String data) throws GeneralSecurityException{
//        return Config.encrypt(data);
//    }
//    public static synchronized String decryptWithInternalKey(String encdata) throws GeneralSecurityException{
//        return Config.decrypt(encdata);
//    }
    
}
