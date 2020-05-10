package com.configfiles.theatreticketsapp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashPassword {


    public static String getSHA256SecurePassword(String passwordToHash) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes)
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

}
