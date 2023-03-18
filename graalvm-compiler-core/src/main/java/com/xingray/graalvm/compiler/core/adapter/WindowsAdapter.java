package com.xingray.graalvm.compiler.core.adapter;

import com.xingray.graalvm.compiler.common.CommonArgument;
import com.xingray.graalvm.compiler.core.*;
import com.xingray.graalvm.compiler.core.configuration.WindowsConfiguration;
import com.xingray.graalvm.compiler.windows.WindowsArgument;
import com.xingray.java.base.interfaces.IndexMapper;
import com.xingray.java.util.collection.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class WindowsAdapter {

    private final Logger logger = LoggerFactory.getLogger(WindowsAdapter.class);

    public WindowsArgument createWindowsCompileArgument(CompileConfiguration compileConfiguration) {
        logger.info("createWindowsCompileArgument, compileArgument:" + compileConfiguration);
        WindowsArgument windowsArgument = new WindowsArgument();

        BuildSystem buildSystem = compileConfiguration.getBuildSystem();
        logger.info("buildSystem:{}", buildSystem);

        CommonArgument commonArgument = new CommonAdapter().createCommonArgument(compileConfiguration);
        windowsArgument.setCommonArgument(commonArgument);

        WindowsConfiguration configuration = compileConfiguration.getWindowsConfiguration();
        windowsArgument.setReleaseList(CollectionUtil.convert(configuration.getReleaseList(), new IndexMapper<WindowsConfiguration.Release, WindowsArgument.Release>() {
            @Override
            public WindowsArgument.Release map(int i, WindowsConfiguration.Release release) {
                logger.info("WindowsConfiguration.Release:{}", release);
                WindowsArgument.Release argumentRelease = new WindowsArgument.Release();
                argumentRelease.setId(release.getId());
                argumentRelease.setName(release.getName());
                argumentRelease.setDescription(release.getDescription());
                argumentRelease.setPackageType(release.getPackageType());
                argumentRelease.setVendor(release.getVendor());
                argumentRelease.setVersionCode(release.getVersionCode());
                argumentRelease.setVersionName(release.getVersionName());
                logger.info("WindowsArgument.Release:{}", argumentRelease);
                return argumentRelease;
            }
        }));

        windowsArgument.setPlatformOutputPath(commonArgument.getOutputRootPath().resolve(Constants.WINDOWS_PATH));

        logger.info("windowsArgument:{}", windowsArgument);
        return windowsArgument;
    }
}
