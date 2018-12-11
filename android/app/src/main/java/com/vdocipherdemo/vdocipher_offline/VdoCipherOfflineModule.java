package com.vdocipherdemo.vdocipher_offline;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
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
import com.vdocipherdemo.Constants;
import com.vdocipherdemo.R;
import com.vdocipherdemo.shared_components.vdo_player.PlayerActivity;
import com.vdocipherdemo.shared_components.VdoInfo;
import com.vdocipherdemo.vdocipher.DownloadEvents;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.vdocipherdemo.Utils.isInternetAvailable;

public class VdoCipherOfflineModule extends ReactContextBaseJavaModule implements VdoDownloadManager.EventListener {

    private static final String MODULE_VDOCIPHER_OFFLINE = "VdoCipherOfflineModule";
    private static final int METHOD_DOWNLOAD = 1;
    private static final int METHOD_PLAY = 2;
    private static final int METHOD_DELETE = 3;
    private static final int PROGRESS_MAX = 100;

    private VdoDownloadManager vdoDownloadManager;
    private boolean isVdoDownloaded = false;
    private boolean isDownloading = false;
    private DownloadStatus caseDownloadStatus = null;
    private VdoInfo vdoInfo;

    NotificationCompat.Builder notificationBuilder = null;
    NotificationChannel notificationChannel = null;

