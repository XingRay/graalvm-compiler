package com.xingray.graalvm.compiler.common.model;


import com.xingray.graalvm.compiler.common.util.FileOps;
import com.xingray.graalvm.compiler.common.util.ProcessRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents the classpath
 * Consolidates all classpath manipulations
 */
public class ClassPath {

    private final String classPath;

    /**
     * Creates the class path
     * @param classPath standard java classpath, delimited with {@code File.pathSeparator}. Should not be null.
     */
    public ClassPath(String classPath) {
        this.classPath = Objects.requireNonNull(classPath);
    }

    private Stream<String> asStream() {
        return Stream.of(classPath.split(File.pathSeparator));
    }

    /**
     * Returns whether any elements of this classpath match the provided
     * predicate.
     * @param predicate predicate to apply to elements of this classpath. Should not be null.
     * @return {@code true} if any elements of the stream match the provided
     *  predicate, otherwise {@code false}
     */
    public boolean contains(Predicate<String> predicate) {
        Objects.requireNonNull(predicate);
        return asStream().anyMatch( predicate);
    }

    /**
     * Returns a list of strings consisting of the elements of this classpath that match
     * the given predicate.
     * @param predicate predicate to apply to elements of this classpath. Should not be null.
     * @return filtered list of elements of this classpath
     */
    public List<String> filter(Predicate<String> predicate) {
        Objects.requireNonNull(predicate);
        return asStream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * Returns a List consisting of the results of applying the given
     * function to the elements of this classpath.
     *
     * @param <T> The element type of the resulting List
     * @param mapper function to apply to each element. Should not be null.
     * @return the list
     */
    public <T> List<T> mapToList( Function<String, T> mapper) {
        Objects.requireNonNull(mapper);
        return asStream().map(mapper).collect(Collectors.toList());
    }

    /**
     * Returns a String classpath consisting of the results of applying the given
     * function to the elements of this classpath.
     *
     * @param mapper function to apply to each element. Should not be null.
     * @return the string classpath
     */
    public String mapToString(Function<String, String> mapper) {
        Objects.requireNonNull(mapper);
        return asStream().map(mapper).distinct().collect(Collectors.joining(File.pathSeparator));
    }

    /**
     * Returns a String classpath consisting of existing classes. Tries to find libraries by name
     * and replace them with full path to this library within the given library path
     * @param libsPath library path
     * @param libNames library names to look for
     * @return the string classpath
     */
    public String mapWithLibs(Path libsPath, String... libNames) {
        return mapWithLibs(libsPath, null, null, libNames);
    }

    /**
     * Returns a String classpath consisting of existing classes. Tries to find libraries by name
     * and replace them with full path to this library within the given library path.
     *
     * A function can be used to map the library names, if the files in the library path follow
     * a different pattern than those on the classpath
     *
     * A predicate can be used to verify certain conditions to the found libraries, for
     * instance checking that the files actually exist. If the test fails, the original string will
     * be returned unmapped
     *
     * @param libsPath library path
     * @param libMap a function that maps the library name, can be null
     * @param pathPredicate test to apply to the replaced libraries, can be null
     * @param libNames library names to look for
     * @return the string classpath
     */
    public String mapWithLibs(Path libsPath, Function<String, String> libMap, Predicate<Path> pathPredicate,
                              String... libNames) {
        Objects.requireNonNull(libsPath);
        return mapToString(s -> Arrays.stream(libNames)
                .filter(s::contains)
                .findFirst()
                .map(d -> {
                    String libName = libMap != null ? libMap.apply(d) : d;
                    Path libPath = libsPath.resolve(libName + ".jar");
                    return pathPredicate == null || pathPredicate.test(libPath) ?
                            libPath.toString() : null;
                })
                .orElse(s));
    }

    /**
     * Returns a list with all the jar files that are found in the classpath.
     *
     * @param includeClasses if true, a jar will be created and added to the list,
     *                       containing the compiled classes and resources of the
     *                       current project
     * @return a list of jar files
     * @throws IOException
     * @throws InterruptedException
     */
    public List<File> getJars(boolean includeClasses) throws IOException, InterruptedException {
        List<File> jars = filter(s -> s.endsWith(".jar")).stream()
                .map(File::new)
                .distinct()
                .collect(Collectors.toList());

        if (includeClasses) {
            // Add project's classes as a jar to the list so it can be scanned as well
            String classes = filter(s -> s.endsWith("classes") ||
                            s.endsWith("classes" + File.separator + "java" + File.separator + "main")).stream()
                    .findFirst()
                    .orElse(null);
            if (classes != null) {
                Path classesPath = Files.createTempDirectory("classes");
                FileOps.copyDirectory(Path.of(classes), classesPath);
                Path resourcesPath = filter(s -> s.endsWith("resources" + File.separator + "main")).stream()
                        .findFirst()
                        .map(Path::of)
                        .orElse(null);
                if (resourcesPath != null && Files.exists(resourcesPath)) {
                    FileOps.copyDirectory(resourcesPath, classesPath);
                }

                String javaPath = System.getenv("JAVA_HOME");
                if (javaPath == null || javaPath.isEmpty()) {
                    javaPath = System.getenv("GRAALVM_HOME");
                    if (javaPath == null || javaPath.isEmpty()) {
                        throw new IOException("Error: $JAVA_HOME and $GRAALVM_HOME are undefined");
                    }
                }
                Path jarPath = Path.of(javaPath, "bin", CompileTask.isWindowsHost() ? "jar.exe" : "jar");
                if (!Files.exists(jarPath)) {
                    throw new IOException("Error: " + jarPath + " doesn't exist");
                }

                Path classesJar = classesPath.resolve("classes.jar");

                ProcessRunner runner = new ProcessRunner(jarPath.toString(),
                        "cf", classesJar.toString(), "-C", classesPath.toString(), ".");

                if (runner.runProcess("jar") == 0 && Files.exists(classesJar)) {
                    jars.add(classesJar.toFile());
                } else {
                    throw new IOException("Error creating classes.jar");
                }
            }
        }

        return jars;
    }
}
