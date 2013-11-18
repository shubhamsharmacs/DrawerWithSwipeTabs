package com.arcasolutions.util;

import com.arcasolutions.api.Client;
import com.arcasolutions.api.model.EdirectoryConf;

public class FmtUtil {

    public static String distance(double latitude, double longitude) {
        EdirectoryConf cnf = Client.getConf();
        String label = cnf != null
                ? cnf.getDistanceLabel()
                : "km";

        double distanceInMeters = Util.distanceFromMe(latitude, longitude);
        if (distanceInMeters == -1) return "";

        String format = "%1$.2f %2$s";
        double finalDistance = "mile".equalsIgnoreCase(label)
                ? mile(distanceInMeters)
                : km(distanceInMeters);

        String finalLabel = label
                + ((label.equalsIgnoreCase("mile") && finalDistance > 1)
                    ? "s"
                    : "");

        return String.format(format, finalDistance, finalLabel);

    }

    private static double km(double distanceInMeters) {
        return distanceInMeters / 1000;
    }

    private static double mile(double distanceInMeters) {
        return distanceInMeters * 0.000621371;
    }

}
