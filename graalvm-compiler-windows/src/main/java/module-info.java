module com.xingray.graalvm.compiler.windows {
    requires com.xingray.graalvm.compiler.common;
    requires com.xingray.java.command;
    requires org.slf4j;
    requires com.xingray.java.util;

    exports com.xingray.graalvm.compiler.windows;
    exports com.xingray.graalvm.compiler.windows.util;
    exports com.xingray.graalvm.compiler.windows.target;
}