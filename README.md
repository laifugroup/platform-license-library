### 基于项目truelicense创建的java证书依赖项目

* 注意1： 本项目配合项目[license cli](https://github.com/laifugroup/license) 使用
* 注意2： 本项目keymgr还需要引入到你自己的项目中验证证书 <如项目启动/用户登录/web拦截器/jpa拦截器等的时候验证证书>
````
open class OauthAppStartupEventListener: ApplicationEventListener<ServerStartupEvent> {


    override fun onApplicationEvent(event: ServerStartupEvent?) {
        try {
            LicenseHelper.verifyLicense()
        }catch (e:Exception){
            println("[证书]许可已到期或不正确,请向服务商获取新证书。")
            exitProcess(-1)
        }
    }



}

````
1. 创建自己的true-license项目
````
mvn -X archetype:generate --batch-mode \
	-DarchetypeCatalog='local' \
    -DarchetypeGroupId='global.namespace.truelicense-maven-archetype' \
    -DarchetypeArtifactId='truelicense-maven-archetype' \
    -DarchetypeVersion='4.0.3' \
    -DartifactId='platform-license' \
    -Dcompany='Company Inc.' \
    -DgroupId='com.company.product' \
    -DkeyGenValidation='new com.company.product.keygen.ExtendLicenseValidation()' \
    -DkeyMgrValidation='new com.company.product.keymgr.ExtendLicenseValidation()' \
    -Dpassword='safepassword147258' \
    -Dsubject='group2020' \
    -Dversion='1.0.0'
    
````
2. 引入oshi 获取设备硬件信息 验证：cup/主板/硬盘等信息

````agsl
        <dependency>
            <groupId>com.github.oshi</groupId>
            <artifactId>oshi-core</artifactId>
            <version>6.4.2</version>
        </dependency>


````
验证信息：
````

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
````

指令环境 /shell 执行
````ignorelang

build in jdk1.8
idea 环境设置为jdk11


1.  java -jar keymgr.jar install license.lic
2.  java -jar keymgr.jar load
3.  java -jar keymgr.jar verify
4.  java -jar keymgr.jar uninstall

````
