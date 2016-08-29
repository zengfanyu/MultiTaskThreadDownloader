package com.zfy.downloadkit;

import java.io.File;

/**
 * 下载请求类  <工厂设计模式>
 * Created by zfy on 2016/8/29.
 */
public class DownloadRequest {
    private String mUrl;
    private File mFolder;
    private CharSequence mName;
    private CharSequence mDescription;
    private boolean mScannable;

    public DownloadRequest(String url, File folder, CharSequence name, CharSequence description, boolean scannable) {
        mUrl = url;
        mFolder = folder;
        mName = name;
        mDescription = description;
        mScannable = scannable;
    }

    public String getUrl() {
        return mUrl;
    }

    public File getFolder() {
        return mFolder;
    }

    public CharSequence getName() {
        return mName;
    }

    public CharSequence getDescription() {
        return mDescription;
    }

    public boolean isScannable() {
        return mScannable;
    }


    public static class Builder{
        private String mUrl;
        private File mFolder;
        private CharSequence mName;
        private CharSequence mDescription;
        private boolean mScannable;

        public Builder setUrl(String url) {
            mUrl = url;
            return this;
        }

        public Builder setFolder(File folder) {
            mFolder = folder;
            return this;
        }

        public Builder setName(CharSequence name) {
            mName = name;
            return this;
        }

        public Builder setDescription(CharSequence description) {
            mDescription = description;
            return this;
        }

        public Builder setScannable(boolean scannable) {
            mScannable = scannable;
            return this;
        }

        public DownloadRequest build(){
            return  new DownloadRequest(mUrl, mFolder, mName, mDescription, mScannable);
}
    }
}
