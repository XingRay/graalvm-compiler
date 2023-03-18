package com.xingray.graalvm.compiler.core.configuration;

public class LinuxConfiguration {

    //    @Parameter(property = Constants.PLUGIN_NAME + ".enableSWRendering", defaultValue = "false")
    private String enableSWRendering;

    public static class Release{
        //    @Parameter(property = Constants.PLUGIN_NAME + ".remoteHostName")
        private String remoteHostName;

        //    @Parameter(property = Constants.PLUGIN_NAME + ".remoteDir")
        private String remoteDir;
    }
}
