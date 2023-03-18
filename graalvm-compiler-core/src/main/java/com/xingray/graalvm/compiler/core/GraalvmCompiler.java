package com.xingray.graalvm.compiler.core;

import com.xingray.graalvm.compiler.android.AndroidNativeCompiler;
import com.xingray.graalvm.compiler.common.CompilerException;
import com.xingray.graalvm.compiler.common.NativeCompiler;
import com.xingray.graalvm.compiler.common.Platform;
import com.xingray.graalvm.compiler.core.adapter.LinuxAdapter;
import com.xingray.graalvm.compiler.core.adapter.WindowsAdapter;
import com.xingray.graalvm.compiler.core.configuration.CommonConfiguration;
import com.xingray.graalvm.compiler.ios.IosNativeCompiler;
import com.xingray.graalvm.compiler.linux.LinuxArgument;
import com.xingray.graalvm.compiler.linux.LinuxNativeCompiler;
import com.xingray.graalvm.compiler.macos.MacosNativeCompiler;
import com.xingray.graalvm.compiler.web.WebNativeCompiler;
import com.xingray.graalvm.compiler.windows.WindowsArgument;
import com.xingray.graalvm.compiler.windows.WindowsNativeCompiler;
import com.xingray.java.util.FileUtil;
import com.xingray.java.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class GraalvmCompiler implements NativeCompiler {

    private final NativeCompiler nativeCompiler;

    private final Logger logger = LoggerFactory.getLogger(GraalvmCompiler.class);

    public GraalvmCompiler(CompileConfiguration compileConfiguration) {
        checkCompileArgument(compileConfiguration);
        this.nativeCompiler = createNativeCompiler(compileConfiguration);
    }

    @Override
    public int prepare() throws CompilerException {
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

    private NativeCompiler createNativeCompiler(CompileConfiguration compileConfiguration) {
        Platform targetPlatform = compileConfiguration.getTargetPlatform();
        return switch (targetPlatform) {
            case WINDOWS -> {
                WindowsArgument windowsArgument = new WindowsAdapter().createWindowsCompileArgument(compileConfiguration);
                yield new WindowsNativeCompiler(windowsArgument);
            }
            case LINUX -> {
                LinuxArgument linuxArgument = new LinuxAdapter().createLinuxCompileArgument(compileConfiguration);
                yield new LinuxNativeCompiler(linuxArgument);
            }
            case MACOS -> new MacosNativeCompiler();
            case WEB -> new WebNativeCompiler();
            case ANDROID -> new AndroidNativeCompiler();
            case IOS -> new IosNativeCompiler();
        };
    }

    /**
     * 检测参数，可以为空的参数在这里补上默认值
     */
    private void checkCompileArgument(CompileConfiguration compileConfiguration) {
        logger.info("createNativeCompiler, compileArgument:" + compileConfiguration);

        if (compileConfiguration == null) {
            logger.error("compileArgument is null");
            throw new IllegalArgumentException("compileProject can not be null");
        }
        Platform targetPlatform = compileConfiguration.getTargetPlatform();
        logger.info("targetPlatform:{}", targetPlatform);
        if (targetPlatform == null) {
            logger.error("targetPlatform is null");
            throw new IllegalArgumentException("targetPlatform is not set in compileProject:" + compileConfiguration);
        }

        BuildSystem buildSystem = compileConfiguration.getBuildSystem();
        logger.info("buildSystem:{}", buildSystem);
        if (buildSystem == null) {
            logger.error("buildSystem is null, if do not have build system, set BuildSystem.NONE");
            throw new IllegalArgumentException("buildSystem is null, if do not have build system, set BuildSystem.NONE");
        }

        ProjectConfiguration projectConfiguration = compileConfiguration.getCommonProjectConfiguration();
        if (projectConfiguration == null) {
            logger.error("commonProjectConfiguration is null");
            throw new IllegalArgumentException("commonProjectConfiguration is null");
        }

        CommonConfiguration commonConfiguration = compileConfiguration.getCommonConfiguration();
        if (commonConfiguration == null) {
            logger.error("commonConfiguration is null");
            throw new IllegalArgumentException("commonConfiguration is null");
        }

        checkGraalvmHome(commonConfiguration);
        checkMainClass(compileConfiguration.getCommonConfiguration().getMainClass());

        checkProjectConfiguration(projectConfiguration);
        switch (buildSystem) {
            case MAVEN -> {
                MavenProjectConfiguration mavenProjectConfiguration = compileConfiguration.getMavenProjectConfiguration();
                if (mavenProjectConfiguration == null) {
                    logger.info("mavenProjectConfiguration is null, use default mavenProjectConfiguration");
                    mavenProjectConfiguration = new MavenProjectConfiguration();
                    compileConfiguration.setMavenProjectConfiguration(mavenProjectConfiguration);
                }
                checkMavenProjectConfiguration(projectConfiguration.getProjectRootPath(), mavenProjectConfiguration);
            }
            case GRADLE -> checkGradleProjectConfiguration(compileConfiguration.getGradleProjectConfiguration());
            default -> {
            }
        }
    }

    private void checkProjectConfiguration(ProjectConfiguration projectConfiguration) {
        logger.info("checkProjectDir({})", projectConfiguration);

        String projectRootPath = projectConfiguration.getProjectRootPath();

        if (!FileUtil.isFolderExist(projectRootPath)) {
            logger.error("project dir not exist: " + projectRootPath);
            throw new IllegalArgumentException("project dir not exist: " + projectRootPath);
        }
        logger.info("checkProjectConfiguration ok");
    }

    public void checkGraalvmHome(CommonConfiguration commonConfiguration) {
        String graalvmHome = commonConfiguration.getGraalvmHome();
        logger.info("checkGraalvmHome({})", graalvmHome);

        if (StringUtil.isEmpty(graalvmHome)) {
            logger.info("graalvmHome is not set, use system environment variable GRAALVM_HOME");
            String systemGraalvmHome = System.getenv("GRAALVM_HOME");
            logger.info("system environment variable GRAALVM_HOME: " + systemGraalvmHome);
            if (!FileUtil.isFolderExist(systemGraalvmHome)) {
                logger.error("graalvmHome not found in configuration or environment variable GRAALVM_HOME");
                throw new IllegalArgumentException("graalvmHome not found in configuration or environment variable GRAALVM_HOME");
            }
            commonConfiguration.setGraalvmHome(systemGraalvmHome);
        } else {
            if (!FileUtil.isFolderExist(graalvmHome)) {
                logger.info("graalvmHome dir not found:{}", graalvmHome);
                throw new IllegalArgumentException("graalvmHome dir not found:" + graalvmHome);
            }
        }
        logger.info("checkGraalvmHome ok");
    }

    private void checkGradleProjectConfiguration(GradleProjectConfiguration configuration) {
        logger.info("checkGradleProjectConfiguration:{}", configuration);
        logger.info("checkGradleProjectConfiguration ok");
    }

    private void checkMavenProjectConfiguration(String projectRootPath, MavenProjectConfiguration configuration) {
        logger.info("checkMavenProjectConfiguration:{}", configuration);
        File pomFile = configuration.getPomFile();
        logger.info("pomFile:{}", pomFile);
        if (pomFile == null) {
            File defaultPomFile = new File(projectRootPath, "pom.xml");
            if (defaultPomFile.exists()) {
                logger.info("pomFile not set, use default pom file:{}", defaultPomFile.getAbsolutePath());
                configuration.setPomFile(defaultPomFile);
            } else {
                throw new IllegalArgumentException("pomFile is null, and default pom file not exist:" + defaultPomFile.getAbsolutePath());
            }
        } else {
            if (!pomFile.exists()) {
                logger.error("pomFile not found:" + pomFile);
                throw new IllegalArgumentException("pomFile not found:" + pomFile);
            }
        }
        logger.info("checkMavenProjectConfiguration ok");
    }

    private void checkMainClass(String mainClass) {
        logger.info("checkMainClass:{}", mainClass);
        if(StringUtil.isEmpty(mainClass)){
            logger.error("mainClass is not set");
            throw new IllegalArgumentException("mainClass is not set");
        }
        logger.info("checkMainClass ok");
    }
}
