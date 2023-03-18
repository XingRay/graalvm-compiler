package com.xingray.graalvm.compiler.core;

import com.xingray.graalvm.compiler.common.CompilerException;

import java.util.Arrays;

public class Launcher {
    public static void main(String[] args) {
        CompileConfiguration compileConfiguration = parseArgs(args);
        if (compileConfiguration == null) {
            throw new IllegalArgumentException("can not parse compile params, args:" + Arrays.toString(args));
        }
        GraalvmCompiler compiler = new GraalvmCompiler(compileConfiguration);

        try {
            compiler.prepare();
            compiler.executeCompile();
            compiler.executeLink();
            compiler.executePackage();
            compiler.executeInstall();
            compiler.executeRun();
        } catch (CompilerException e) {
            throw new RuntimeException(e);
        }


//        Path buildRoot = Paths.get(System.getProperty("user.dir"), "build", "autoclient");
//        ProjectConfiguration configuration = createProjectConfiguration();
//        GraalvmCompilerImpl dispatcher = new GraalvmCompilerImpl(buildRoot, configuration);
//
//        executeCompileStep(dispatcher);
//
//        if (step.requires(Step.LINK)) {
//            executeLinkStep(dispatcher);
//        }
//
//        if (step.requires(Step.PACKAGE)) {
//            executePackageStep(dispatcher);
//        }
//
//        if (step.requires(Step.INSTALL)) {
//            executeInstallStep(dispatcher);
//        }
//
//        if (step.requires(Step.RUN)) {
//            executeRunStep(dispatcher);
//        }
    }

    public static CompileConfiguration parseArgs(String[] args) {
        if (args == null || args.length == 0) {
            return null;
        }
        CompileConfiguration compileConfiguration = new CompileConfiguration();


        return compileConfiguration;
    }
}
