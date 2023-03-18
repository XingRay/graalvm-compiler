package com.xingray.graalvm.compiler.windows;

public class WindowsConstants {
    public static final String COMPILER = "cl";

    public static final String LINKER = "link";

    public static final String NATIVE_IMAGE = "native-image.cmd";

    public static final String OBJECT_FILE_EXTENSION = "obj";

    public static final String STATIC_LIBRARY_FILE_EXTENSION = "lib";

    public static final String[] JAVA_WINDOWS_LIB = {
            "advapi32", "iphlpapi", "secur32", "userenv", "version", "ws2_32", "winhttp", "ncrypt", "crypt32"
    };

    public static final String[] STATIC_JAVA_LIB = {
            "j2pkcs11", "java", "net", "nio", "prefs", "fdlibm", "sunec", "zip", "sunmscapi"
    };

    public static final String[] STATIC_JVM_LIB = {
            "jvm", "libchelper"
    };

    public static final String[] JAVAFX_WINDOWS_LIB = {
            "comdlg32", "dwmapi", "gdi32", "imm32", "shell32", "uiautomationcore", "urlmon", "winmm"
    };
    public static final String[] STATIC_JAVAFX_LIB = {
            "glass", "javafx_font", "javafx_iio", "prism_common", "prism_d3d"
    };
    public static final String[] STATIC_JAVAFX_SW_LIB = {
            "prism_sw"
    };
}
