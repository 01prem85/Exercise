package com.alpsmobi.mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FileDownload implements Parcelable {

    private int downloadProgress;
    private int currentFileSize;
    private int totalFileSize;

    public FileDownload() {
    }

    private FileDownload(Parcel parcel) {
        downloadProgress = parcel.readInt();
        currentFileSize = parcel.readInt();
        totalFileSize = parcel.readInt();
    }

    public static final Creator<FileDownload> CREATOR = new Creator<FileDownload>() {
        @Override
        public FileDownload createFromParcel(Parcel parcel) {
            return new FileDownload(parcel);
        }

        @Override
        public FileDownload[] newArray(int size) {
            return new FileDownload[size];
        }
    };

    public int getDownloadProgress() {
        return downloadProgress;
    }

    public void setDownloadProgress(int downloadProgress) {
        this.downloadProgress = downloadProgress;
    }

    public int getCurrentFileSize() {
        return currentFileSize;
    }

    public void setCurrentFileSize(int currentFileSize) {
        this.currentFileSize = currentFileSize;
    }

    public int getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(int totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(downloadProgress);
        parcel.writeInt(currentFileSize);
        parcel.writeInt(totalFileSize);
    }
}
