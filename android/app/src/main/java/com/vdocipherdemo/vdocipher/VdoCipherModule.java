package com.vdocipherdemo.vdocipher;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;


import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.gson.Gson;
import com.vdocipher.aegis.media.ErrorDescription;
import com.vdocipher.aegis.media.Track;
import com.vdocipher.aegis.offline.DownloadOptions;
import com.vdocipher.aegis.offline.DownloadRequest;
import com.vdocipher.aegis.offline.DownloadSelections;
import com.vdocipher.aegis.offline.DownloadStatus;
import com.vdocipher.aegis.offline.OptionsDownloader;
import com.vdocipher.aegis.offline.VdoDownloadManager;
import com.vdocipherdemo.Utils;
import com.vdocipherdemo.shared_components.vdo_player.VdoInfo;
import com.vdocipherdemo.vdocipher_offline.DownloadTrack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VdoCipherModule extends ReactContextBaseJavaModule implements VdoDownloadManager.EventListener {

    private final static String TAG = "VdoCipherModule";

    private Gson gson = new Gson();
    private VdoInfo vdoInfo;
    private VdoDownloadManager vdoDownloadManager;

    public VdoCipherModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return TAG;
    }

    @ReactMethod
    public void getDownloadOptions(String vdoInfoString, Promise promise){
        vdoInfo = gson.fromJson(vdoInfoString, VdoInfo.class);
        if (isSupportedSDK()){
            getDownloadOptions(vdoInfo, promise);
        } else {
            promise.reject(Errors.UNSUPPORTED_SDK_ERROR, Errors.UNSUPPORTED_SDK_ERROR_MESSAGE);
            return;
        }

    }

    @ReactMethod
    private void download(String downloadParamsString, Promise promise){
        if (isSupportedSDK()){
            initVdoDownloadManager(getReactApplicationContext());
            DownloadParams downloadParams = gson.fromJson(downloadParamsString, DownloadParams.class);
            DownloadOptions downloadOptions = downloadParams.getDownloadOptions();
            int [] selectedIndices = downloadParams.getSelectedTrackOption().selectedIndicesArray();
            downloadSelectedOptions(downloadOptions, selectedIndices, promise);
        } else {
            promise.reject(Errors.UNSUPPORTED_SDK_ERROR, Errors.UNSUPPORTED_SDK_ERROR_MESSAGE);
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initVdoDownloadManager(Context context) {
        vdoDownloadManager = VdoDownloadManager.getInstance(getReactApplicationContext());
    }

    private void getDownloadOptions(VdoInfo vdoInfo, final Promise promise) {
        new OptionsDownloader().downloadOptionsWithOtp(vdoInfo.getOtp(), vdoInfo.getPlaybackInfo(), new OptionsDownloader.Callback() {
            @Override
            public void onOptionsReceived(DownloadOptions downloadOptions) {
                List<DownloadTrack> downloadTracks = getDownloadTracksFromDownloadOptions(downloadOptions);
                if (downloadTracks != null){
                    try {
                        DownloadDialogOptions downloadDialogOptions = new DownloadDialogOptions(downloadOptions, downloadTracks);
                        String jsonString = gson.toJson(downloadDialogOptions);
                        JSONObject jsonObject = new JSONObject(jsonString);
                        WritableMap writableMap = Utils.convertJsonToMap(jsonObject);
                        promise.resolve(writableMap);
                        return;
                    } catch (JSONException e) {
                        promise.reject(Errors.JSON_EXCEPTION, "JsonException");
                        return;
                    }
                } else {
                    promise.reject(Errors.NO_AVAILABLE_TRACK_ERROR, Errors.NO_AVAILABLE_TRACK_MESSAGE);
                    return;
                }
            }

            @Override
            public void onOptionsNotReceived(ErrorDescription errorDescription) {
                final String message = errorDescription.errorCode + " : " + errorDescription.errorMsg;
                promise.reject(Errors.OPTIONS_NOT_RECEIVED, message);
            }
        });
    }


    private boolean isSupportedSDK() { return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP; }

    private List<DownloadTrack> getDownloadTracksFromDownloadOptions(DownloadOptions downloadOptions) {
        int audioIndex = -1;
        int maxAudioBitrate = 0;
        List<Integer> videoIndices = new ArrayList<>();

        Track[] availableTracks = downloadOptions.availableTracks;
        for (int i=0; i<availableTracks.length; i++) {
            Track currentTrack = availableTracks[i];
            if(currentTrack.type == Track.TYPE_VIDEO)
                videoIndices.add(i);
            else if (currentTrack.type == Track.TYPE_AUDIO) {
                if (currentTrack.bitrate > maxAudioBitrate) {
                    maxAudioBitrate = currentTrack.bitrate;
                    audioIndex = i;
                }
            }
        }
        if (videoIndices.size() > 0) {
            List<DownloadTrack> downloadTracks = new ArrayList<>();
            for(int videoIndex: videoIndices) {
                DownloadTrack downloadTrack = new DownloadTrack(audioIndex, videoIndex);
                downloadTrack.setTrackName(downloadOptions);
                downloadTracks.add(downloadTrack);
            }
            return downloadTracks;
        }

        return null;
    }

    private void downloadSelectedOptions(DownloadOptions downloadOptions, int[] selectionIndices, Promise promise) {
        DownloadSelections selections = new DownloadSelections(downloadOptions, selectionIndices);

        if (!isExternalStorageWritable()) {
            promise.reject(Errors.EXTERNAL_STORAGE_UNAVAILABLE_ERROR, Errors.EXTERNAL_STORAGE_UNAVAILABLE_MESSAGE);
            return;
        }

        String downloadLocation;
        try {
            downloadLocation = getReactApplicationContext().getExternalFilesDir(null).getPath() + File.separator + "offline";
        } catch (NullPointerException npe) {
            promise.reject(Errors.EXTERNAL_STORAGE_UNAVAILABLE_ERROR, Errors.EXTERNAL_STORAGE_UNAVAILABLE_MESSAGE);
            return;
        }

        // ensure download directory is created
        File dlLocation = new File(downloadLocation);
        if (!(dlLocation.exists() && dlLocation.isDirectory())) {
            // directory not created yet; let's create it
            if (!dlLocation.mkdir()) {
                promise.reject(Errors.CREATE_DIRECTORY_ERROR, Errors.CREATE_DIRECTORY_MESSAGE);
                return;
            }
        }

        // build a DownloadRequest
        DownloadRequest request = new DownloadRequest.Builder(selections, downloadLocation).build();

        // enqueue request to VdoDownloadManager for download
        try {
            vdoDownloadManager.enqueue(request);
        } catch (IllegalArgumentException | IllegalStateException e) {
            promise.reject(Errors.ENQUEUE_FAILED_ERROR, Errors.ENQUEUE_FAILED_MESSAGE);
            return;

        }
        promise.resolve(null);
        return;
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }



    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    @Override
    public void onQueued(String mediaId, DownloadStatus downloadStatus) {
        WritableMap params = Arguments.createMap();
        params.putString("mediaId", mediaId);
        sendEvent(getReactApplicationContext(), DownloadEvents.QUEUED, params);
    }

    @Override
    public void onChanged(String mediaId, DownloadStatus downloadStatus) {
        WritableMap params = Arguments.createMap();
        params.putString("mediaId", mediaId);
        params.putInt("progress", downloadStatus.downloadPercent);
        sendEvent(getReactApplicationContext(), DownloadEvents.PROGRESS, params);
    }

    @Override
    public void onCompleted(String mediaId, DownloadStatus downloadStatus) {
        WritableMap params = Arguments.createMap();
        params.putString("mediaId", mediaId);
        sendEvent(getReactApplicationContext(), DownloadEvents.COMPLETED, params);

    }

    @Override
    public void onFailed(String mediaId, DownloadStatus downloadStatus) {
        WritableMap params = Arguments.createMap();
        params.putString("mediaId", mediaId);
        params.putInt("reason", downloadStatus.reason);
        sendEvent(getReactApplicationContext(), DownloadEvents.FAILED, params);

    }

    @Override
    public void onDeleted(String mediaId) {
        WritableMap params = Arguments.createMap();
        params.putString("mediaId", mediaId);
        sendEvent(getReactApplicationContext(), DownloadEvents.DELETED, params);

    }
}
