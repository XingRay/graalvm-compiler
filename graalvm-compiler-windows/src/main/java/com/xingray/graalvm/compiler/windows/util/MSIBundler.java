package com.xingray.graalvm.compiler.windows.util;



import com.xingray.graalvm.compiler.common.Constants;
import com.xingray.graalvm.compiler.common.model.InternalProjectConfiguration;
import com.xingray.graalvm.compiler.common.model.ProcessPaths;
import com.xingray.graalvm.compiler.common.model.ReleaseConfiguration;
import com.xingray.graalvm.compiler.common.util.FileOps;
import com.xingray.graalvm.compiler.common.util.Logger;
import com.xingray.graalvm.compiler.common.util.ProcessRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MSIBundler {

    private final ProcessPaths paths;
    private final InternalProjectConfiguration projectConfiguration;
    private final String sourceOS;
    private final Path rootPath;

    public MSIBundler(ProcessPaths paths, InternalProjectConfiguration projectConfiguration) {
        this.paths = paths;
        this.projectConfiguration = projectConfiguration;
        this.sourceOS = projectConfiguration.getTargetTriplet().getOs();
        this.rootPath = paths.getSourcePath().resolve(sourceOS);
    }

    public boolean createPackage(boolean sign) throws IOException, InterruptedException {

        if (WixTool.CANDLE.getPath() == null || WixTool.LIGHT.getPath() == null) {
            Logger.logSevere("Please make sure to install Wix Toolset (https://wixtoolset.org/) before proceeding.");
            return false;
        }

        final String appName = projectConfiguration.getAppName();
        final String version = projectConfiguration.getReleaseConfiguration().getVersion();
        Path localAppPath = paths.getAppPath().resolve(appName + ".exe");
        if (!Files.exists(localAppPath)) {
            throw new IOException("Error: " + appName + ".exe not found");
        }
        Logger.logInfo("Building msi for " + localAppPath);

        /**
         * Create directories and copy resources
         */
        Path tmpMSI = paths.getTmpPath().resolve("tmpMSI");
        if (Files.exists(tmpMSI)) {
            FileOps.deleteDirectory(tmpMSI);
        }
        Files.createDirectories(tmpMSI);
        Path config = tmpMSI.resolve("config");
        Files.createDirectories(config);

        Path userAssets = rootPath.resolve(Constants.WIN_ASSETS_FOLDER);
        Path iconPath = null;
        if (Files.exists(userAssets) && Files.isDirectory(userAssets)) {
            iconPath = userAssets.resolve("icon.ico");
        }

        // copy assets to gensrc/windows
        if (iconPath == null || !Files.exists(iconPath)) {
            Path windowsGenSrcPath = paths.getGenPath().resolve(sourceOS);
            Path windowsAssetPath = windowsGenSrcPath.resolve(Constants.WIN_ASSETS_FOLDER);
            iconPath = windowsAssetPath.resolve("icon.ico");
            if (!Files.exists(iconPath)) { // not created during link task
                Files.createDirectories(windowsAssetPath);
                iconPath = FileOps.copyResource("/native/windows/assets/icon.ico", windowsAssetPath.resolve("icon.ico"));
                Logger.logInfo("Default icon.ico image generated in " + windowsAssetPath + ".\n" +
                        "Consider copying it to " + rootPath + " before performing any modification");
            }
        }

        // copy to config
        FileOps.copyFile(iconPath, config.resolve("icon.ico"));

        // copy wix related files
        Path wixPath = config.resolve("main.wxs");
        FileOps.copyResource("/native/windows/wix/main.wxs", wixPath);

        Path wixObjPath = config.resolve(wixPath.getFileName() + ".wixobj");
        Path msiPath = paths.getGvmPath().getParent().resolve(appName + "-" +  version  + ".msi");

        /**
         * Wix Compile
         */
        Map<String, String> userInput = createAppDetailMap();
        List<String> processArgs = new ArrayList<>(List.of(
                WixTool.CANDLE.getPath(),
                "-nologo",
                wixPath.toString(),
                "-ext", "WixUtilExtension",
                "-arch", "x64",
                "-out", wixObjPath.toString()
        ));

        userInput.entrySet().stream()
                .map(wixVar -> String.format("-d%s=%s", wixVar.getKey(), wixVar.getValue()))
                .forEachOrdered(processArgs::add);
        ProcessRunner candle = new ProcessRunner(processArgs.toArray(new String[0]));
        if (candle.runProcess("Wix Compiler") != 0) {
            throw new IOException("Error running candle to generate wixobj");
        }

        /**
         * Wix Link
         */
        processArgs.clear();
        processArgs.addAll(List.of(
                WixTool.LIGHT.getPath(),
                "-nologo",
                "-spdb",
                "-sw1076", // https://github.com/wixtoolset/issues/issues/5938
                "-ext", "WixUtilExtension",
                "-ext", "WixUIExtension",
                "-out", msiPath.toString(),
                wixObjPath.toString()
        ));

        ProcessRunner light = new ProcessRunner(processArgs.toArray(new String[0]));
        if (light.runProcess("Wix Linker") != 0) {
            throw new IOException("Error running light to generate msi");
        }
        Logger.logInfo("MSI created successfully at " + msiPath);
        return true;
    }

    private Map<String, String> createAppDetailMap() {

        Map<String, String> userInput = new HashMap<>();
        ReleaseConfiguration releaseConfiguration = projectConfiguration.getReleaseConfiguration();

        String appName = getTrimmedAppName();
        String vendor = releaseConfiguration.getVendor();
        String version = releaseConfiguration.getVersion();
        Path localAppPath = paths.getAppPath().resolve(projectConfiguration.getAppName() + ".exe");
        userInput.put("GSProductCode", createUUID("ProductCode", appName, vendor, version).toString());
        userInput.put("GSAppName", appName);
        userInput.put("GSAppVersion", version);
        userInput.put("GSAppVendor", vendor);
        userInput.put("GSAppIconName", appName + ".ico");
        userInput.put("GSAppIcon", paths.getTmpPath().resolve("tmpMSI").resolve("config").resolve("icon.ico").toString());
        userInput.put("GSMainExecutableGUID", createUUID("GSMainExecutableGUID", appName, vendor, version).toString());
        userInput.put("GSStartMenuShortcutGUID", createUUID("StartMenuShortcutGUID", appName, vendor, version).toString());
        userInput.put("GSDesktopShortcutGUID", createUUID("DesktopShortcutGUID", appName, vendor, version).toString());
        Path license = paths.getTmpPath().resolve("tmpMSI").resolve("config").resolve("license.rtf");
        if (Files.exists(license)) {
            ensureByMutationFileIsRTF(license);
            userInput.put("GSLicenseRtf", license.toString());
        }
        userInput.put("GSApplicationPath", localAppPath.toString());
        userInput.put("GSProductUpgradeCode", createUUID("UpgradeCode", appName, vendor, version).toString());
        userInput.put("GSAppDescription", releaseConfiguration.getDescription());
        return userInput;
    }

    String getTrimmedAppName() {
        return projectConfiguration.getAppName().replaceAll("\\s", "");
    }

    private UUID createUUID(String prefix,String appName, String vendor, String version) {
        String key = String.join(",", prefix, appName, vendor, version);
        return UUID.nameUUIDFromBytes(key.getBytes(StandardCharsets.UTF_8));
    }

    private static void ensureByMutationFileIsRTF(Path f) {
        if (f == null || !Files.isRegularFile(f)) return;

        try {
            boolean existingLicenseIsRTF = false;

            try (InputStream fin = Files.newInputStream(f)) {
                byte[] firstBits = new byte[7];

                if (fin.read(firstBits) == firstBits.length) {
                    String header = new String(firstBits);
                    existingLicenseIsRTF = "{\\rtf1\\".equals(header);
                }
            }

            if (!existingLicenseIsRTF) {
                List<String> oldLicense = Files.readAllLines(f);
                try (Writer w = Files.newBufferedWriter(
                        f, Charset.forName("Windows-1252"))) {
                    w.write("{\\rtf1\\ansi\\ansicpg1252\\deff0\\deflang1033"
                            + "{\\fonttbl{\\f0\\fnil\\fcharset0 Arial;}}\n"
                            + "\\viewkind4\\uc1\\pard\\sa200\\sl276"
                            + "\\slmult1\\lang9\\fs20 ");
                    oldLicense.forEach(l -> {
                        try {
                            for (char c : l.toCharArray()) {
                                // 0x00 <= ch < 0x20 Escaped (\'hh)
                                // 0x20 <= ch < 0x80 Raw(non - escaped) char
                                // 0x80 <= ch <= 0xFF Escaped(\ 'hh)
                                // 0x5C, 0x7B, 0x7D (special RTF characters
                                // \,{,})Escaped(\'hh)
                                // ch > 0xff Escaped (\\ud###?)
                                if (c < 0x10) {
                                    w.write("\\'0");
                                    w.write(Integer.toHexString(c));
                                } else if (c > 0xff) {
                                    w.write("\\ud");
                                    w.write(Integer.toString(c));
                                    // \\uc1 is in the header and in effect
                                    // so we trail with a replacement char if
                                    // the font lacks that character - '?'
                                    w.write("?");
                                } else if ((c < 0x20) || (c >= 0x80) ||
                                        (c == 0x5C) || (c == 0x7B) ||
                                        (c == 0x7D)) {
                                    w.write("\\'");
                                    w.write(Integer.toHexString(c));
                                } else {
                                    w.write(c);
                                }
                            }
                            // blank lines are interpreted as paragraph breaks
                            if (l.length() < 1) {
                                w.write("\\par");
                            } else {
                                w.write(" ");
                            }
                            w.write("\r\n");
                        } catch (IOException e) {
                            Logger.logDebug(e.getMessage());
                        }
                    });
                    w.write("}\r\n");
                }
            }
        } catch (IOException e) {
            Logger.logDebug(e.getMessage());
        }
    }

    enum WixTool {
        CANDLE,
        LIGHT;

        String getPath() {
            for (var dir : findWixInstallDirs()) {
                Path path = dir.resolve(name().toLowerCase(Locale.ROOT) + ".exe");
                if (Files.exists(path)) {
                    return path.toString();
                }
            }
            Logger.logSevere(name() + " not found.");
            return null;
        }

        private List<Path> findWixInstallDirs() {
            PathMatcher wixInstallDirMatcher = FileSystems.getDefault().getPathMatcher(
                    "glob:WiX Toolset v*");

            Path programFiles = getSystemDir("ProgramFiles", "\\Program Files");
            Path programFilesX86 = getSystemDir("ProgramFiles(x86)",
                    "\\Program Files (x86)");

            // Returns list of WiX install directories ordered by WiX version number.
            // Newer versions go first.
            return Stream.of(programFiles, programFilesX86).map(path -> {
                        List<Path> result;
                        try (var paths = Files.walk(path, 1)) {
                            result = paths.collect(Collectors.toList());
                        } catch (IOException ex) {
                            Logger.logDebug(ex.getMessage());
                            result = Collections.emptyList();
                        }
                        return result;
                    }).flatMap(List::stream)
                    .filter(path -> wixInstallDirMatcher.matches(path.getFileName()))
                    .sorted(Comparator.comparing(Path::getFileName).reversed())
                    .map(path -> path.resolve("bin"))
                    .collect(Collectors.toList());
        }

        private Path getSystemDir(String envVar, String knownDir) {
            return Optional
                    .ofNullable(getEnvVariableAsPath(envVar))
                    .orElseGet(() -> Optional
                            .ofNullable(getEnvVariableAsPath("SystemDrive"))
                            .orElseGet(() -> Path.of("C:")).resolve(knownDir));
        }

        private Path getEnvVariableAsPath(String envVar) {
            String path = System.getenv(envVar);
            if (path != null) {
                try {
                    return Path.of(path);
                } catch (InvalidPathException ex) {
                    Logger.logDebug(MessageFormat.format("Invalid value of {0} environment variable", envVar));
                }
            }
            return null;
        }
    }
}
