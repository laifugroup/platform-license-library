package com.bbbang.parent.keymgr;

import global.namespace.truelicense.api.License;
import global.namespace.truelicense.api.LicenseValidation;
import global.namespace.truelicense.api.LicenseValidationException;
import global.namespace.truelicense.api.i18n.Message;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ExtendLicenseValidation implements LicenseValidation {



    /**
     * @title 验证附加信息
     * @param license
     * @throws LicenseValidationException
     * @desc 如果不验证硬件信息，那么只会验证时间是否过期
     */
    @Override
    public void validate(License license) throws LicenseValidationException {
//        System.out.println("info->"+license.getInfo());
//        System.out.println("issuer->"+license.getIssuer());
//        System.out.println("subject->"+license.getSubject());
//        System.out.println("consumerType->"+license.getConsumerType());
//        System.out.println("holder->"+license.getHolder());
//        System.out.println("consumerAmount->"+license.getConsumerAmount());
//        System.out.println("issued->"+license.getIssued());
        //System.out.println("notAfter->"+license.getNotAfter());
        //System.out.println("notBefore->"+license.getNotBefore());

        if (!Issuer.subject.equals(license.getSubject())){
            throw new LicenseValidationException((Message) locale -> "[subject]授权主体不正确,请联系管理员");
        }else  if (license.getNotBefore()!=null && license.getNotBefore().after(new Date())){
            throw new LicenseValidationException((Message) locale -> "[subject]授权未生效,请联系管理员");
        }else  if (license.getNotAfter()!=null && license.getNotAfter().before(new Date())){
            throw new LicenseValidationException((Message) locale -> "[subject]授权已过期,请联系管理员");
        }

        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        final String cupId=hal.getProcessor().getProcessorIdentifier().getProcessorID();
        final  String mainBoardSerial=hal.getComputerSystem().getHardwareUUID();

        //验证附加信息-> IP,CUP,网卡,主板
        Object extra=license.getExtra();
        if (extra!=null && extra instanceof HashMap){
            HashMap<String,String> map=(HashMap<String, String>) extra;
            String mCupId=map.get(LicenseConstants.cpuSerial);
            String mMainBoardSerial=map.get(LicenseConstants.mainBoardSerial);


            if (!cupId.equals(mCupId)){
                System.out.println("cupId->"+cupId);
                System.out.println("mCupId->"+mCupId);
                throw new RuntimeException("[CPU]序列号不正确,请联系管理员");
            }else  if (!mainBoardSerial.equals(mMainBoardSerial)){
                System.out.println("mainBoardSerial->"+mainBoardSerial);
                System.out.println("mMainBoardSerial->"+mMainBoardSerial);
                throw new RuntimeException("[mainBoard]序列号不正确,请联系管理员");
            }
            //System.out.println("验证成功");
        }
    }

}


