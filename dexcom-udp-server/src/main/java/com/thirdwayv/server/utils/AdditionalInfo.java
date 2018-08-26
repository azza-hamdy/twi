package com.thirdwayv.server.utils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by FaridaMohamed
 */

/**
 * This Enum is used to define the additional info 2 bytes in comm header
 * to identify the type of received or sent message 
 */
public enum AdditionalInfo
{
    ClearMessage ((short) 0) ,
    EncryptedMessage ((short)1),
    SessionEstablishmentMessage ((short)2),
    PairingMessage ((short) 3);

    private static final Map<Short,AdditionalInfo> lookup = new HashMap<Short, AdditionalInfo>();

    static {
        for(AdditionalInfo aadInfo : EnumSet.allOf(AdditionalInfo.class))
            lookup.put(aadInfo.getCode(), aadInfo);
    }

    private short code;

    AdditionalInfo(short code) {
        this.code = code;
    }

    public short getCode() { return code; }

    public static AdditionalInfo get(short code) {
        return lookup.get(code);
    }
}