package org.hackDefender.util;

import java.util.UUID;

/**
 * @author vvings
 * @version 2020/4/20 22:39
 */
public class UUIDUtil {
    public static String getUUID8() {
        String uuid = UUID.randomUUID().toString();
        return new StringBuilder(uuid).substring(0, 8);
    }
}
