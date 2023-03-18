module com.xingray.graalvm.compiler.android {
    requires com.xingray.graalvm.compiler.common;
    requires org.slf4j;
    requires com.xingray.java.util;

    exports com.xingray.graalvm.compiler.android;
    exports com.xingray.graalvm.compiler.android.target;
}