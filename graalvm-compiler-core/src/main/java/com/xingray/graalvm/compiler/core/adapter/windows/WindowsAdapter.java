package com.xingray.graalvm.compiler.core.adapter.windows;

import com.xingray.graalvm.compiler.core.CompileArgument;
import com.xingray.graalvm.compiler.windows.WindowsArgument;

public class WindowsAdapter {
    public WindowsArgument createWindowsCompileArgument(CompileArgument compileArgument) {
        System.out.println("createWindowsCompileArgument, compileArgument:" + compileArgument);
        return new WindowsArgument();
    }
}
