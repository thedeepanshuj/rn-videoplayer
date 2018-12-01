package com.vdocipherdemo.vdocipher_offline;

import com.vdocipher.aegis.media.Track;
import com.vdocipher.aegis.offline.DownloadOptions;
import com.vdocipherdemo.Utils;

public class DownloadTrack {
    private Integer audioTrackIndex;
    private Integer videoTrackIndex;

    public DownloadTrack(Integer audioTrackIndex, Integer videoTrackIndex) {
        this.audioTrackIndex = audioTrackIndex;
        this.videoTrackIndex = videoTrackIndex;
    }

    public int[] selectedIndicesArray() {
        return new int[]{audioTrackIndex, videoTrackIndex};
    }

    public String getTrackName(DownloadOptions downloadOptions) {

        Track videoTrack = downloadOptions.availableTracks[videoTrackIndex];
        Track audioTrack = downloadOptions.availableTracks[audioTrackIndex];
        Long durationInMs = downloadOptions.mediaInfo.duration;

        Integer totalBitsPerSec = audioTrack.bitrate + videoTrack.bitrate;
        return videoTrack.height + "p ("+videoTrack.bitrate/1024+" kbps"+") - " + getTrackSizeInMB(totalBitsPerSec, durationInMs) + "MB";
    }

    public double getTrackSizeInMB(Integer bitsPerSec, Long durationInMs) {
        double sizeMB = ((double)bitsPerSec / (8 * 1024 * 1024)) * (durationInMs / 1000);
        return Utils.round(sizeMB, 2);
    }
}
