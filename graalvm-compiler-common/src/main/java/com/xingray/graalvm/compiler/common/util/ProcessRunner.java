/*
 * Copyright (c) 2019, 2020, Gluon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

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
package com.xingray.graalvm.compiler.common.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Utility class to run processes based on command line arguments
 * by wrapping a {@Link ProcessBuilder}.
 *
 */
public class ProcessRunner {

    private final List<String> args = new ArrayList<>();
    private final Map<String, String> map;
    private final List<String> passwords;
    private final StringBuffer answer;
    private boolean info;
    private boolean showSevere = true;
    private boolean logToFile;
    private boolean interactive;

    private static Path processLogPath;
    private static boolean consoleProcessLog;

    /**
     * Constructor, allowing some command line arguments
     * @param args A varargs of command line arguments
     */
    public ProcessRunner(String... args) {
        this.args.addAll(Arrays.asList(args));
        this.answer = new StringBuffer();
        this.map = new HashMap<>();
        this.passwords = new ArrayList<>();
    }

    /**
     * Sets the path where the process logs will be created. If the
     * path doesn't exist, it will be created.
     *
     * This should be called once. If not set, the process won't be
     * logged.
     *
     * @param path the path where the process logs will be created
     */
    public static void setProcessLogPath(Path path) throws IOException {
        processLogPath = Objects.requireNonNull(path);
        if (!Files.exists(processLogPath)) {
            Files.createDirectories(processLogPath);
        }
    }

    /**
     * Sets true if the processes are logged, not only to a
     * file, but also to console. Useful, for instance, in CI
     * environments without access to log files.
     *
     * This should be called once.
     *
     * @param value true if process is logged to console, false
     *              by default
     */
    public static void setConsoleProcessLog(boolean value) {
        consoleProcessLog = value;
    }

    /**
     * When set to true, it will log with Level.INFO the output
     * during the process. By default is false, and uses Level.DEBUG
     * @param info a boolean that sets the log level of the process output
     */
    public void setInfo(boolean info) {
        this.info = info;
    }

    /**
     * When set to true, a message with Level.SEVERE will be logged in case
     * the process fails.
     * By default is true.
     * @param showSevere a boolean that allows showing or not a severe message
     */
    public void showSevereMessage(boolean showSevere) {
        this.showSevere = showSevere;
    }

    /**
     * When set to true, it will enable user interaction
     * during the process. By default is false
     * @param interactive a boolean that sets the interactive mode
     */
    public void setInteractive(boolean interactive) {
        this.interactive = interactive;
    }

    /**
     * When set to true, it will log to a file the command line
     * arguments, the output and the result of the process. By default
     * it is false, but the log will be created regardless this value
     * for any failing process.
     * @param logToFile if true will log the process to a file
     */
    public void setLogToFile(boolean logToFile) {
        this.logToFile = logToFile;
    }

    /**
     * Adds a command line argument to the list of existing list of
     * command line arguments
     * @param arg a string passed to the command line arguments
     */
    public void addArg(String arg) {
        args.add(arg);
    }

    /**
     * Adds a command line argument to the list of existing list of
     * command line arguments, marking it as secret argument, in
     * order to avoid logging
     * @param arg a string passed to the command line arguments
     */
    public void addSecretArg(String arg) {
        passwords.add(arg);
        args.add(arg);
    }

    /**
     * Adds a varargs list of arguments to the existing list of
     * command line of arguments
     * @param args varargs list of arguments
     */
    public void addArgs(String... args) {
        this.args.addAll(Arrays.asList(args));
    }

    /**
     * Adds a collection of arguments to the existing list of
     * command line of arguments
     * @param args a collection of arguments
     */
    public void addArgs(Collection<String> args) {
        this.args.addAll(args);
    }

    /**
     *
     * @return the command line of arguments as a string
     */
    public String getCmd() {
        return String.join(" ", args);
    }

    /**
     *
     * @return the current list of command line arguments
     */
    public List<String> getCmdList() {
        return args;
    }

    /**
     * Adds a pair (key, value) to the environment map of the
     * process
     * @param key a string with the environmental variable name
     * @param value a string with the environmental variable value
     */
    public void addToEnv(String key, String value) {
        map.put(key, value);
    }

    /**
     * Runs a process with a given set of command line arguments
     *
     * @param processName the name of the process
     * @return 0 if the process ends successfully, non-zero values indicate a failure
     * @throws IOException
     * @throws InterruptedException
     */
    public int runProcess(String processName) throws IOException, InterruptedException {
        return runProcess(processName, null);
    }

    /**
     * Runs a process with a given set of command line arguments, in a given
     * working directory.
     *
     * @param processName the name of the process
     * @param workingDirectory a file with the working directory of the process
     * @return 0 if the process ends successfully, non-zero values indicate a failure
     * @throws IOException
     * @throws InterruptedException
     */
    public int runProcess(String processName, File workingDirectory) throws IOException, InterruptedException {
        Process p = setupProcess(processName, workingDirectory);

        Thread logThread = mergeProcessOutput(p.getInputStream());
        int result = p.waitFor();
        logThread.join();
        Logger.logDebug("Result for " + processName + ": " + result);
        if (result != 0 && showSevere) {
            Logger.logSevere("Process " + processName + " failed with result: " + result);
        }
        if (logToFile || result != 0) {
            logProcess(processName, "result: " + result, result != 0);
        }
        return result;
    }

