package com.xingray.graalvm.compiler.core;

import com.xingray.graalvm.compiler.android.target.AndroidTargetConfiguration;
import com.xingray.graalvm.compiler.common.Constants;
import com.xingray.graalvm.compiler.common.Profile;
import com.xingray.graalvm.compiler.common.ProjectConfiguration;
import com.xingray.graalvm.compiler.common.model.InternalProjectConfiguration;
import com.xingray.graalvm.compiler.common.model.ProcessPaths;
import com.xingray.graalvm.compiler.common.model.CompileTask;
import com.xingray.graalvm.compiler.common.target.TargetConfiguration;
import com.xingray.graalvm.compiler.common.util.Logger;
import com.xingray.graalvm.compiler.common.util.ProcessRunner;
import com.xingray.graalvm.compiler.common.util.Strings;
import com.xingray.graalvm.compiler.ios.target.IosTargetConfiguration;
import com.xingray.graalvm.compiler.linux.target.LinuxTargetConfiguration;
import com.xingray.graalvm.compiler.macos.target.MacOSTargetConfiguration;
import com.xingray.graalvm.compiler.web.target.WebTargetConfiguration;
import com.xingray.graalvm.compiler.windows.target.WindowsTargetConfiguration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class GraalvmCompiler {

    private static volatile boolean compiling = true;

    public static void main(String[] args) throws IOException {
        Step step = getStepToExecute();

        Path buildRoot = Paths.get(System.getProperty("user.dir"), "build", "autoclient");
        ProjectConfiguration configuration = createProjectConfiguration();
        GraalvmCompiler dispatcher = new GraalvmCompiler(buildRoot, configuration);

        executeCompileStep(dispatcher);

        if (step.requires(Step.LINK)) {
            executeLinkStep(dispatcher);
        }

        if (step.requires(Step.PACKAGE)) {
            executePackageStep(dispatcher);
        }

        if (step.requires(Step.INSTALL)) {
            executeInstallStep(dispatcher);
        }

        if (step.requires(Step.RUN)) {
            executeRunStep(dispatcher);
        }
    }

    private static ProjectConfiguration createProjectConfiguration() {
        String classpath = requireSystemProperty("imagecp", "Use -Dimagecp=/path/to/classes");
        String graalVM = requireSystemProperty("graalvm", "Use -Dgraalvm=/path/to/graalvm");
        String mainClass = requireSystemProperty("mainclass", "Use -Dmainclass=main.class.name");

        String appId = Optional.ofNullable(System.getProperty("appId")).orElse("com.gluonhq.anonymousApp");
        String appName = Optional.ofNullable(System.getProperty("appname")).orElse("anonymousApp");
        boolean verbose = System.getProperty("verbose") != null;

        boolean usePrismSW = Boolean.parseBoolean(System.getProperty("prism.sw", "false"));
        boolean usePrecompiledCode = Boolean.parseBoolean(System.getProperty("usePrecompiledCode", "true"));
        List<String> nativeImageArgs = Arrays.asList(System.getProperty("nativeImageArgs", "").split(","));
        String targetProfile = System.getProperty("targetProfile");
        CompileTask targetCompileTask = targetProfile != null ?
                new CompileTask(Profile.valueOf(targetProfile.toUpperCase())) :
                CompileTask.fromCurrentOS();

        ProjectConfiguration config = new ProjectConfiguration(mainClass, classpath);
        config.setGraalPath(Path.of(graalVM));
        config.setAppId(appId);
        config.setAppName(appName);
        config.setTarget(targetCompileTask);
        config.setReflectionList(Strings.split(System.getProperty("reflectionlist")));
        config.setJniList(Strings.split(System.getProperty("jnilist")));
        config.setBundlesList(Strings.split(System.getProperty("bundleslist")));
        config.setVerbose(verbose);
        config.setUsePrismSW(usePrismSW);
        config.setUsePrecompiledCode(usePrecompiledCode);
        if (!nativeImageArgs.isEmpty()) {
            config.setCompilerArgs(nativeImageArgs);
        }
        return config;
    }

    private static Step getStepToExecute() {
        return Optional.ofNullable(System.getProperty("step"))
                .map(stepProperty -> {
                    try {
                        return Step.valueOf(stepProperty.toUpperCase(Locale.ROOT));
                    } catch (IllegalArgumentException e) {
                        printUsage();
                        throw new IllegalArgumentException(String.format("Invalid value for 'step' specified. Possible values: %s", Arrays.toString(Step.values())));
                    }
                })
                .orElse(Step.RUN);
    }

    public static void executeCompileStep(GraalvmCompiler dispatcher) {
        startNativeCompileTimer();

        try {
            boolean nativeCompileSucceeded = dispatcher.nativeCompile();
            compiling = false;

            if (!nativeCompileSucceeded) {
                Logger.logSevere("Compiling failed.");
                System.exit(1);
            }
        } catch (Throwable t) {
            Logger.logFatal(t, "Compiling failed with an exception.");
        }
    }

    private static void startNativeCompileTimer() {
        Thread timer = new Thread(() -> {
            int counter = 1;
            while (compiling) {
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Logger.logInfo("NativeCompile is still running, please hold [" + counter++ + " minute(s)]");
            }
        });
        timer.setDaemon(true);
        timer.start();
    }

    private static void executeLinkStep(GraalvmCompiler dispatcher) {
        try {
            if (!dispatcher.nativeLink()) {
                Logger.logSevere("Linking failed.");
                System.exit(1);
            }
        } catch (Throwable t) {
            Logger.logFatal(t, "Linking failed with an exception.");
        }
    }

    private String getHash(String input) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private static void executePackageStep(GraalvmCompiler dispatcher) {
        try {
            if (!dispatcher.nativePackage()) {
                Logger.logSevere("Packaging failed.");
                System.exit(1);
            }
        } catch (Throwable t) {
            Logger.logFatal(t, "Packaging failed with an exception.");
        }
    }

    private static void executeInstallStep(GraalvmCompiler dispatcher) {
        try {
            if (!dispatcher.nativeInstall()) {
                Logger.logSevere("Installing failed.");
                System.exit(1);
            }
        } catch (Throwable t) {
            Logger.logFatal(t, "Installing failed with an exception.");
        }
    }

    private static void executeRunStep(GraalvmCompiler dispatcher) {
        try {
            String expected = System.getProperty("expected");
            if (expected != null) {
                Logger.logInfo(logTitle("RUN TASK (with expected)"));

                String response = dispatcher.targetConfiguration.run();
                if (expected.equals(response)) {
                    Logger.logInfo("Run ended successfully, the output: " + expected + " matched the expected result.");
                } else {
                    Logger.logSevere("Run failed, expected output: " + expected + ", output: " + response);
                    System.exit(1);
                }
            } else {
                dispatcher.nativeRun();
            }
        } catch (Throwable t) {
            Logger.logFatal(t, "Running failed with an exception");
        }
    }

    private static String requireSystemProperty(String argName, String errorMessage) {
        String arg = System.getProperty(argName);
        if (arg == null || arg.trim().isEmpty()) {
            printUsage();
            throw new IllegalArgumentException(String.format("No '%s' specified. %s", argName, errorMessage));
        }
        return arg;
    }

    private static String logTitle(String text) {
        return "==================== " + (text == null ? "" : text) + " ====================";
    }

    private static void printUsage() {
        System.out.println("Usage:\n java -Dimagecp=... -Dgraalvm=... -Dmainclass=... com.gluonhq.substrate.SubstrateDispatcher");
    }

    private final InternalProjectConfiguration config;
    private final ProcessPaths paths;
    private final TargetConfiguration targetConfiguration;


    /**
     * Dispatches calls to different process steps. Uses shared build root path and project configuration
     *
     * @param buildRoot the root, relative to which the compilation step can create object files and temporary files
     * @param config    the ProjectConfiguration, including the target triplet
     */
    public GraalvmCompiler(Path buildRoot, ProjectConfiguration config) throws IOException {
        this.paths = new ProcessPaths(Objects.requireNonNull(buildRoot),
                Objects.requireNonNull(config).getTargetTriplet().getArchOs());
        ProcessRunner.setProcessLogPath(paths.getClientPath().resolve(Constants.LOG_PATH));
        ProcessRunner.setConsoleProcessLog(Boolean.getBoolean("consoleProcessLog"));

        this.config = new InternalProjectConfiguration(config);
        if (this.config.isVerbose()) {
            System.out.println("Configuration: " + this.config);
        }

        this.config.checkGraalVMVersion();
        this.config.checkGraalVMJavaVersion();
        this.config.checkGraalVMVendor();

        CompileTask targetCompileTask = config.getTargetTriplet();

        this.targetConfiguration = Objects.requireNonNull(getTargetConfiguration(targetCompileTask),
                "Error: Target Configuration was not found for " + targetCompileTask);

        Logger.logInit(paths.getLogPath().toString(), this.config.isVerbose());
    }

    private TargetConfiguration getTargetConfiguration(CompileTask targetCompileTask) throws IOException {
        if (!Constants.OS_WEB.equals(targetCompileTask.getOs()) && !config.getHostTriplet().canCompileTo(targetCompileTask)) {
            throw new IllegalArgumentException("We currently can't compile to " + targetCompileTask + " when running on " + config.getHostTriplet());
        }
        switch (targetCompileTask.getOs()) {
            case Constants.OS_LINUX:
                return new LinuxTargetConfiguration(paths, config);
            case Constants.OS_DARWIN:
                return new MacOSTargetConfiguration(paths, config);
            case Constants.OS_WINDOWS:
                return new WindowsTargetConfiguration(paths, config);
            case Constants.OS_IOS:
                return new IosTargetConfiguration(paths, config);
            case Constants.OS_ANDROID:
                return new AndroidTargetConfiguration(paths, config);
            case Constants.OS_WEB:
                return new WebTargetConfiguration(paths, config);
            default:
                return null;
        }
    }


    /**
     * This method will start native compilation for the specified configuration.
     * The result of compilation is a at least one native file (2 files in case LLVM backend is used).
     * This method returns <code>true</code> on successful compilation and <code>false</code> when compilations fails.
     *
     * @return true if compilation succeeded, false if it fails
     * @throws Exception
     * @throws IllegalArgumentException when the supplied configuration contains illegal combinations
     */
    public boolean nativeCompile() throws Exception {
        Logger.logInfo(logTitle("COMPILE TASK"));
        System.out.println("compile");

        CompileTask targetCompileTask = config.getTargetTriplet();
        config.canRunLLVM(targetCompileTask);

        Logger.logInfo("We will now compile your code for " + targetCompileTask + ". This may take some time.");
        boolean compilingSucceeded = targetConfiguration.compile();
        if (!compilingSucceeded) {
            Logger.logSevere("Compiling failed.");
        }
        return compilingSucceeded;
    }

    /**
     * This method will start native linking for the specified configuration, after {@link #nativeCompile()}
     * was called and ended successfully.
     * The result of linking is a at least an native image application file.
     * This method returns <code>true</code> on successful linking and <code>false</code> when linking fails.
     *
     * @return true if linking succeeded, false if it fails
     * @throws Exception
     * @throws IllegalArgumentException when the supplied configuration contains illegal combinations
     */
    public boolean nativeLink() throws IOException, InterruptedException {
        Logger.logInfo(logTitle("LINK TASK"));
        boolean linkingSucceeded = targetConfiguration.link();
        if (!linkingSucceeded) {
            Logger.logSevere("Linking failed.");
        }
        System.out.println("link");
        return linkingSucceeded;
    }

    /**
     * This method creates a package of the native image application, that was created after {@link #nativeLink()}
     * was called and ended successfully.
     * This method returns <code>true</code> on successful packaging and <code>false</code> when packaging fails.
     *
     * @return true if packaging succeeded, false if it fails
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean nativePackage() throws IOException, InterruptedException {
        Logger.logInfo(logTitle("PACKAGE TASK"));
        boolean packagingSucceeded = targetConfiguration.packageApp();
        if (!packagingSucceeded) {
            Logger.logSevere("Packaging failed.");
        }
        System.out.println("package");
        return packagingSucceeded;
    }

    /**
     * This method installs the generated package that was created after {@link #nativePackage()}
     * was called and ended successfully.
     * This method returns <code>true</code> on successful installation and <code>false</code> when installation fails.
     *
     * @return true if installing succeeded, false if it fails
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean nativeInstall() throws IOException, InterruptedException {
        Logger.logInfo(logTitle("INSTALL TASK"));
        boolean installingSucceeded = targetConfiguration.install();
        if (!installingSucceeded) {
            Logger.logSevere("Installing failed.");
        }
        System.out.println("install");
        return installingSucceeded;
    }

    /**
     * This method runs the native image application, that was created after {@link #nativeLink()}
     * was called and ended successfully.
     *
     * @throws IOException
     * @throws IllegalArgumentException when the supplied configuration contains illegal combinations
     */
    public void nativeRun() throws IOException, InterruptedException {
        Logger.logInfo(logTitle("RUN TASK"));
        targetConfiguration.runUntilEnd();
        System.out.println("run");
    }

    /**
     * This method builds a native image that can be used as shared library by third
     * party projects, considering it contains one or more entry points.
     * <p>
     * Static entry points, callable from C, can be created with the {@code @CEntryPoint}
     * annotation.
     *
     * @throws Exception
     */
    public boolean nativeSharedLibrary() throws Exception {
        Logger.logInfo(logTitle("SHARED LIBRARY TASK"));
        config.setSharedLibrary(true);
        return targetConfiguration.createSharedLib();
    }
}
