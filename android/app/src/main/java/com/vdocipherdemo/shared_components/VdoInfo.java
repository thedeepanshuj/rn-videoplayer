package com.vdocipherdemo.shared_components;

import java.io.Serializable;

public class VdoInfo implements Serializable {
    private String otp;
    private String playbackInfo;
    private String mediaId;
    private String name;
    private Integer vdoId;
    private Integer batchId;

    public VdoInfo(String otp, String playbackInfo, String mediaId, String name, Integer vdoId, Integer batchId) {
        this.otp = otp;
        this.playbackInfo = playbackInfo;
        this.mediaId = mediaId;
        this.name = name;
        this.vdoId = vdoId;
        this.batchId = batchId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getPlaybackInfo() {
        return playbackInfo;
    }

    public void setPlaybackInfo(String playbackInfo) {
        this.playbackInfo = playbackInfo;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVdoId() {
        return vdoId;
    }

    public void setVdoId(Integer vdoId) {
        this.vdoId = vdoId;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }
}
