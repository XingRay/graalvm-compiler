package com.xingray.graalvm.compiler.shared.apple;

public class AppleCommonReleaseArgument {

    // macOS/iOS

    private String bundleName;

    private String bundleVersion;
    private String bundleShortVersion;

    private String providedSigningIdentity;

    private String providedProvisioningProfile;

    public String getBundleName() {
        return bundleName;
    }

    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

    public String getBundleVersion() {
        return bundleVersion;
    }

    public void setBundleVersion(String bundleVersion) {
        this.bundleVersion = bundleVersion;
    }

    public String getBundleShortVersion() {
        return bundleShortVersion;
    }

    public void setBundleShortVersion(String bundleShortVersion) {
        this.bundleShortVersion = bundleShortVersion;
    }

    public String getProvidedSigningIdentity() {
        return providedSigningIdentity;
    }

    public void setProvidedSigningIdentity(String providedSigningIdentity) {
        this.providedSigningIdentity = providedSigningIdentity;
    }

    public String getProvidedProvisioningProfile() {
        return providedProvisioningProfile;
    }

    public void setProvidedProvisioningProfile(String providedProvisioningProfile) {
        this.providedProvisioningProfile = providedProvisioningProfile;
    }

    @Override
    public String toString() {
        return "AppleCommonReleaseArgument{" +
                "bundleName='" + bundleName + '\'' +
                ", bundleVersion='" + bundleVersion + '\'' +
                ", bundleShortVersion='" + bundleShortVersion + '\'' +
                ", providedSigningIdentity='" + providedSigningIdentity + '\'' +
                ", providedProvisioningProfile='" + providedProvisioningProfile + '\'' +
                '}';
    }
}
