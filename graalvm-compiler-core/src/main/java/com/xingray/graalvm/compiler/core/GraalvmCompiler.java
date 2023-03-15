package com.xingray.graalvm.compiler.core;

import com.xingray.graalvm.compiler.android.AndroidNativeCompiler;
import com.xingray.graalvm.compiler.common.CompilerException;
import com.xingray.graalvm.compiler.common.NativeCompiler;
import com.xingray.graalvm.compiler.common.Platform;
import com.xingray.graalvm.compiler.ios.IosNativeCompiler;
import com.xingray.graalvm.compiler.linux.LinuxNativeCompiler;
import com.xingray.graalvm.compiler.macos.MacosNativeCompiler;
import com.xingray.graalvm.compiler.web.WebNativeCompiler;
import com.xingray.graalvm.compiler.windows.WindowsNativeCompiler;

public class GraalvmCompiler implements NativeCompiler {

    private final NativeCompiler nativeCompiler;

    public GraalvmCompiler(CompileProject compileProject) {
        this.nativeCompiler = createNativeCompiler(compileProject);
    }

    private NativeCompiler createNativeCompiler(CompileProject compileProject) {
        if (compileProject == null) {
            throw new IllegalArgumentException("compileProject can not be null");
        }
        Platform targetPlatform = compileProject.getTargetPlatform();
        if (targetPlatform == null) {
            throw new IllegalArgumentException("targetPlatform is not set in compileProject:" + compileProject);
        }
        return switch (targetPlatform) {
            case WINDOWS -> new WindowsNativeCompiler();
            case LINUX -> new LinuxNativeCompiler();
            case MACOS -> new MacosNativeCompiler();
            case WEB -> new WebNativeCompiler();
            case ANDROID -> new AndroidNativeCompiler();
            case IOS -> new IosNativeCompiler();
        };
    }

    @Override
    public int executeCompile() throws CompilerException {
        return nativeCompiler.executeCompile();
    }

    @Override
    public int executeLink() throws CompilerException {
        return nativeCompiler.executeLink();
    }

    @Override
    public int executePackage() throws CompilerException {
        return nativeCompiler.executePackage();
    }

    @Override
    public int executeInstall() throws CompilerException {
        return nativeCompiler.executeInstall();
    }

    @Override
    public int executeRun() throws CompilerException {
        return nativeCompiler.executeRun();
    }
}
