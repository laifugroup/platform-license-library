package com.bbbang.parent.keymgr;

import global.namespace.truelicense.api.License;
import global.namespace.truelicense.api.LicenseValidation;
import global.namespace.truelicense.api.LicenseValidationException;
import global.namespace.truelicense.api.i18n.Message;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.HashMap;
import java.util.Locale;
import java.util.function.Consumer;

public class ExtendLicenseValidation implements LicenseValidation {



    /**
     * @title 验证附加信息
     * @param license
     * @throws LicenseValidationException
     * @desc 如果不验证硬件信息，那么只会验证时间是否过期
     */
    @Override
    public void validate(License license) throws LicenseValidationException {
        System.out.println("info->"+license.getInfo());
        System.out.println("issuer->"+license.getIssuer());
        System.out.println("subject->"+license.getSubject());
        System.out.println("consumerType->"+license.getConsumerType());
        System.out.println("holder->"+license.getHolder());
        System.out.println("consumerAmount->"+license.getConsumerAmount());
        System.out.println("issued->"+license.getIssued());
        System.out.println("notAfter->"+license.getNotAfter());
        System.out.println("notBefore->"+license.getNotBefore());

        if (!Issuer.subject.equals(license.getSubject())){
            throw new LicenseValidationException((Message) locale -> "[subject]授权主体不合法");
        }

        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        final String cupId=hal.getProcessor().getProcessorIdentifier().getProcessorID();
        final  String mainBoardSerial=hal.getComputerSystem().getHardwareUUID();

        //验证附加信息-> IP,CUP,网卡,主板
        Object extra=license.getExtra();
        if (extra!=null && extra instanceof HashMap){
            HashMap<String,String> map=(HashMap<String, String>) extra;
            map.keySet().stream().forEach(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    if (Constants.cpuSerial.equals(s)){
                        System.out.println("cupId->"+cupId+"  "+map.get(s));
                            if (!cupId.equals(map.get(s))){
                              throw new RuntimeException("[CPU]授权硬件不合法");
                            }
                    }
                    if (Constants.mainBoardSerial.equals(s)){
                        System.out.println("mainBoardSerial->"+mainBoardSerial+"  "+map.get(s));
                        if (!mainBoardSerial.equals(map.get(s))){
                            throw new RuntimeException("[mainBoardSerial]授权硬件不合法");
                        }
                    }
                    System.out.println(s+":"+map.get(s));
                }
            });
        }
    }
}
