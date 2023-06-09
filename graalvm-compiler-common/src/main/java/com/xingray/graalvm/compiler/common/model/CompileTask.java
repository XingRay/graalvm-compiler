/*
 * Copyright (c) 2019, 2022, Gluon
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
package com.xingray.graalvm.compiler.common.model;


import com.xingray.graalvm.compiler.common.Constants;
import com.xingray.graalvm.compiler.common.Profile;

import java.util.Locale;

public class CompileTask {

    private static final String OS_ARCH = System.getProperty("os.arch").toLowerCase(Locale.ROOT);
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase(Locale.ROOT);

    private String arch;
    private String vendor;
    private String os;

    public static CompileTask fromCurrentOS() throws IllegalArgumentException {
        if (isMacOSHost()) {
            if (isAarch64Arch()) {
                return new CompileTask(Profile.MACOS_AARCH64);
            } else {
                return new CompileTask(Profile.MACOS);
            }
        } else if (isLinuxHost()) {
            if (isAarch64Arch()) {
                return new CompileTask(Profile.LINUX_AARCH64);
            } else {
                return new CompileTask(Profile.LINUX);
            }
        } else if (isWindowsHost()) {
            return new CompileTask(Profile.WINDOWS);
        } else {
            throw new IllegalArgumentException("OS " + OS_NAME + " not supported");
        }
    }

    /**
     * @return true if host is Windows
     */
    public static boolean isWindowsHost() {
        return OS_NAME.contains("windows");
    }

    /**
     * @return true if host is MacOS
     */
    public static boolean isMacOSHost() {
        return OS_NAME.contains("mac");
    }

    /**
     * @return true if host is Linux
     */
    public static boolean isLinuxHost() {
        return OS_NAME.contains("nux");
    }

    /**
     * @return true if host architecture is AArch64
     */
    public static boolean isAarch64Arch() {
        return OS_ARCH.contains("aarch64");
    }

    public CompileTask(String arch, String vendor, String os) {
        this.arch = arch;
        this.vendor = vendor;
        this.os = os;
    }

    public CompileTask(Profile profile) {
        switch (profile) {
            case LINUX:
                this.arch = Constants.ARCH_AMD64;
                this.vendor = Constants.VENDOR_LINUX;
                this.os = Constants.OS_LINUX;
                break;
            case LINUX_AARCH64:
                this.arch = Constants.ARCH_AARCH64;
                this.vendor = Constants.VENDOR_LINUX;
                this.os = Constants.OS_LINUX;
                break;
            case MACOS:
                this.arch = Constants.ARCH_AMD64;
                this.vendor = Constants.VENDOR_APPLE;
                this.os = Constants.OS_DARWIN;
                break;
            case MACOS_AARCH64:
                this.arch = Constants.ARCH_AARCH64;
                this.vendor = Constants.VENDOR_APPLE;
                this.os = Constants.OS_DARWIN;
                break;
            case WINDOWS:
                this.arch = Constants.ARCH_AMD64;
                this.vendor = Constants.VENDOR_MICROSOFT;
                this.os = Constants.OS_WINDOWS;
                break;
            case IOS:
                this.arch = Constants.ARCH_ARM64;
                this.vendor = Constants.VENDOR_APPLE;
                this.os = Constants.OS_IOS;
                break;
            case IOS_SIM:
                this.arch = Constants.ARCH_AMD64;
                this.vendor = Constants.VENDOR_APPLE;
                this.os = Constants.OS_IOS;
                break;
            case ANDROID:
                this.arch = Constants.ARCH_AARCH64;
                this.vendor = Constants.VENDOR_LINUX;
                this.os = Constants.OS_ANDROID;
                break;
            case WEB:
                this.arch = Constants.ARCH_AMD64;
                this.vendor = Constants.VENDOR_WEB;
                this.os = Constants.OS_WEB;
                break;
            default:
                throw new IllegalArgumentException("Triplet for profile " + profile + " is not supported yet");
        }
    }

    /*
     * check if this host can be used to provide binaries for this target.
     * host and target should not be null.
     */
    public boolean canCompileTo(CompileTask target) {
        // if the host os and target os are the same, always return true
        if (getOs().equals(target.getOs())) return true;

        // so far, iOS can be built from Mac, Android can be built from Linux
        return (Constants.OS_DARWIN.equals(getOs()) && Constants.OS_IOS.equals(target.getOs())) ||
                (Constants.OS_LINUX.equals(getOs()) && Constants.OS_ANDROID.equals(target.getOs()));
    }

    public String getArch() {
        return arch;
    }

    public void setArch(String arch) {
        this.arch = arch;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getArchOs() {
        return this.arch + "-" + this.os;
    }

    public String getOsArch() {
        return this.os + "-" + this.arch;
    }

    /**
     * returns os-arch but use amd64 instead of x86_64.
     * This should become the default
     *
     * @return
     */
    public String getOsArch2() {
        String myarch = this.arch;
        if (myarch.equals("x86_64")) {
            myarch = "amd64";
        }
        return this.os + "-" + myarch;
    }

    /**
     * On iOS/iOS-sim, Android and Linux-AArch64, it returns a string
     * with a valid version for clibs, for other OSes returns an empty string
     *
     * @return
     */
    public String getClibsVersion() {
        if (Constants.OS_IOS.equals(getOs()) || Constants.OS_ANDROID.equals(getOs()) ||
                (Constants.OS_LINUX.equals(getOs()) && Constants.ARCH_AARCH64.equals(getArch()))) {
            return "-ea+" + Constants.DEFAULT_CLIBS_VERSION;
        }
        return "";
    }

    /**
     * On iOS/iOS-sim, Android and Linux-AArch64, it returns a string
     * with a valid path for clibs, for other OSes returns an empty string
     *
     * @return
     */
    public String getClibsVersionPath() {
        if (Constants.OS_IOS.equals(getOs()) || Constants.OS_ANDROID.equals(getOs()) ||
                (Constants.OS_LINUX.equals(getOs()) && Constants.ARCH_AARCH64.equals(getArch()))) {
            return Constants.DEFAULT_CLIBS_VERSION;
        }
        return "";
    }

    @Override
    public String toString() {
        return arch + '-' + vendor + '-' + os;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CompileTask)) return false;
        CompileTask target = (CompileTask) o;
        return (this.arch.equals(target.arch) &&
                this.os.equals(target.os) &&
                this.vendor.equals(target.vendor));
    }


}
