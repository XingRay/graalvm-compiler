package com.xingray.graalvm.compiler.core.adapter;

import com.xingray.graalvm.compiler.common.CommonArgument;
import com.xingray.graalvm.compiler.common.target.AbstractTargetConfiguration;
import com.xingray.graalvm.compiler.core.*;
import com.xingray.graalvm.compiler.core.configuration.CommonConfiguration;
import com.xingray.java.base.interfaces.Matcher;
import com.xingray.java.util.FileUtil;
import com.xingray.java.util.StringUtil;
import com.xingray.java.util.collection.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CommonAdapter {

    private final Logger logger = LoggerFactory.getLogger(CommonAdapter.class);

    public CommonArgument createCommonArgument(CompileConfiguration compileConfiguration) {
        logger.info("createCommonArgument:{}", compileConfiguration);

        CommonConfiguration configuration = compileConfiguration.getCommonConfiguration();

        ProjectConfiguration projectConfiguration = compileConfiguration.getCommonProjectConfiguration();
        String projectRootPath = projectConfiguration.getProjectRootPath();
        Path projectRoot = Path.of(projectRootPath).toAbsolutePath();

        CommonArgument argument = new CommonArgument();

        argument.setProjectRoot(projectRoot);
        Path target = projectRoot.resolve(Constants.TARGET_PATH);
        argument.setTargetPath(target);
        argument.setClassesPath(target.resolve(Constants.CLASSES_PATH));
        argument.setOutputRootPath(target.resolve(Constants.OUTPUT_PATH));

        List<String> classPathList = new ArrayList<>();
        classPathList.add(argument.getClassesPath().toString());
        argument.setClassPathList(classPathList);
        classPathList.add(argument.getClassesPath().toString());

        List<String> configurationClassPathList = configuration.getClassPathList();
        if (configurationClassPathList != null) {
            classPathList.addAll(configurationClassPathList);
        }
//        private String graalvmHome;
//
//        private String mainClass;
//        private List<String> classPathList;
//        private List<String> bundlesList;
//        private List<String> resourcesList;
//        private List<String> reflectionList;
//        private List<String> jniList;
//        private List<String> compilerArgs;
//        private List<String> linkerArgs;
//        private List<String> runtimeArgs;
        argument.setGraalvmHome(Path.of(configuration.getGraalvmHome()));
        argument.setMainClass(configuration.getMainClass());
        argument.setBundlesList(configuration.getBundlesList());
        argument.setReflectionList(configuration.getResourcesList());
        argument.setJniList(configuration.getJniList());
        argument.setCompilerArgs(configuration.getCompilerArgs());
        argument.setLinkerArgs(configuration.getLinkerArgs());
        argument.setRuntimeArgs(configuration.getRuntimeArgs());


        BuildSystem buildSystem = compileConfiguration.getBuildSystem();
        switch (buildSystem) {
            case MAVEN -> {
                MavenProjectConfiguration mavenProjectConfiguration = compileConfiguration.getMavenProjectConfiguration();
                File pomFile = mavenProjectConfiguration.getPomFile();
                MavenParser mavenParser = new MavenParser(pomFile);

                List<String> classPathListOfDependencies = mavenParser.parseClassPathListOfDependencies();
                if (classPathListOfDependencies != null) {
                    classPathList.addAll(classPathListOfDependencies);
                }
            }

            case GRADLE -> {
                logger.info("gradle not support yet");
            }
            default -> {
                logger.info("none build system not support yet");
            }
        }

        List<String> javafxClassPathList = CollectionUtil.findAll(classPathList, new Matcher<String>() {
            @Override
            public boolean isMatch(String s) {
                return s.contains("javafx");
            }
        });
        if (!CollectionUtil.isEmpty(javafxClassPathList)) {
            logger.info("javafx detected in classpath list:{}", StringUtil.toString(javafxClassPathList));
            String javafxPath = configuration.getJavafxPath();
            logger.info("javafxPath:{}", javafxPath);
            if (StringUtil.isEmpty(javafxPath)) {
                logger.error("use javafx, but javafx path not set");
                throw new IllegalArgumentException("use javafx, but javafx path not set");
            }
            if (!FileUtil.isFolderExist(javafxPath)) {
                logger.error("javafx path is not exist or not a dir : {}", javafxPath);
                throw new IllegalArgumentException("javafx path is not exist or not a dir : " + javafxPath);
            }
            Path path = Path.of(javafxPath);
            Path libPath = path.resolve(Constants.JAVAFX_LIB_PATH);
            if (!Files.exists(libPath) || !Files.isDirectory(libPath)) {
                logger.error("javafx path is not exist or not a dir : {}", javafxPath);
                throw new IllegalArgumentException("javafx path is not exist or not a dir : " + javafxPath);
            }
            if (!Files.exists(libPath.resolve("javafx.base.jar")) ||
                    !Files.exists(libPath.resolve("javafx.graphics.jar"))) {
                logger.error("javafx path error, javafx.base.jar and javafx.graphics.jar not found in " + libPath);
                throw new IllegalArgumentException("");
            }

            argument.setJavafxPath(path);
        }


        String graalvmBackend = System.getenv(Constants.GRAALVM_BACKEND);
        if (!StringUtil.isEmpty(graalvmBackend)) {
            argument.setCompilerBackend(graalvmBackend);
        }

        String graalvmCompilerClassPath = "";
        try {
            graalvmCompilerClassPath = new File(GraalvmCompiler.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
        logger.info("graalvmCompilerClassPath:" + graalvmCompilerClassPath);

        logger.info("argument:{}", argument);
        return argument;
    }
}
