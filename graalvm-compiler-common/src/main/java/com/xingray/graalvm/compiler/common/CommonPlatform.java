package com.xingray.graalvm.compiler.common;

import com.xingray.java.command.JavaRuntimeCommandExecutor;
import com.xingray.java.util.FileUtil;
import com.xingray.java.util.StringUtil;
import com.xingray.java.util.collection.CollectionUtil;

import java.io.File;
import java.nio.file.Path;

public class CommonPlatform {

    private final CommonArgument commonArgument;

    public CommonPlatform(CommonArgument commonArgument) {
        this.commonArgument = commonArgument;
    }

    public boolean prepare() {
        if (!checkGraalvm(commonArgument.getCompile().getGraalvmHome())) {
            return false;
        }

        if (!checkProjectDir(commonArgument)) {
            return false;
        }

        if (!checkCompiler()) {
            return false;
        }

        return true;
    }

    private boolean checkProjectDir(CommonArgument commonArgument) {
        String projectDir = commonArgument.getCompile().getProjectDir();

        if (!FileUtil.isFolderExist(projectDir)) {
            System.out.println("project dir not exist: " + projectDir);
            return false;
        }

        String outputDir = commonArgument.getCompile().getOutputDir();
        if (StringUtil.isEmpty(outputDir)) {
            outputDir = Path.of(projectDir).resolve("target").toUri().getPath();
            commonArgument.getCompile().setOutputDir(outputDir);
        }

        return true;
    }

    private boolean checkGraalvm(String graalvmHome) {
        if (StringUtil.isEmpty(graalvmHome)) {
            System.out.println("graalvmHome is not set, use system environment variable GRAALVM_HOME");
            graalvmHome = System.getenv("GRAALVM_HOME");
            System.out.println("system environment variable GRAALVM_HOME: " + graalvmHome);
        }

        if (!FileUtil.isFolderExist(graalvmHome)) {
            System.out.println("graalvmHome not found, set graalvmHome in " + Constants.PLUGIN_NAME
                    + " plugin configuration or environment variable");
            return false;
        }

        return true;
    }

    public boolean checkCompiler() {
        String compilerBackend = commonArgument.getCompile().getCompilerBackend();
        Platform targetPlatform = commonArgument.getCompile().getTargetPlatform();
        String graalvmHome = commonArgument.getCompile().getGraalvmHome();

        System.out.println("compilerBackend: " + compilerBackend + " targetPlatform:" + targetPlatform);

        if (!compilerBackend.equals(Constants.BACKEND_LLVM)) {
            return true;
        }
        if (!CollectionUtil.contains(new Platform[]{Platform.ANDROID, Platform.IOS, Platform.LINUX},
                targetPlatform)) {
            System.out.println(compilerBackend + " can not compile to " + targetPlatform);
            return false;
        }
        if (!FileUtil.isFolderExist(graalvmHome)) {
            System.out.println("GraalvmHome dir not exist");
            return false;
        }
        if (!new File(graalvmHome, "lib/llvm/bin/llvm-config.exe").exists()) {
            String cmd = "gu --jvm install llvm-toolchain";
            int result = new JavaRuntimeCommandExecutor().execute(cmd);
            if (result != 0) {
                System.out.println("llvm install failed, use command : " + cmd);
                return false;
            }
        }
        return true;
    }
}
