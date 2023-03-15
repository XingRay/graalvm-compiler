package com.xingray.graalvm.compiler.core;

import com.xingray.graalvm.compiler.common.CompilerException;

import java.util.Arrays;

public class Launcher {
    public static void main(String[] args) {
        CompileProject compileProject = parseArgs(args);
        if (compileProject == null) {
            throw new IllegalArgumentException("can not parse compile params, args:" + Arrays.toString(args));
        }
        GraalvmCompiler compiler = new GraalvmCompiler(compileProject);

        try {
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

    public static CompileProject parseArgs(String[] args) {
        if (args == null || args.length == 0) {
            return null;
        }
        CompileProject compileProject = new CompileProject();


        return compileProject;
    }
}
