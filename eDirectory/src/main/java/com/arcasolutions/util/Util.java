package com.arcasolutions.util;

import android.os.Build;

public class Util {

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

}
