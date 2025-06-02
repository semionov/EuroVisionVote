package com.rviewer.skeletons.domain.blockchain;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Country {
    BE, EL, LT, PT, BG, ES, LU, RO, CZ,
    FR, HU, SI, DK, HR, MT, SK, DE, IT,
    NL, FI, EE, CY, AT, SE, IE, LV, PL;

    public static boolean isValid(String code) {
        try {
            Country.valueOf(code);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}

