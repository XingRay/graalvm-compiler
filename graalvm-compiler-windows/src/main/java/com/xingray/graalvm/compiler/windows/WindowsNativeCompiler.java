package com.xingray.graalvm.compiler.windows;

import com.xingray.graalvm.compiler.common.CompilerException;
import com.xingray.graalvm.compiler.common.NativeCompiler;

public class WindowsNativeCompiler implements NativeCompiler {

    private final WindowsArgument windowsArgument;

    public WindowsNativeCompiler(WindowsArgument windowsArgument) {
        this.windowsArgument = windowsArgument;
    }

    @Override
    public int prepare() throws CompilerException {
        System.out.println("sout: " + windowsArgument);
        return 0;
    }

    @Override
    public int executeCompile() throws CompilerException {
        return 0;
    }

    @Override
    public int executeLink() throws CompilerException {
        return 0;
    }

    @Override
    public int executePackage() throws CompilerException {
        return 0;
    }

    @Override
    public int executeInstall() throws CompilerException {
        return 0;
    }

    @Override
    public int executeRun() throws CompilerException {
        return 0;
    }
}