    public VdoCipherOfflineModule(ReactApplicationContext reactContext) {
        super(reactContext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { initNotificationChannel(); }
    }

    @Override
    public String getName() {
        return MODULE_VDOCIPHER_OFFLINE;
    }

    @ReactMethod
    public void delete(String vdoInfoString) {
        vdoInfo = new Gson().fromJson(vdoInfoString, VdoInfo.class);
        if (isSupportedSDK()) {

            initVdoCipherAttributes(METHOD_DELETE);
            initNotificationBuilder(getReactApplicationContext());
        } else
            showToast(getReactApplicationContext(),"Minimum api level required is 21");
    }

    @ReactMethod
    public void play(String vdoInfoString) {
        vdoInfo = new Gson().fromJson(vdoInfoString, VdoInfo.class);
        if (isSupportedSDK())
            initVdoCipherAttributes(METHOD_PLAY);
        else
            showToast(getReactApplicationContext(),"Minimum api level required is 21");

    }

    @ReactMethod
    public void download(String vdoInfoString){
        vdoInfo = new Gson().fromJson(vdoInfoString, VdoInfo.class);
        if (isSupportedSDK()) {
            initVdoCipherAttributes(METHOD_DOWNLOAD);
            initNotificationBuilder(getReactApplicationContext());
        } else
            showToast(getReactApplicationContext(),"Minimum api level required is 21");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initVdoCipherAttributes(final int sourceMethod) {
        if (vdoDownloadManager==null){
            vdoDownloadManager = VdoDownloadManager.getInstance(getReactApplicationContext());
            vdoDownloadManager.addEventListener(this);
        }

        isVdoDownloaded = false;
        isDownloading = false;
        caseDownloadStatus = null;

        vdoDownloadManager.query(new VdoDownloadManager.Query(), new VdoDownloadManager.QueryResultListener() {
            @Override
            public void onQueryResult(List<DownloadStatus> list) {
                for (DownloadStatus status: list) {
                    if(status.mediaInfo.mediaId.equals(vdoInfo.getMediaId())){
                        caseDownloadStatus = status;
                        if(status.status == VdoDownloadManager.STATUS_COMPLETED)
                            isVdoDownloaded = true;
                        else
                            isDownloading = true;
                    }
                }

                switch(sourceMethod) {
                    case METHOD_DOWNLOAD:   downloadPostQuery();    break;
                    case METHOD_PLAY:   playPostQuery();    break;
                    case METHOD_DELETE:     deletePostQuery();  break;
                }
            }
        });
    }

    private void downloadPostQuery() {
        if (isVdoDownloaded) {
            showToast(getReactApplicationContext(), "Vdo already saved offline");
        } else if (isDownloading){
            showToast(getReactApplicationContext(), "Downloading at " + caseDownloadStatus.downloadPercent + "%");
        } else {
            if (!isInternetAvailable()) {
                showToast(getReactApplicationContext(), "Check Internet connectivity");
                return;
            }
            showToast(getReactApplicationContext(), "Will download the video");
            getOptionsAndInitiateDownload(vdoInfo);
        }
    }

    private void playPostQuery(){
        if (isVdoDownloaded) {
            Intent intent = new Intent(getReactApplicationContext(), PlayerActivity.class);
            intent.putExtra(Constants.PLAY_TYPE, Constants.PLAY_OFFLINE);
            intent.putExtra(Constants.VDO_INFO, vdoInfo);
            getReactApplicationContext().startActivity(intent);
        } else if (isDownloading) {
            showToast(getReactApplicationContext(), "Downloaded " + caseDownloadStatus.downloadPercent + "%");
        }
        else {
            showToast(getReactApplicationContext(), "Vdo not yet saved offline");
        }

    }

    private void deletePostQuery() {
        if (isVdoDownloaded) {
            showToast(getReactApplicationContext(), "Will delete the video");
            vdoDownloadManager.remove(caseDownloadStatus.mediaInfo.mediaId);
        } else if (isDownloading){
            showToast(getReactApplicationContext(), "Video is still downloading");
        } else {
            showToast(getReactApplicationContext(), "Video not downloaded or already deleted");

        }

    }

    private void getOptionsAndInitiateDownload(VdoInfo vdoDetails) {
        new OptionsDownloader().downloadOptionsWithOtp(vdoDetails.getOtp(), vdoDetails.getPlaybackInfo(), new OptionsDownloader.Callback() {
            @Override
            public void onOptionsReceived(DownloadOptions downloadOptions) {
                List<DownloadTrack> downloadTracks = getDownloadTracksFromDownloadOptions(downloadOptions);
                showSelectOptionDialog(downloadOptions, downloadTracks);
            }

            @Override
            public void onOptionsNotReceived(ErrorDescription errorDescription) {
                Log.d(MODULE_VDOCIPHER_OFFLINE, errorDescription.errorMsg);
            }
        });
    }

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

    private void showSelectOptionDialog(DownloadOptions downloadOptions, List<DownloadTrack> downloadTracks) {
        OptionSelector optionSelector = new OptionSelector(downloadOptions, downloadTracks, new OnOptionSelectedListener() {
            @Override
            public void onOptionSelected(DownloadOptions downloadOptions, DownloadTrack selectedTrack) {
                if (selectedTrack == null)
                    showToast(getReactApplicationContext(), "Select an option first first");
                else
                    downloadSelectedOptions(downloadOptions, selectedTrack.selectedIndicesArray());
            }
        });
        optionSelector.showSelectionDialog(getCurrentActivity(), "Download");
    }

    private void downloadSelectedOptions(DownloadOptions downloadOptions, int[] selectionIndices) {
        DownloadSelections selections = new DownloadSelections(downloadOptions, selectionIndices);

        if (!isExternalStorageWritable()) {
            Toast.makeText(getReactApplicationContext(), "External storage is not available", Toast.LENGTH_LONG).show();
            return;
        }

        String downloadLocation;
        try {
            downloadLocation = getReactApplicationContext().getExternalFilesDir(null).getPath() + File.separator + vdoInfo.getVdoId();
        } catch (NullPointerException npe) {
            Toast.makeText(getReactApplicationContext(), "external storage not available", Toast.LENGTH_LONG).show();
            return;
        }

        // ensure download directory is created
        File dlLocation = new File(downloadLocation);
        if (!(dlLocation.exists() && dlLocation.isDirectory())) {
            // directory not created yet; let's create it
            if (!dlLocation.mkdir()) {
                showToast(getReactApplicationContext(), "failed to create storage directory");
                return;
            }
        }


        // build a DownloadRequest
        DownloadRequest request = new DownloadRequest.Builder(selections, downloadLocation)
                .setNotificationDescription(downloadOptions.mediaInfo.description)
                .setNotificationTitle(downloadOptions.mediaInfo.title).build();

        // enqueue request to VdoDownloadManager for download
        try {
            vdoDownloadManager.enqueue(request);
        } catch (IllegalArgumentException | IllegalStateException e) {
            showToast(getReactApplicationContext(), "error enqueuing download request");
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private boolean isSupportedSDK() { return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP; }

    private void showToast(Context context, String message) { Toast.makeText(context, message, Toast.LENGTH_SHORT).show(); }

    @Override
    public void onQueued(String mediaId, DownloadStatus downloadStatus) {
        Context context = getReactApplicationContext();
        showNotification(context, vdoInfo.getVdoId(), notificationBuilder, "Download Queued", vdoInfo.getName(), null);
        showToast(context, "Queued");
        WritableMap params = Arguments.createMap();
        params.putString("mediaId", mediaId);
        sendEvent(getReactApplicationContext(), DownloadEvents.QUEUED, params);
    }

    @Override
    public void onChanged(String mediaId, DownloadStatus downloadStatus) {
        Context context = getReactApplicationContext();
        showNotification(context, vdoInfo.getVdoId(), notificationBuilder, "Downloading", vdoInfo.getName(), downloadStatus.downloadPercent);
        showToast(context, "Downloading at " + downloadStatus.downloadPercent +" %");

        WritableMap params = Arguments.createMap();
        params.putString("mediaId", mediaId);
        params.putInt("progress", downloadStatus.downloadPercent);
        sendEvent(getReactApplicationContext(), DownloadEvents.PROGRESS, params);
    }

    @Override
    public void onCompleted(String mediaId, DownloadStatus downloadStatus) {
        Context context = getReactApplicationContext();
        showNotification(context, vdoInfo.getVdoId(), notificationBuilder, "Download Completed", vdoInfo.getName(), null);
        showToast(context, "Download Finished");
        vdoDownloadManager.removeEventListener(this);
        vdoDownloadManager = null;
        WritableMap params = Arguments.createMap();
        params.putString("mediaId", mediaId);
        sendEvent(getReactApplicationContext(), DownloadEvents.COMPLETED, params);
    }

    @Override
    public void onFailed(String mediaId, DownloadStatus downloadStatus) {
        Context context = getReactApplicationContext();
        showNotification(context, vdoInfo.getVdoId(), notificationBuilder, "Download Failed", vdoInfo.getName(), null);
        showToast(context, "Failed : " + downloadStatus.reason);
        WritableMap params = Arguments.createMap();
        params.putString("mediaId", mediaId);
        params.putInt("reason", downloadStatus.reason);
        sendEvent(getReactApplicationContext(), DownloadEvents.FAILED, params);
    }

    @Override
    public void onDeleted(String mediaId) {
        Context context = getReactApplicationContext();
        showNotification(context, vdoInfo.getVdoId(), notificationBuilder, "Video Deleted", vdoInfo.getName(), null);
        showToast(context, "Deleted");
        vdoDownloadManager.removeEventListener(this);
        vdoDownloadManager = null;
        WritableMap params = Arguments.createMap();
        params.putString("mediaId", mediaId);
        sendEvent(getReactApplicationContext(), DownloadEvents.DELETED, params);

    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    private void initNotificationBuilder(Context context){
        if (notificationBuilder == null) notificationBuilder = new NotificationCompat.Builder(context, "default").setSmallIcon(R.drawable.logo_thumb);
    }

    private void showNotification(Context context, int notificationId, NotificationCompat.Builder notificationBuilder, String contentTitle, String contextText, Integer progress){

        initNotificationBuilder(context);

        notificationBuilder.setContentTitle(contentTitle).setContentText(contextText);
        if (progress == null)
            notificationBuilder.setProgress(0, 0, false);
        else
            notificationBuilder.setProgress(PROGRESS_MAX, progress, false);



        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initNotificationChannel();
            notificationManager.createNotificationChannel(notificationChannel);
        }

        
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initNotificationChannel() {
        notificationChannel = new NotificationChannel("default", "DownloadStatus", NotificationManager.IMPORTANCE_DEFAULT);
    }

}
