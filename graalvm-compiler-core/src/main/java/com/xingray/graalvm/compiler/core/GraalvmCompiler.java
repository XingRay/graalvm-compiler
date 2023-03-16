package com.xingray.graalvm.compiler.core;

import com.xingray.graalvm.compiler.android.AndroidNativeCompiler;
import com.xingray.graalvm.compiler.common.CommonPlatform;
import com.xingray.graalvm.compiler.common.CompilerException;
import com.xingray.graalvm.compiler.common.NativeCompiler;
import com.xingray.graalvm.compiler.common.Platform;
import com.xingray.graalvm.compiler.core.adapter.windows.WindowsAdapter;
import com.xingray.graalvm.compiler.ios.IosNativeCompiler;
import com.xingray.graalvm.compiler.linux.LinuxNativeCompiler;
import com.xingray.graalvm.compiler.macos.MacosNativeCompiler;
import com.xingray.graalvm.compiler.web.WebNativeCompiler;
import com.xingray.graalvm.compiler.windows.WindowsArgument;
import com.xingray.graalvm.compiler.windows.WindowsNativeCompiler;

public class GraalvmCompiler implements NativeCompiler {

    private final NativeCompiler nativeCompiler;
    private final CommonPlatform commonPlatform;

    public GraalvmCompiler(CompileArgument compileArgument) {
        this.nativeCompiler = createNativeCompiler(compileArgument);
        this.commonPlatform = new CommonPlatform(compileArgument.getCommonArgument());
    }

    private NativeCompiler createNativeCompiler(CompileArgument compileArgument) {
        System.out.println("createNativeCompiler, compileArgument:" + compileArgument);

        if (compileArgument == null) {
            System.out.println("compileArgument is null");
            throw new IllegalArgumentException("compileProject can not be null");
        }
        Platform targetPlatform = compileArgument.getCommonArgument().getCompile().getTargetPlatform();
        if (targetPlatform == null) {
            System.out.println("targetPlatform is null");
            throw new IllegalArgumentException("targetPlatform is not set in compileProject:" + compileArgument);
        }

        return switch (targetPlatform) {
            case WINDOWS -> {
                WindowsArgument windowsArgument = new WindowsAdapter().createWindowsCompileArgument(compileArgument);
                yield new WindowsNativeCompiler(windowsArgument);
            }
            case LINUX -> new LinuxNativeCompiler();
            case MACOS -> new MacosNativeCompiler();
            case WEB -> new WebNativeCompiler();
            case ANDROID -> new AndroidNativeCompiler();
            case IOS -> new IosNativeCompiler();
        };
    }

    @Override
    public int prepare() throws CompilerException {
        commonPlatform.prepare();
        return nativeCompiler.prepare();
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
