package com.xingray.graalvm.compiler.core.configuration;

import java.util.List;

public class CommonConfiguration {
    private String graalvmHome;
    private String mainClass;
    private String javafxPath;
    private List<String> classPathList;
    private List<String> bundlesList;
    private List<String> resourcesList;
    private List<String> reflectionList;
    private List<String> jniList;
    private List<String> compilerArgs;
    private List<String> linkerArgs;
    private List<String> runtimeArgs;

    public String getJavafxPath() {
        return javafxPath;
    }

    public void setJavafxPath(String javafxPath) {
        this.javafxPath = javafxPath;
    }

    public String getGraalvmHome() {
        return graalvmHome;
    }

    public void setGraalvmHome(String graalvmHome) {
        this.graalvmHome = graalvmHome;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
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
            sb.append("\"graalvmHome\":\"").append(graalvmHome).append("\",");
        }
        if (mainClass != null) {
            sb.append("\"mainClass\":\"").append(mainClass).append("\",");
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
