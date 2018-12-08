package com.vdocipherdemo.vdocipher;

import com.vdocipher.aegis.offline.DownloadOptions;
import com.vdocipherdemo.vdocipher_offline.DownloadTrack;

import java.util.List;

public class DownloadDialogOptions {

    DownloadOptions downloadOptions;
    List<DownloadTrack> downloadTracks;

    public DownloadDialogOptions(DownloadOptions downloadOptions, List<DownloadTrack> downloadTracks) {
        this.downloadOptions = downloadOptions;
        this.downloadTracks = downloadTracks;
    }

    public DownloadOptions getDownloadOptions() {
        return downloadOptions;
    }

    public void setDownloadOptions(DownloadOptions downloadOptions) {
        this.downloadOptions = downloadOptions;
    }

    public List<DownloadTrack> getDownloadTracks() {
        return downloadTracks;
    }

    public void setDownloadTracks(List<DownloadTrack> downloadTracks) {
        this.downloadTracks = downloadTracks;
    }
}
