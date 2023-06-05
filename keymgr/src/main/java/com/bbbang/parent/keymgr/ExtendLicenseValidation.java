package com.bbbang.parent.keymgr;

import global.namespace.truelicense.api.License;
import global.namespace.truelicense.api.LicenseValidation;
import global.namespace.truelicense.api.LicenseValidationException;

import java.util.HashMap;
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

        //验证附加信息-> IP,CUP,网卡,主板
        Object extra=license.getExtra();
        if (extra!=null && extra instanceof HashMap){
            HashMap<String,String> map=(HashMap<String, String>) extra;
            map.keySet().stream().forEach(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    System.out.println(s+":"+map.get(s));
                }
            });
        }
    }
}
