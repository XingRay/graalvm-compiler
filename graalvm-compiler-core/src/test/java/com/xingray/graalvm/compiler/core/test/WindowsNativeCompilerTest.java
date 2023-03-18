package com.xingray.graalvm.compiler.core.test;

import com.xingray.graalvm.compiler.common.CompilerException;
import com.xingray.graalvm.compiler.common.Platform;
import com.xingray.graalvm.compiler.core.*;
import com.xingray.graalvm.compiler.core.configuration.CommonConfiguration;
import com.xingray.graalvm.compiler.core.configuration.WindowsConfiguration;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class WindowsNativeCompilerTest {
    @Test
    public void testCompile() {
        CompileConfiguration compileConfiguration = new CompileConfiguration();
        compileConfiguration.setTargetPlatform(Platform.WINDOWS);

        CommonConfiguration commonConfiguration = new CommonConfiguration();
        compileConfiguration.setCommonConfiguration(commonConfiguration);


        ProjectConfiguration project = new ProjectConfiguration();
        compileConfiguration.setCommonProjectConfiguration(project);

        String projectDir = "D:\\code\\demo\\javafx\\javafx-graalvm";
        project.setProjectRootPath(projectDir);

//        project.setProjectOutputPath(projectDir + "\\" + Constants.buildPathRelativeToProject + "\\" + Constants.nativeBuildPathRelativeToBuild);

        commonConfiguration.setMainClass("com.xingray.javafx.graalvm.Launcher");
        commonConfiguration.setClassPathList(Arrays.asList("output/dependency", "target/classes"));
        commonConfiguration.setGraalvmHome("D:\\develop\\java\\jdk\\graalvm\\19\\graalvm-ce-java19-22.3.1");
        commonConfiguration.setJavafxPath("D:\\develop\\java\\javafx\\19\\javafx-sdk-19.0.2.1");
        WindowsConfiguration windowsConfiguration = new WindowsConfiguration();
        compileConfiguration.setWindowsConfiguration(windowsConfiguration);
        windowsConfiguration.setEnableSWRendering(false);

        WindowsConfiguration.Release release = new WindowsConfiguration.Release();
        windowsConfiguration.setReleaseList(List.of(release));

        release.setPackageType("image");
        release.setDescription("graalvm demo");
        release.setVendor("xingray");
        release.setVersionName("1.0.0");
        release.setId("com.xingray.graalvm-demo");
        release.setName("launcher");

        compileConfiguration.setBuildSystem(BuildSystem.MAVEN);

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
    }

}
