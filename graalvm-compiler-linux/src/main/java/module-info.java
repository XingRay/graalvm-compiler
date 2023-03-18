module com.xingray.graalvm.compiler.linux {
    requires com.xingray.graalvm.compiler.common;
    requires org.slf4j;
    requires com.xingray.java.util;

    exports com.xingray.graalvm.compiler.linux;
    exports com.xingray.graalvm.compiler.linux.target;
}