    /**
     * Runs a process with a given set of command line arguments within a given time frame
     *
     * @param processName the name of the process
     * @param timeout the maximum time allowed to run the process
     * @return true if the process ended successfully, false otherwise
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean runTimedProcess(String processName, long timeout) throws IOException, InterruptedException {
        return runTimedProcess(processName, null, timeout);
    }

    /**
     * Runs a process with a given set of command line arguments, in a given
     * working directory, within a given time frame
     *
     * @param processName the name of the process
     * @param workingDirectory a file with the working directory of the process
     * @param timeout the maximum time allowed to run the process
     * @return true if the process ended successfully, false otherwise
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean runTimedProcess(String processName, File workingDirectory, long timeout) throws IOException, InterruptedException {
        Process p = setupProcess(processName, workingDirectory);
        Thread logThread = mergeProcessOutput(p.getInputStream());
        boolean result = p.waitFor(timeout, TimeUnit.SECONDS);
        logThread.join();
        Logger.logDebug("Result for " + processName + ": " + result);
        if (!result && showSevere) {
            Logger.logSevere("Process " + processName + " failed with result: " + result);
        }
        if (logToFile || !result) {
            logProcess(processName, "result: " + result, !result);
        }
        return result;
    }

    /**
     * Gets the response of the process as single string
     *
     * @return a single string with the whole output of the process
     */
    public String getResponse() {
        if (answer != null) {
            return answer.toString().replace("\n", "");
        }
        return null;
    }

    /**
     * Gets the response of the process as list of lines
     *
     * @return a list with all the lines of the output
     */
    public List<String> getResponses() {
        return answer == null ? null :
                Arrays.asList(answer.toString().split("\n"));
    }

    /**
     * Gets the last line of the output process
     *
     * @return a string with the last line of the output
     */
    public String getLastResponse() {
        if (answer == null) {
            return null;
        }
        String[] answers = answer.toString().split("\n");
        return answers.length > 0 ? answers[answers.length - 1] : "";
    }

    /**
     * Static method that can be used to process a given command line,
     * returning the output of the process as a single string, or null if
     * it failed.
     * It is convenient when the output of the process to be executed
     * returns a single line
     * @param name the name of the process
     * @param args a varargs list of command line arguments
     * @return a string with the response of the process or null if it failed
     * @throws IOException
     * @throws InterruptedException
     */
    public static String runProcessForSingleOutput(String name, String... args) throws IOException, InterruptedException {
        ProcessRunner process = new ProcessRunner(args);
        int result = process.runProcess(name);
        if (result == 0) {
            return process.getResponse();
        }
        return null;
    }

    /**
     * Executes a process while printing "." every second the process is running.
     * The feedback is helpful for end user. A lack of feedback can lead to an impression that the process is stuck.
     * @param name the name of the process
     * @param args a varargs list of command line arguments
     * @return Integer result of the process
     *
     */
    public static Integer executeWithFeedback(String name, String... args) throws IOException, InterruptedException {
        ProcessRunner process = new ProcessRunner(args);
        return new ProcessWithFeedback(process, name).call();
    }

    private Process setupProcess(String processName, File directory) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(args);
        String join = pb.command().stream().map(s -> {
            if (!s.isEmpty() && passwords.contains(s)) {
                return s.substring(0, 1) + "******";
            }
            return s;
        }).collect(Collectors.joining(" "));
        Logger.logDebug("PB Command for " +  processName + ": " + join);
        pb.redirectErrorStream(true);
        if (interactive) {
            pb.inheritIO();
        }
        if (directory != null) {
            pb.directory(directory);
        }
        map.forEach((k, v) -> pb.environment().put(k, v));
        answer.setLength(0);
        Logger.logDebug("Start process " + processName + "...");
        return pb.start();
    }

    private Thread mergeProcessOutput(final InputStream is) {
        Runnable r = () -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    answer.append(line).append("\n");
                    if (info) {
                        Logger.logInfo("[SUB] " + line);
                    } else {
                        Logger.logDebug("[SUB] " + line);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        };
        Thread thread = new Thread(r);
        thread.start();
        return thread;
    }

    /**
     * Logs to a file the command line arguments, the whole output and the
     * result of the process.
     * @param processName The name of the process
     * @param result The result of the process
     * @param failure true if the process failed
     * @throws IOException
     */
    private void logProcess(String processName, String result, boolean failure) throws IOException {
        if (processLogPath == null) {
            Logger.logSevere("Can't log " + processName + " process, processLogPath was null");
            return;
        }

        Path log = processLogPath.resolve("process-" + processName + "-" + System.currentTimeMillis() + ".log");
        if (failure) {
            Logger.logInfo("Logging process [" + processName + "] to file: " + log);
        } else {
            Logger.logDebug("Logging process [" + processName + "] to file: " + log);
        }
        String message = toString(processName, result);
        Files.write(log, message.getBytes());
        if (consoleProcessLog) {
            Logger.logInfo(message);
        }
    }

    private String toString(String processName, String result) {
        return "Process\n=======\n" + processName + "\n\n" +
                "Command Line\n============\n" + getCmd() + "\n\n" +
                "Output\n======\n" + answer.toString() + "\n\n" +
                "Result\n======\n" + result;
    }

    /**
     * Prints "." every second the background process is running.
     * The feedback is helpful for end user. A lack of feedback can lead to an impression that the process is stuck.
     */
    private static class ProcessWithFeedback implements Callable<Integer> {

        private final ProcessRunner processRunner;
        private final String processName;
        private final Timer timer;

        private ProcessWithFeedback(ProcessRunner processRunner, String processName) {
            this.processRunner = processRunner;
            this.processName = processName;
            timer = new Timer(true);
        }

        @Override
        public Integer call() throws IOException, InterruptedException {
            timer.schedule(new PrintTask(), 0, 1000);
            final int result = processRunner.runProcess(processName);
            timer.cancel();
            System.out.println();
            return result;
        }

        private static class PrintTask extends TimerTask {
            @Override
            public void run() {
                System.out.print(".");
                System.out.flush();
            }
        }
    }
}
