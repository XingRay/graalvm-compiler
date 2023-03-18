package com.xingray.graalvm.compiler.core;

public class ProjectConfiguration {
    private String projectRootPath;

    public String getProjectRootPath() {
        return projectRootPath;
    }

    public void setProjectRootPath(String projectRootPath) {
        this.projectRootPath = projectRootPath;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        if (projectRootPath != null) {
            sb.append("\"projectRootPath\":\"").append(projectRootPath).append("\",");
        }
        if (sb.lastIndexOf(",") != -1)
            sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append('}');
        return sb.toString();
    }
}
