package com.tp3.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        System.out.println("🔍 Hashed password reçu : " + hashedPassword);
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }

}
