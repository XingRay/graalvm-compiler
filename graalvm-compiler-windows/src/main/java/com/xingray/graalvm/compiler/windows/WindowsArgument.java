package com.xingray.graalvm.compiler.windows;

import com.xingray.graalvm.compiler.common.CommonArgument;

import java.nio.file.Path;
import java.util.List;

public class WindowsArgument {

    private CommonArgument commonArgument;
    private boolean enableSWRendering;
    private List<Release> releaseList;

    private Path platformOutputPath;

    public boolean isEnableSWRendering() {
        return enableSWRendering;
    }

    public CommonArgument getCommonArgument() {
        return commonArgument;
    }

    public void setCommonArgument(CommonArgument commonArgument) {
        this.commonArgument = commonArgument;
    }

    public void setEnableSWRendering(boolean enableSWRendering) {
        this.enableSWRendering = enableSWRendering;
    }

    public List<Release> getReleaseList() {
        return releaseList;
    }

    public void setReleaseList(List<Release> releaseList) {
        this.releaseList = releaseList;
    }

    public Path getPlatformOutputPath() {
        return platformOutputPath;
    }

    public void setPlatformOutputPath(Path platformOutputPath) {
        this.platformOutputPath = platformOutputPath;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        if (commonArgument != null) {
            sb.append("\"commonArgument\":").append(commonArgument).append(",");
        }
        sb.append("\"enableSWRendering\":").append(enableSWRendering).append(",");
        if (releaseList != null) {
            sb.append("\"releaseList\":").append(releaseList).append(",");
        }
        if (sb.lastIndexOf(",") != -1)
            sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append('}');
        return sb.toString();
    }

    public static class Release {
        private String packageType;
        private String name;
        private String id;
        private String description;
        private String vendor;
        private String versionName;
        private int versionCode;

        public String getPackageType() {
            return packageType;
        }

        public void setPackageType(String packageType) {
            this.packageType = packageType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVendor() {
            return vendor;
        }

        public void setVendor(String vendor) {
            this.vendor = vendor;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("{");
            if (packageType != null) {
                sb.append("\"packageType\":\"").append(packageType).append("\",");
            }
            if (name != null) {
                sb.append("\"name\":\"").append(name).append("\",");
            }
            if (id != null) {
                sb.append("\"id\":\"").append(id).append("\",");
            }
            if (description != null) {
                sb.append("\"description\":\"").append(description).append("\",");
            }
            if (vendor != null) {
                sb.append("\"vendor\":\"").append(vendor).append("\",");
            }
            if (versionName != null) {
                sb.append("\"versionName\":\"").append(versionName).append("\",");
            }
            sb.append("\"versionCode\":").append(versionCode).append(",");
            if (sb.lastIndexOf(",") != -1)
                sb.deleteCharAt(sb.lastIndexOf(","));
            sb.append('}');
            return sb.toString();
        }
    }
}
