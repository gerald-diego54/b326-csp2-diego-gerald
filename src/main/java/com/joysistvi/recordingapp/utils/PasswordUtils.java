package com.joysistvi.recordingapp.utils;

import com.joysistvi.recordingapp.config.ArgonConfig;
import com.joysistvi.recordingapp.config.PropertiesConfig;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class PasswordUtils {

    private static final Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
    private static final ArgonConfig config = new ArgonConfig(new PropertiesConfig());

    private PasswordUtils() {
    }

    public static String hash(String rawPassword) {

        char[] password = rawPassword.toCharArray();

        try {
            return argon2.hash(config.iterations(), config.memory(), config.parallelism(), password);
        } finally {
            argon2.wipeArray(password);
        }
    }

    public static boolean verify(String hashedPassword, String rawPassword) {

        char[] password = rawPassword.toCharArray();

        try {
            return argon2.verify(hashedPassword, password);
        } finally {
            argon2.wipeArray(password);
        }
    }
}
