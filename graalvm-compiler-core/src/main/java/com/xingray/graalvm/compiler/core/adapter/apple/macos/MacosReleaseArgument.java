package com.xingray.graalvm.compiler.core.adapter.apple.macos;

import com.xingray.graalvm.compiler.core.adapter.apple.AppleCommonReleaseArgument;

public class MacosReleaseArgument {
    // macOS
    private boolean macAppStore;

    private String macSigningUserName;

    private String macAppCategory;

    private AppleCommonReleaseArgument appleCommonReleaseArgument;

    public boolean isMacAppStore() {
        return macAppStore;
    }

    public void setMacAppStore(boolean macAppStore) {
        this.macAppStore = macAppStore;
    }

    public String getMacSigningUserName() {
        return macSigningUserName;
    }

    public void setMacSigningUserName(String macSigningUserName) {
        this.macSigningUserName = macSigningUserName;
    }

    public String getMacAppCategory() {
        return macAppCategory;
    }

    public void setMacAppCategory(String macAppCategory) {
        this.macAppCategory = macAppCategory;
    }

    public AppleCommonReleaseArgument getAppleCommonReleaseArgument() {
        return appleCommonReleaseArgument;
    }

    public void setAppleCommonReleaseArgument(AppleCommonReleaseArgument appleCommonReleaseArgument) {
        this.appleCommonReleaseArgument = appleCommonReleaseArgument;
    }

    @Override
    public String toString() {
        return "MacosReleaseArgument{" +
                "macAppStore=" + macAppStore +
                ", macSigningUserName='" + macSigningUserName + '\'' +
                ", macAppCategory='" + macAppCategory + '\'' +
                ", appleCommonReleaseArgument=" + appleCommonReleaseArgument +
                '}';
    }
}
