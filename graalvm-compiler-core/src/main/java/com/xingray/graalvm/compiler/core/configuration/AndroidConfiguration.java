package com.xingray.graalvm.compiler.core.configuration;

public class AndroidConfiguration {
    public static class Release{

        // 'apk' or 'aab'
        private String packageType;

        private String appLabel;

        private String versionCode;

        private String versionName;

        private String providedKeyStorePath;

        private String providedKeyStorePassword;

        private String providedKeyAlias;
        private String providedKeyAliasPassword;
    }
}
