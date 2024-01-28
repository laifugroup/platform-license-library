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
    