module com.xingray.graalvm.compiler.common {
    requires java.logging;
    requires com.xingray.java.util;
    requires com.xingray.java.command;

    exports com.xingray.graalvm.compiler.common;
    exports com.xingray.graalvm.compiler.common.util;
    exports com.xingray.graalvm.compiler.common.util.plist;
    exports com.xingray.graalvm.compiler.common.model;
    exports com.xingray.graalvm.compiler.common.target;
}