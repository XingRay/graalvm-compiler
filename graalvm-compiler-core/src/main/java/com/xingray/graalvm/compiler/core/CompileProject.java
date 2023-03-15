package com.xingray.graalvm.compiler.core;

import com.xingray.graalvm.compiler.common.Platform;

public class CompileProject {

    private Platform targetPlatform;
    private String graalvmHome;


    public Platform getTargetPlatform() {
        return targetPlatform;
    }

    public void setTargetPlatform(Platform targetPlatform) {
        this.targetPlatform = targetPlatform;
    }

    public String getGraalvmHome() {
        return graalvmHome;
    }

    public void setGraalvmHome(String graalvmHome) {
        this.graalvmHome = graalvmHome;
    }

    @Override
    public String toString() {
        return "CompileProject{" +
                "targetPlatform=" + targetPlatform +
                ", graalvmHome='" + graalvmHome + '\'' +
                '}';
    }
}
