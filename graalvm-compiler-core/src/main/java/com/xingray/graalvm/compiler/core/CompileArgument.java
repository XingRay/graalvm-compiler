package com.xingray.graalvm.compiler.core;

import com.xingray.graalvm.compiler.android.AndroidArgument;
import com.xingray.graalvm.compiler.common.CommonArgument;
import com.xingray.graalvm.compiler.ios.IosArgument;
import com.xingray.graalvm.compiler.linux.LinuxArgument;
import com.xingray.graalvm.compiler.macos.MacosArgument;
import com.xingray.graalvm.compiler.web.WebArgument;
import com.xingray.graalvm.compiler.windows.WindowsArgument;

public class CompileArgument {

    private CommonArgument commonArgument;
    private WindowsArgument windowsArgument;
    private LinuxArgument linuxArgument;
    private MacosArgument macosArgument;

    private WebArgument webArgument;

    private IosArgument iosArgument;

    private AndroidArgument androidArgument;


    public CommonArgument getCommonArgument() {
        return commonArgument;
    }

    public void setCommonArgument(CommonArgument commonArgument) {
        this.commonArgument = commonArgument;
    }

    public WindowsArgument getWindowsArgument() {
        return windowsArgument;
    }

    public void setWindowsArgument(WindowsArgument windowsArgument) {
        this.windowsArgument = windowsArgument;
    }

    public LinuxArgument getLinuxArgument() {
        return linuxArgument;
    }

    public void setLinuxArgument(LinuxArgument linuxArgument) {
        this.linuxArgument = linuxArgument;
    }

    public MacosArgument getMacosArgument() {
        return macosArgument;
    }

    public void setMacosArgument(MacosArgument macosArgument) {
        this.macosArgument = macosArgument;
    }

    public WebArgument getWebArgument() {
        return webArgument;
    }

    public void setWebArgument(WebArgument webArgument) {
        this.webArgument = webArgument;
    }

    public IosArgument getIosArgument() {
        return iosArgument;
    }

    public void setIosArgument(IosArgument iosArgument) {
        this.iosArgument = iosArgument;
    }

    public AndroidArgument getAndroidArgument() {
        return androidArgument;
    }

    public void setAndroidArgument(AndroidArgument androidArgument) {
        this.androidArgument = androidArgument;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        if (commonArgument != null) {
            sb.append("\"commonArgument\":").append(commonArgument).append(",");
        }
        if (windowsArgument != null) {
            sb.append("\"windowsArgument\":").append(windowsArgument).append(",");
        }
        if (linuxArgument != null) {
            sb.append("\"linuxArgument\":").append(linuxArgument).append(",");
        }
        if (macosArgument != null) {
            sb.append("\"macosArgument\":").append(macosArgument).append(",");
        }
        if (webArgument != null) {
            sb.append("\"webArgument\":").append(webArgument).append(",");
        }
        if (iosArgument != null) {
            sb.append("\"iosArgument\":").append(iosArgument).append(",");
        }
        if (androidArgument != null) {
            sb.append("\"androidArgument\":").append(androidArgument).append(",");
        }
        if (sb.lastIndexOf(",") != -1)
            sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append('}');
        return sb.toString();
    }
}
