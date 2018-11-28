package com.vdocipherdemo.vdocipher_offline;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.vdocipher.aegis.media.ErrorDescription;
import com.vdocipher.aegis.media.Track;
import com.vdocipher.aegis.offline.DownloadOptions;
import com.vdocipher.aegis.offline.DownloadRequest;
import com.vdocipher.aegis.offline.DownloadSelections;
import com.vdocipher.aegis.offline.DownloadStatus;
import com.vdocipher.aegis.offline.OptionsDownloader;
import com.vdocipher.aegis.offline.VdoDownloadManager;
import com.vdocipherdemo.Constants;
import com.vdocipherdemo.shared_components.activities.PlayerActivity;

import java.io.File;
import java.util.List;

import static com.vdocipherdemo.Utils.isInternetAvailable;

public class VdoCipherOfflineModule extends ReactContextBaseJavaModule {

    private static final String MODULE_VDOCIPHER = "VdoCipherOfflineModule";
    private static final int METHOD_DOWNLOAD = 1;
    private static final int METHOD_PLAY = 2;
    private static final int METHOD_DELETE = 3;

    private VdoDownloadManager vdoDownloadManager;
    private boolean isVdoDownloaded = false;
    private boolean isDownloading = false;
    private DownloadStatus caseDownloadStatus = null;

    public VdoCipherOfflineModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return MODULE_VDOCIPHER;
    }

    @ReactMethod
    public void delete() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initVdoCipherAttributes(METHOD_DELETE);
        } else {
            showToast(getReactApplicationContext(),"Minimum api level required is 21");
        }

    }

    @ReactMethod
    public void play() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initVdoCipherAttributes(METHOD_PLAY);
        } else {
            showToast(getReactApplicationContext(),"Minimum api level required is 21");
        }

    }

    @ReactMethod
    public void download(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initVdoCipherAttributes(METHOD_DOWNLOAD);
        } else {
            showToast(getReactApplicationContext(),"Minimum api level required is 21");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initVdoCipherAttributes(final int sourceMethod) {
        if (vdoDownloadManager==null) {
            vdoDownloadManager = VdoDownloadManager.getInstance(getReactApplicationContext());
        }

        isVdoDownloaded = false;
        isDownloading = false;
        caseDownloadStatus = null;

        vdoDownloadManager.query(new VdoDownloadManager.Query(), new VdoDownloadManager.QueryResultListener() {
            @Override
            public void onQueryResult(List<DownloadStatus> list) {
                for (DownloadStatus status: list) {
                    if(status.mediaInfo.mediaId.equals(Constants.MEDIA_ID)){
                        caseDownloadStatus = status;
                        if(status.status == VdoDownloadManager.STATUS_COMPLETED) isVdoDownloaded = true;
                        else isDownloading = true;
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
            getOptions();
        }
    }

    private void playPostQuery(){
        if (isVdoDownloaded) {
            Intent intent = new Intent(getReactApplicationContext(), PlayerActivity.class);
            intent.putExtra(Constants.PLAY_TYPE, Constants.PLAY_OFFLINE);
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

    private void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void getOptions() {
        new OptionsDownloader().downloadOptionsWithOtp(Constants.OTP, Constants.PLAYBACK_INFO, new OptionsDownloader.Callback() {
            @Override
            public void onOptionsReceived(DownloadOptions downloadOptions) {
                Integer audioIndex = -1;
                Integer audioMaxBitrate = 0;
                Integer videoIndex = -1;
                Integer videoMaxBitrate = 0;
                for(int i=0; i<downloadOptions.availableTracks.length; i++) {
                    Track track = downloadOptions.availableTracks[i];
                    if (track.type == Track.TYPE_AUDIO && track.bitrate > audioMaxBitrate) {
                        audioIndex = i;
                    } else if (track.type == Track.TYPE_VIDEO && track.bitrate > videoMaxBitrate) {
                        videoIndex = i;
                    }

                }

                int [] selectedIndices = new int[]{audioIndex, videoIndex};
                downloadSelectedOptions(downloadOptions, selectedIndices);

            }

            @Override
            public void onOptionsNotReceived(ErrorDescription errorDescription) {

            }
        });
    }

    private void downloadSelectedOptions(DownloadOptions downloadOptions, int[] selectionIndices) {
        DownloadSelections selections = new DownloadSelections(downloadOptions, selectionIndices);

        if (!isExternalStorageWritable()) {
            Toast.makeText(getReactApplicationContext(), "External storage is not available", Toast.LENGTH_LONG).show();
            return;
        }

        String downloadLocation;
        try {
            downloadLocation = getReactApplicationContext().getExternalFilesDir(null).getPath() + File.separator + "offlineVdos";
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
        DownloadRequest request = new DownloadRequest.Builder(selections, downloadLocation).build();

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

}
