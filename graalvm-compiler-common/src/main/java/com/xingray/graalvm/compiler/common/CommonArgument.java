package com.xingray.graalvm.compiler.common;

import com.xingray.graalvm.compiler.common.model.ProcessPaths;

import java.util.Collections;
import java.util.List;

public class CommonArgument {
    private Compile compile;
    private Release release;

    public Compile getCompile() {
        return compile;
    }

    public void setCompile(Compile compile) {
        this.compile = compile;
    }

    public Release getRelease() {
        return release;
    }

    public void setRelease(Release release) {
        this.release = release;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        if (compile != null) {
            sb.append("\"compile\":").append(compile).append(",");
        }
        if (release != null) {
            sb.append("\"release\":").append(release).append(",");
        }
        if (sb.lastIndexOf(",") != -1)
            sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append('}');
        return sb.toString();
    }

    public static class Compile {
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

        private Platform targetPlatform;

        private Platform compilePlatform;

        private String graalvmHome;

        private String projectDir;

        private String outputDir;

        private String javaVersion;
        private String javafxVersion;
        private boolean usePrismSW;
        private boolean usePrecompiledCode;
        private String compilerBackend;

        private List<String> bundlesList = Collections.emptyList();
        private List<String> resourcesList = Collections.emptyList();
        private List<String> reflectionList = Collections.emptyList();
        private List<String> jniList = Collections.emptyList();
        private List<String> compilerArgs = Collections.emptyList();
        private List<String> linkerArgs = Collections.emptyList();
        private List<String> runtimeArgs = Collections.emptyList();

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

        public Platform getTargetPlatform() {
            return targetPlatform;
        }

        public void setTargetPlatform(Platform targetPlatform) {
            this.targetPlatform = targetPlatform;
        }

        public Platform getCompilePlatform() {
            return compilePlatform;
        }

        public void setCompilePlatform(Platform compilePlatform) {
            this.compilePlatform = compilePlatform;
        }

        public String getGraalvmHome() {
            return graalvmHome;
        }

        public void setGraalvmHome(String graalvmHome) {
            this.graalvmHome = graalvmHome;
        }

        public String getProjectDir() {
            return projectDir;
        }

        public void setProjectDir(String projectDir) {
            this.projectDir = projectDir;
        }

        public String getOutputDir() {
            return outputDir;
        }

        public void setOutputDir(String outputDir) {
            this.outputDir = outputDir;
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
            if (targetPlatform != null) {
                sb.append("\"targetPlatform\":").append(targetPlatform).append(",");
            }
            if (compilePlatform != null) {
                sb.append("\"compilePlatform\":").append(compilePlatform).append(",");
            }
            if (graalvmHome != null) {
                sb.append("\"graalvmHome\":\"").append(graalvmHome).append("\",");
            }
            if (projectDir != null) {
                sb.append("\"projectDir\":\"").append(projectDir).append("\",");
            }
            if (outputDir != null) {
                sb.append("\"outputDir\":\"").append(outputDir).append("\",");
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

    public static class Release {
        private String appId;
        private String appName;
        private String mainClassName;
        private String classpath;

        private String packageType;

        private String description;
        private String vendor;
        private String version;

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getMainClassName() {
            return mainClassName;
        }

        public void setMainClassName(String mainClassName) {
            this.mainClassName = mainClassName;
        }

        public String getClasspath() {
            return classpath;
        }

        public void setClasspath(String classpath) {
            this.classpath = classpath;
        }

        public String getPackageType() {
            return packageType;
        }

        public void setPackageType(String packageType) {
            this.packageType = packageType;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVendor() {
            return vendor;
        }

        public void setVendor(String vendor) {
            this.vendor = vendor;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("{");
            if (appId != null) {
                sb.append("\"appId\":\"").append(appId).append("\",");
            }
            if (appName != null) {
                sb.append("\"appName\":\"").append(appName).append("\",");
            }
            if (mainClassName != null) {
                sb.append("\"mainClassName\":\"").append(mainClassName).append("\",");
            }
            if (classpath != null) {
                sb.append("\"classpath\":\"").append(classpath).append("\",");
            }
            if (packageType != null) {
                sb.append("\"packageType\":\"").append(packageType).append("\",");
            }
            if (description != null) {
                sb.append("\"description\":\"").append(description).append("\",");
            }
            if (vendor != null) {
                sb.append("\"vendor\":\"").append(vendor).append("\",");
            }
            if (version != null) {
                sb.append("\"version\":\"").append(version).append("\",");
            }
            if (sb.lastIndexOf(",") != -1)
                sb.deleteCharAt(sb.lastIndexOf(","));
            sb.append('}');
            return sb.toString();
        }
    }
}
