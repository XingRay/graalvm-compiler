module com.xingray.graalvm.compiler.core {
    requires com.xingray.graalvm.compiler.common;
    requires com.xingray.graalvm.compiler.windows;
    requires com.xingray.graalvm.compiler.macos;
    requires com.xingray.graalvm.compiler.linux;
    requires com.xingray.graalvm.compiler.web;
    requires com.xingray.graalvm.compiler.android;
    requires com.xingray.graalvm.compiler.ios;
    requires com.xingray.graalvm.compiler.shared.apple;

    requires com.xingray.java.maven.core;
    requires com.xingray.java.command;
    requires com.xingray.java.util;
    requires com.xingray.java.base;

    requires org.slf4j;

    exports com.xingray.graalvm.compiler.core;
    exports com.xingray.graalvm.compiler.core.configuration;

}