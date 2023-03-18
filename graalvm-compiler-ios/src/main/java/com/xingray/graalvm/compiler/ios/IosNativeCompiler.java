package com.xingray.graalvm.compiler.ios;

import com.xingray.graalvm.compiler.common.CompilerException;
import com.xingray.graalvm.compiler.common.NativeCompiler;

public class IosNativeCompiler implements NativeCompiler {
    @Override
    public int prepare() throws CompilerException {
//        logger.info("compilerBackend: " + compilerBackend + " targetPlatform:" + targetPlatform);
//        if (StringUtil.isEmpty(compilerBackend) || !compilerBackend.equals(Constants.BACKEND_LLVM)) {
//            logger.info("compilerBackend not set to use llvm");
//            return true;
//        }
//
//        if (!CollectionUtil.contains(new Platform[]{Platform.ANDROID, Platform.IOS, Platform.LINUX},
//                targetPlatform)) {
//            logger.info(compilerBackend + " can not compile to " + targetPlatform);
//            return false;
//        }
//
//        logger.info("check llvm toolchain");
//        if (!new File(graalvmHome, "lib/llvm/bin/llvm-config.exe").exists()) {
//            logger.info("llvm not installed, try to install llvm toolchain");
//            String cmd = graalvmHome + "/bin/gu --jvm install llvm-toolchain";
//            logger.info("install llvm toolchain by cmd:{}", cmd);
//            int result = new JavaRuntimeCommandExecutor().execute(cmd);
//            if (result != 0) {
//                logger.error("llvm install failed, use command:{}", cmd);
//                return false;
//            }
//            logger.info("llvm install success, use command:{}", cmd);
//        }
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
