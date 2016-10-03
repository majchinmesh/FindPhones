package com.lol.majchin.findphones;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by majch on 02-10-2016.
 */
public class vcode {

    private static SecureRandom random = new SecureRandom();

    public static String getVcode() {
        return new BigInteger(130, random).toString(32);
    }

}
