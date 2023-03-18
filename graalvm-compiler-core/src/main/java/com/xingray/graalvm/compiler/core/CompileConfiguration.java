package com.xingray.graalvm.compiler.core;

import com.xingray.graalvm.compiler.common.Platform;
import com.xingray.graalvm.compiler.core.configuration.*;

public class CompileConfiguration {

    private Platform targetPlatform;

    private CommonConfiguration commonConfiguration;

    private WindowsConfiguration windowsConfiguration;
    private LinuxConfiguration linuxConfiguration;
    private MacosConfiguration macosConfiguration;
    private WebConfiguration webConfiguration;
    private IosConfiguration iosConfiguration;
    private AndroidConfiguration androidConfiguration;

    private ProjectConfiguration projectConfiguration;
    private BuildSystem buildSystem;
    private MavenProjectConfiguration mavenProjectConfiguration;
    private GradleProjectConfiguration gradleProjectConfiguration;

    public Platform getTargetPlatform() {
        return targetPlatform;
    }

    public void setTargetPlatform(Platform targetPlatform) {
        this.targetPlatform = targetPlatform;
    }

    public CommonConfiguration getCommonConfiguration() {
        return commonConfiguration;
    }

    public void setCommonConfiguration(CommonConfiguration commonConfiguration) {
        this.commonConfiguration = commonConfiguration;
    }

    public WindowsConfiguration getWindowsConfiguration() {
        return windowsConfiguration;
    }

    public void setWindowsConfiguration(WindowsConfiguration windowsConfiguration) {
        this.windowsConfiguration = windowsConfiguration;
    }

    public LinuxConfiguration getLinuxConfiguration() {
        return linuxConfiguration;
    }

    public void setLinuxConfiguration(LinuxConfiguration linuxConfiguration) {
        this.linuxConfiguration = linuxConfiguration;
    }

    public MacosConfiguration getMacosConfiguration() {
        return macosConfiguration;
    }

    public void setMacosConfiguration(MacosConfiguration macosConfiguration) {
        this.macosConfiguration = macosConfiguration;
    }

    public WebConfiguration getWebConfiguration() {
        return webConfiguration;
    }

    public void setWebConfiguration(WebConfiguration webConfiguration) {
        this.webConfiguration = webConfiguration;
    }

    public IosConfiguration getIosConfiguration() {
        return iosConfiguration;
    }

    public void setIosConfiguration(IosConfiguration iosConfiguration) {
        this.iosConfiguration = iosConfiguration;
    }

    public AndroidConfiguration getAndroidConfiguration() {
        return androidConfiguration;
    }

    public void setAndroidConfiguration(AndroidConfiguration androidConfiguration) {
        this.androidConfiguration = androidConfiguration;
    }

    public ProjectConfiguration getCommonProjectConfiguration() {
        return projectConfiguration;
    }

    public void setCommonProjectConfiguration(ProjectConfiguration projectConfiguration) {
        this.projectConfiguration = projectConfiguration;
    }

    public BuildSystem getBuildSystem() {
        return buildSystem;
    }

    public void setBuildSystem(BuildSystem buildSystem) {
        this.buildSystem = buildSystem;
    }

    public MavenProjectConfiguration getMavenProjectConfiguration() {
        return mavenProjectConfiguration;
    }

    public void setMavenProjectConfiguration(MavenProjectConfiguration mavenProjectConfiguration) {
        this.mavenProjectConfiguration = mavenProjectConfiguration;
    }

    public GradleProjectConfiguration getGradleProjectConfiguration() {
        return gradleProjectConfiguration;
    }

    public void setGradleProjectConfiguration(GradleProjectConfiguration gradleProjectConfiguration) {
        this.gradleProjectConfiguration = gradleProjectConfiguration;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        if (targetPlatform != null) {
            sb.append("\"targetPlatform\":").append(targetPlatform).append(",");
        }
        if (commonConfiguration != null) {
            sb.append("\"commonConfiguration\":").append(commonConfiguration).append(",");
        }
        if (windowsConfiguration != null) {
            sb.append("\"windowsConfiguration\":").append(windowsConfiguration).append(",");
        }
        if (linuxConfiguration != null) {
            sb.append("\"linuxConfiguration\":").append(linuxConfiguration).append(",");
        }
        if (macosConfiguration != null) {
            sb.append("\"macosConfiguration\":").append(macosConfiguration).append(",");
        }
        if (webConfiguration != null) {
            sb.append("\"webConfiguration\":").append(webConfiguration).append(",");
        }
        if (iosConfiguration != null) {
            sb.append("\"iosConfiguration\":").append(iosConfiguration).append(",");
        }
        if (androidConfiguration != null) {
            sb.append("\"androidConfiguration\":").append(androidConfiguration).append(",");
        }
        if (projectConfiguration != null) {
            sb.append("\"commonProjectConfiguration\":").append(projectConfiguration).append(",");
        }
        if (buildSystem != null) {
            sb.append("\"buildSystem\":").append(buildSystem).append(",");
        }
        if (mavenProjectConfiguration != null) {
            sb.append("\"mavenProjectConfiguration\":").append(mavenProjectConfiguration).append(",");
        }
        if (gradleProjectConfiguration != null) {
            sb.append("\"gradleProjectConfiguration\":").append(gradleProjectConfiguration).append(",");
        }
        if (sb.lastIndexOf(",") != -1)
            sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append('}');
        return sb.toString();
    }
}
