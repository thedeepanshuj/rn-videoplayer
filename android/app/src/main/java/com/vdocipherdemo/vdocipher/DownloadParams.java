package com.vdocipherdemo.vdocipher;

import com.vdocipher.aegis.offline.DownloadOptions;
import com.vdocipherdemo.vdocipher_offline.DownloadTrack;

public class DownloadParams {
    private DownloadOptions downloadOptions;
    private DownloadTrack selectedTrackOption;

    public DownloadParams(DownloadOptions downloadOptions, DownloadTrack selectedTrackOption) {
        this.downloadOptions = downloadOptions;
        this.selectedTrackOption = selectedTrackOption;
    }

    public DownloadOptions getDownloadOptions() {
        return downloadOptions;
    }

    public void setDownloadOptions(DownloadOptions downloadOptions) {
        this.downloadOptions = downloadOptions;
    }

    public DownloadTrack getSelectedTrackOption() {
        return selectedTrackOption;
    }

    public void setSelectedTrackOption(DownloadTrack selectedTrackOption) {
        this.selectedTrackOption = selectedTrackOption;
    }
}
