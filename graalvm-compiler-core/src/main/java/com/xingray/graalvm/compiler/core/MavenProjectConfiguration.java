package com.xingray.graalvm.compiler.core;

import java.io.File;

public class MavenProjectConfiguration {
    private File pomFile;

    public File getPomFile() {
        return pomFile;
    }

    public void setPomFile(File pomFile) {
        this.pomFile = pomFile;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        if (pomFile != null) {
            sb.append("\"pomFile\":").append(pomFile).append(",");
        }
        if (sb.lastIndexOf(",") != -1)
            sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append('}');
        return sb.toString();
    }
}
