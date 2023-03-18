package com.xingray.graalvm.compiler.common;

import com.xingray.graalvm.compiler.common.model.ProcessPaths;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class CommonArgument {
    private Path graalvmHome;
    private Path projectRoot;
    private Path targetPath;
    private Path classesPath;
    private Path outputRootPath;

    private Path javaLibPath;
    private Path javafxPath;

    private boolean skipSigning;
    private String simulatorDevice;
    private String appLabel;
    private String versionCode;
    private String versionName;
    private String providedKeyStorePath;
    private String providedKeyStorePassword;
    private String providedKeyAlias;
    private String providedKeyAliasPassword;
    private ProcessPaths paths;
    private String javaStaticLibs;
    private String javaFXStaticSDK;
    private String mainClass;

    private String javaVersion;
    private String javafxVersion;
    private boolean usePrismSW;
    private boolean usePrecompiledCode;
    private String compilerBackend;

    private List<String> classPathList;

    private List<String> bundlesList = Collections.emptyList();
    private List<String> resourcesList = Collections.emptyList();
    private List<String> reflectionList = Collections.emptyList();
    private List<String> jniList = Collections.emptyList();
    private List<String> compilerArgs = Collections.emptyList();
    private List<String> linkerArgs = Collections.emptyList();
    private List<String> runtimeArgs = Collections.emptyList();

    public Path getProjectRoot() {
        return projectRoot;
    }

    public void setProjectRoot(Path projectRoot) {
        this.projectRoot = projectRoot;
    }

    public Path getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(Path targetPath) {
        this.targetPath = targetPath;
    }

    public Path getClassesPath() {
        return classesPath;
    }

    public void setClassesPath(Path classesPath) {
        this.classesPath = classesPath;
    }

    public Path getOutputRootPath() {
        return outputRootPath;
    }

    public void setOutputRootPath(Path outputRootPath) {
        this.outputRootPath = outputRootPath;
    }

    public boolean isSkipSigning() {
        return skipSigning;
    }

    public void setSkipSigning(boolean skipSigning) {
        this.skipSigning = skipSigning;
    }

    public String getSimulatorDevice() {
        return simulatorDevice;
    }

    public void setSimulatorDevice(String simulatorDevice) {
        this.simulatorDevice = simulatorDevice;
    }

    public String getAppLabel() {
        return appLabel;
    }

    public void setAppLabel(String appLabel) {
        this.appLabel = appLabel;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getProvidedKeyStorePath() {
        return providedKeyStorePath;
    }

    public void setProvidedKeyStorePath(String providedKeyStorePath) {
        this.providedKeyStorePath = providedKeyStorePath;
    }

    public String getProvidedKeyStorePassword() {
        return providedKeyStorePassword;
    }

    public void setProvidedKeyStorePassword(String providedKeyStorePassword) {
        this.providedKeyStorePassword = providedKeyStorePassword;
    }

    public String getProvidedKeyAlias() {
        return providedKeyAlias;
    }

    public void setProvidedKeyAlias(String providedKeyAlias) {
        this.providedKeyAlias = providedKeyAlias;
    }

    public String getProvidedKeyAliasPassword() {
        return providedKeyAliasPassword;
    }

    public void setProvidedKeyAliasPassword(String providedKeyAliasPassword) {
        this.providedKeyAliasPassword = providedKeyAliasPassword;
    }

    public Path getJavaLibPath() {
        return javaLibPath;
    }

    public void setJavaLibPath(Path javaLibPath) {
        this.javaLibPath = javaLibPath;
    }

    public Path getJavafxPath() {
        return javafxPath;
    }

    public void setJavafxPath(Path javafxPath) {
        this.javafxPath = javafxPath;
    }

    public ProcessPaths getPaths() {
        return paths;
    }

    public void setPaths(ProcessPaths paths) {
        this.paths = paths;
    }

    public String getJavaStaticLibs() {
        return javaStaticLibs;
    }

    public void setJavaStaticLibs(String javaStaticLibs) {
        this.javaStaticLibs = javaStaticLibs;
    }

    public String getJavaFXStaticSDK() {
        return javaFXStaticSDK;
    }

    public void setJavaFXStaticSDK(String javaFXStaticSDK) {
        this.javaFXStaticSDK = javaFXStaticSDK;
    }

    public Path getGraalvmHome() {
        return graalvmHome;
    }

    public void setGraalvmHome(Path graalvmHome) {
        this.graalvmHome = graalvmHome;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public String getJavafxVersion() {
        return javafxVersion;
    }

    public void setJavafxVersion(String javafxVersion) {
        this.javafxVersion = javafxVersion;
    }

    public boolean isUsePrismSW() {
        return usePrismSW;
    }

    public void setUsePrismSW(boolean usePrismSW) {
        this.usePrismSW = usePrismSW;
    }

    public boolean isUsePrecompiledCode() {
        return usePrecompiledCode;
    }

    public void setUsePrecompiledCode(boolean usePrecompiledCode) {
        this.usePrecompiledCode = usePrecompiledCode;
    }

    public String getCompilerBackend() {
        return compilerBackend;
    }

    public void setCompilerBackend(String compilerBackend) {
        this.compilerBackend = compilerBackend;
    }

    public List<String> getClassPathList() {
        return classPathList;
    }

    public void setClassPathList(List<String> classPathList) {
        this.classPathList = classPathList;
    }

    public List<String> getBundlesList() {
        return bundlesList;
    }

    public void setBundlesList(List<String> bundlesList) {
        this.bundlesList = bundlesList;
    }

    public List<String> getResourcesList() {
        return resourcesList;
    }

    public void setResourcesList(List<String> resourcesList) {
        this.resourcesList = resourcesList;
    }

    public List<String> getReflectionList() {
        return reflectionList;
    }

    public void setReflectionList(List<String> reflectionList) {
        this.reflectionList = reflectionList;
    }

    public List<String> getJniList() {
        return jniList;
    }

    public void setJniList(List<String> jniList) {
        this.jniList = jniList;
    }

    public List<String> getCompilerArgs() {
        return compilerArgs;
    }

    public void setCompilerArgs(List<String> compilerArgs) {
        this.compilerArgs = compilerArgs;
    }

    public List<String> getLinkerArgs() {
        return linkerArgs;
    }

    public void setLinkerArgs(List<String> linkerArgs) {
        this.linkerArgs = linkerArgs;
    }

    public List<String> getRuntimeArgs() {
        return runtimeArgs;
    }

    public void setRuntimeArgs(List<String> runtimeArgs) {
        this.runtimeArgs = runtimeArgs;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        if (graalvmHome != null) {
            sb.append("\"graalvmHome\":").append(graalvmHome).append(",");
        }
        if (projectRoot != null) {
            sb.append("\"projectRoot\":").append(projectRoot).append(",");
        }
        if (targetPath != null) {
            sb.append("\"targetPath\":").append(targetPath).append(",");
        }
        if (classesPath != null) {
            sb.append("\"classesPath\":").append(classesPath).append(",");
        }
        if (outputRootPath != null) {
            sb.append("\"outputRootPath\":").append(outputRootPath).append(",");
        }
        if (javaLibPath != null) {
            sb.append("\"javaLibPath\":").append(javaLibPath).append(",");
        }
        if (javafxPath != null) {
            sb.append("\"javafxLibPath\":").append(javafxPath).append(",");
        }
        sb.append("\"skipSigning\":").append(skipSigning).append(",");
        if (simulatorDevice != null) {
            sb.append("\"simulatorDevice\":\"").append(simulatorDevice).append("\",");
        }
        if (appLabel != null) {
            sb.append("\"appLabel\":\"").append(appLabel).append("\",");
        }
        if (versionCode != null) {
            sb.append("\"versionCode\":\"").append(versionCode).append("\",");
        }
        if (versionName != null) {
            sb.append("\"versionName\":\"").append(versionName).append("\",");
        }
        if (providedKeyStorePath != null) {
            sb.append("\"providedKeyStorePath\":\"").append(providedKeyStorePath).append("\",");
        }
        if (providedKeyStorePassword != null) {
            sb.append("\"providedKeyStorePassword\":\"").append(providedKeyStorePassword).append("\",");
        }
        if (providedKeyAlias != null) {
            sb.append("\"providedKeyAlias\":\"").append(providedKeyAlias).append("\",");
        }
        if (providedKeyAliasPassword != null) {
            sb.append("\"providedKeyAliasPassword\":\"").append(providedKeyAliasPassword).append("\",");
        }
        if (paths != null) {
            sb.append("\"paths\":").append(paths).append(",");
        }
        if (javaStaticLibs != null) {
            sb.append("\"javaStaticLibs\":\"").append(javaStaticLibs).append("\",");
        }
        if (javaFXStaticSDK != null) {
            sb.append("\"javaFXStaticSDK\":\"").append(javaFXStaticSDK).append("\",");
        }
        if (mainClass != null) {
            sb.append("\"mainClass\":\"").append(mainClass).append("\",");
        }
        if (javaVersion != null) {
            sb.append("\"javaVersion\":\"").append(javaVersion).append("\",");
        }
        if (javafxVersion != null) {
            sb.append("\"javafxVersion\":\"").append(javafxVersion).append("\",");
        }
        sb.append("\"usePrismSW\":").append(usePrismSW).append(",");
        sb.append("\"usePrecompiledCode\":").append(usePrecompiledCode).append(",");
        if (compilerBackend != null) {
            sb.append("\"compilerBackend\":\"").append(compilerBackend).append("\",");
        }
        if (classPathList != null) {
            sb.append("\"classPathList\":").append(classPathList).append(",");
        }
        if (bundlesList != null) {
            sb.append("\"bundlesList\":").append(bundlesList).append(",");
        }
        if (resourcesList != null) {
            sb.append("\"resourcesList\":").append(resourcesList).append(",");
        }
        if (reflectionList != null) {
            sb.append("\"reflectionList\":").append(reflectionList).append(",");
        }
        if (jniList != null) {
            sb.append("\"jniList\":").append(jniList).append(",");
        }
        if (compilerArgs != null) {
            sb.append("\"compilerArgs\":").append(compilerArgs).append(",");
        }
        if (linkerArgs != null) {
            sb.append("\"linkerArgs\":").append(linkerArgs).append(",");
        }
        if (runtimeArgs != null) {
            sb.append("\"runtimeArgs\":").append(runtimeArgs).append(",");
        }
        if (sb.lastIndexOf(",") != -1)
            sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append('}');
        return sb.toString();
    }
}
