package com.xingray.graalvm.compiler.android;

import com.xingray.graalvm.compiler.common.CompilerException;
import com.xingray.graalvm.compiler.common.NativeCompiler;

public class AndroidNativeCompiler implements NativeCompiler {
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
