package com.xingray.graalvm.compiler.core.configuration;

public class MacosConfiguration {
    public static class Release{

        // pkg or dmg
        private String packageType;

        /**
         * Boolean that indicates if the macOS bundle is intended for the Mac App Store.
         */
        private boolean macAppStore;

        /**
         * Team or user name portion in Apple signing identities
         */
        private String macSigningUserName;

        /**
         * The category that best describes the app for the Mac App Store.
         * Default is public.app-category.utilities. See
         * https://developer.apple.com/documentation/bundleresources/information_property_list/lsapplicationcategorytype
         * for the full list of categories.
         */
        private String macAppCategory;

        // macOS/iOS

        /**
         * A user-visible short name for the bundle
         * <p>
         * Default: if not set, $appName will be used.
         */
        private String bundleName;

        /**
         * Boolean that can be used to skip signing macOS/iOS apps. This will prevent any
         * deployment, but can be useful to run tests without an actual device
         */
        private boolean skipSigning;

        /**
         * The version of the build that identifies an iteration of the bundle. A
         * string composed of one to three period-separated integers, containing
         * numeric characters (0-9) and periods only.
         * <p>
         * Default: 1.0
         */
        private String bundleVersion;

        /**
         * A user-visible string for the release or version number of the bundle. A
         * string composed of one to three period-separated integers, containing
         * numeric characters (0-9) and periods only.
         * <p>
         * Default: 1.0
         */
        private String bundleShortVersion;

        /**
         * String that identifies a valid certificate that will be used for macOS/iOS development
         * or macOS/iOS distribution.
         * <p>
         * Default: null. When not provided, Substrate will be selected from all the valid identities found
         * installed on the machine from any of these types:
         * <p>
         * macOS: Apple Development|Apple Distribution|Mac Developer|3rd Party Mac Developer Application|Developer ID Application
         * iOS: iPhone Developer|Apple Development|iOS Development|iPhone Distribution
         * <p>
         * and that were used by the provisioning profile.
         */
        private String providedSigningIdentity;

        /**
         * String with the name of the provisioning profile created for macOS/iOS development or
         * distribution of the given app.
         * <p>
         * Default: null. When not provided, Substrate will try to find a valid installed
         * provisioning profile that can be used to sign the app, including wildcards.
         */
        private String providedProvisioningProfile;
    }
}
