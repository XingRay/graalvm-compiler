/*
 * Copyright (c) 2019, Gluon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL GLUON BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.xingray.graalvm.compiler.common.config;


import com.xingray.graalvm.compiler.common.Constants;
import com.xingray.graalvm.compiler.common.model.ClassPath;
import com.xingray.graalvm.compiler.common.util.FileOps;
import com.xingray.graalvm.compiler.common.util.Logger;
import com.xingray.graalvm.compiler.common.util.Strings;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Helper class that helps scanning jars in the classpath looking for
 * files in META-INF/substrate/config that contain information that has
 * to be added to the config files and native image command line flags
 */
public class ConfigResolver {

    private final List<File> jars;

    /**
     * ConfigResolver constructor
     *
     * @param classpath a string with the full classpath of the user's project
     * @throws IOException
     * @throws InterruptedException
     */
    public ConfigResolver(String classpath) throws IOException, InterruptedException {
        this.jars = new ClassPath(classpath).getJars(true);
    }

    /**
     * Walks through the jars in the classpath,
     * and looks for META-INF/substrate/config/initbuildtime or
     * META-INF/substrate/config/initbuildtime-${archos} files.
     *
     * The method will return a list of class names from all the files found
     *
     * @param archOs a string with the arch and os, it can be null
     * @return a list of classes that should be initialized at build time
     * @throws IOException
     */
    public List<String> getUserInitBuildTimeList(String archOs) throws IOException {
        Logger.logDebug("Scanning for init build time files");
        return scanJars(Constants.USER_INIT_BUILD_TIME_FILE,
                getFileNameForArchOs(Constants.USER_INIT_BUILD_TIME_ARCHOS_FILE, archOs),
                null,
                null);
    }

    /**
     * Walks through the jars in the classpath,
     * and looks for META-INF/substrate/config/resourcebundles or
     * META-INF/substrate/config/resourcebundles-${archos} files.
     *
     * The method will return a list of resource bundles names from all the files found
     *
     * @param archOs a string with the arch and os, it can be null
     * @return a list of resource bundles
     * @throws IOException
     */
    public List<String> getResourceBundlesList(String archOs) throws IOException {
        Logger.logDebug("Scanning for resource bundles");
        return scanJars(Constants.RESOURCE_BUNDLES_FILE,
                getFileNameForArchOs(Constants.RESOURCE_BUNDLES_ARCHOS_FILE, archOs),
                null,
                null);
    }

    /**
     * Walks through the jars in the classpath,
     * and looks for META-INF/substrate/config/reflectionconfig or
     * META-INF/substrate/config/reflectionconfig-${archos} files.
     *
     * The method will return a list of lines from all the files found
     *
     * @param archOs a string with the arch and os, it can be null
     * @return a list of lines that should be added to the reflectionconfig.json file
     * @throws IOException
     */
    public List<String> getUserReflectionList(String archOs) throws IOException {
        Logger.logDebug("Scanning for reflection files");
        return scanJars(Constants.USER_REFLECTION_FILE,
                getFileNameForArchOs(Constants.USER_REFLECTION_ARCHOS_FILE, archOs),
                ",",
                line -> !line.startsWith("[") && !line.startsWith("]"));
    }

    /**
     * Walks through the jars in the classpath,
     * and looks for META-INF/substrate/config/jniconfig or
     * META-INF/substrate/config/jniconfig-${archos} files.
     *
     * The method will return a list of lines from all the files found
     *
     * @param archOs a string with the arch and os, it can be null
     * @return a list of lines that should be added to the jniconfig.json file
     * @throws IOException
     */
    public List<String> getUserJNIList(String archOs) throws IOException {
        Logger.logDebug("Scanning for JNI files");
        return scanJars(Constants.USER_JNI_FILE,
                getFileNameForArchOs(Constants.USER_JNI_ARCHOS_FILE, archOs),
                ",",
                line -> !line.startsWith("[") && !line.startsWith("]"));
    }

    /**
     * Walks through the jars in the classpath,
     * and looks for META-INF/substrate/config/resourceconfig or
     * META-INF/substrate/config/resourceconfig-${archos} files.
     *
     * The method will return a list of lines from all the files found
     *
     * @param archOs a string with the arch and os, it can be null
     * @return a list of lines that should be added to the resourceconfig.json file
     * @throws IOException
     */
    public List<String> getUserResourcesList(String archOs) throws IOException {
        Logger.logDebug("Scanning for resource files");
        List<String> resources = scanJars(Constants.USER_RESOURCE_FILE,
                getFileNameForArchOs(Constants.USER_RESOURCE_ARCHOS_FILE, archOs),
                null,
                line -> line.trim().startsWith("{\"pattern\""));
        AtomicInteger index = new AtomicInteger();
        return resources.stream()
                .map(r -> (index.getAndIncrement() < resources.size() - 1 && !r.trim().endsWith(",")) ?
                                r.concat(",") : r)
                .collect(Collectors.toList());
    }

    private List<String> scanJars(String configName, String configArchosName, String initLine, Predicate<String> filter) throws IOException {
        Objects.requireNonNull(configName, "configName can't be null");
        List<String> list = new ArrayList<>();
        for (File jar : jars) {
            if (!jar.exists()) {
                continue;
            }
            try (ZipFile zip = new ZipFile(jar)) {
                Logger.logDebug("Scanning " + jar);
                for (Enumeration<? extends ZipEntry> e = zip.entries(); e.hasMoreElements(); ) {
                    ZipEntry zipEntry = e.nextElement();
                    String name = zipEntry.getName();
                    if (!zipEntry.isDirectory() &&
                            ((Constants.META_INF_SUBSTRATE_CONFIG + configName).equals(name) ||
                                (configArchosName != null && (Constants.META_INF_SUBSTRATE_CONFIG + configArchosName).equals(name)))) {
                        if (initLine != null) {
                            // first line content before adding the file's content
                            list.add(initLine);
                        }
                        Logger.logDebug("Adding classes from " + zip.getName() + "::" + zipEntry.getName());
                        list.addAll(FileOps.readFileLines(zip.getInputStream(zipEntry), filter));
                    }
                }
            }
        }
        return list;
    }

    private String getFileNameForArchOs(String userFileName, String archOs) {
        return archOs == null ?
                null : Strings.substitute(userFileName, Map.of("archOs", archOs));
    }

}
