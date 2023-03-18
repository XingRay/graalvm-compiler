package com.xingray.graalvm.compiler.windows;

import com.xingray.graalvm.compiler.common.CommonArgument;
import com.xingray.graalvm.compiler.common.CompilerException;
import com.xingray.graalvm.compiler.common.Constants;
import com.xingray.graalvm.compiler.common.NativeCompiler;
import com.xingray.graalvm.compiler.common.model.ClassPath;
import com.xingray.graalvm.compiler.common.util.FileOps;
import com.xingray.java.util.StringUtil;
import com.xingray.java.util.collection.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class WindowsNativeCompiler implements NativeCompiler {

    private final WindowsArgument argument;
    private final Logger logger = LoggerFactory.getLogger(WindowsNativeCompiler.class);

    public WindowsNativeCompiler(WindowsArgument argument) {
        this.argument = argument;
        logger.info("argumentL{}", argument);
    }

    @Override
    public int prepare() throws CompilerException {
        logger.info("prepare()");
        try {
            extractNativeLibs();
        } catch (IOException e) {
            throw new CompilerException(e);
        }

        return 0;
    }

    @Override
    public int executeCompile() throws CompilerException {
        logger.info("executeCompile()");
        return 0;
    }

    @Override
    public int executeLink() throws CompilerException {
        logger.info("executeLink()");
        return 0;
    }

    @Override
    public int executePackage() throws CompilerException {
        logger.info("executePackage()");
        return 0;
    }

    @Override
    public int executeInstall() throws CompilerException {
        logger.info("executeInstall()");
        return 0;
    }

    @Override
    public int executeRun() throws CompilerException {
        logger.info("executeRun()");
        return 0;
    }


    private void extractNativeLibs() throws IOException {
        logger.info("extractNativeLibs()");
        CommonArgument commonArgument = argument.getCommonArgument();
        List<String> classPathList = commonArgument.getClassPathList();

        Path platformOutputPath = argument.getPlatformOutputPath();
        Path libPath = platformOutputPath.resolve(Constants.LIB_PATH);
        logger.info("libPath:{}", libPath);

        if (Files.exists(libPath)) {
            logger.info("lib dir already exist, delete it");
            FileOps.deleteDirectory(libPath);
        }

        List<String> jars = classPathList.stream().filter((s -> s.endsWith(".jar") && !s.contains("javafx-"))).toList();
        logger.info("jars:{}", StringUtil.toString(jars));

        if(CollectionUtil.isEmpty(jars)){
            logger.info("no jar file found, do nothing");
            return;
        }
        for (String jar : jars) {
            FileOps.extractFilesFromJar("." + getStaticLibraryFileExtension(), Path.of(jar),
                    libPath, getTargetSpecificNativeLibsFilter());
        }
    }
}
