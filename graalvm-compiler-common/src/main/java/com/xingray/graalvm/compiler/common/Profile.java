package com.xingray.graalvm.compiler.common;

public enum Profile {
        LINUX, // (x86_64-linux-linux)
        LINUX_AARCH64, // (aarch64-linux-linux or aarch64-linux-gnu)
        MACOS, // (x86_64-apple-darwin)
        MACOS_AARCH64, // (aarch64-apple-darwin)
        WINDOWS, // (x86_64-windows-windows)
        IOS,   // (aarch64-apple-ios)
        IOS_SIM,   // (x86_64-apple-ios)
        ANDROID, // (aarch64-linux-android);
        WEB // (x86_64-web-web)
    };