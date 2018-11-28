package com.vdocipherdemo.save_offline;


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

import java.io.File;
import java.util.List;

public class SaveOfflineModule extends ReactContextBaseJavaModule {

    private static final String MODULE_SAVE_OFFLINE = "SaveOfflineModule";
    private VdoDownloadManager vdoDownloadManager;

    public SaveOfflineModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }
    private boolean isVdoDownloaded = false;
    private boolean isDownloading = false;
    private DownloadStatus caseDownloadStatus = null;


    @Override
    public String getName() {
        return MODULE_SAVE_OFFLINE;
    }

    @ReactMethod
    public void download(){
        Toast.makeText(getReactApplicationContext(), MODULE_SAVE_OFFLINE, Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initializeVdoDownloadManager();
            updateVdoDownloadStatus();
            if (isVdoDownloaded) {
                Toast.makeText(getReactApplicationContext(), "Vdo already saved offline", Toast.LENGTH_SHORT).show();
            } else if (isDownloading){
                Toast.makeText(getReactApplicationContext(), "Downloaded " + caseDownloadStatus.downloadPercent + "%", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getReactApplicationContext(), "Will download the video", Toast.LENGTH_SHORT).show();
                getOptions();
            }
        } else {
            Toast.makeText(getReactApplicationContext(),"Minimum api level required is 21", Toast.LENGTH_LONG).show();
        }


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
                Toast.makeText(getReactApplicationContext(), "failed to create storage directory", Toast.LENGTH_LONG).show();
                return;
            }
        }


        // build a DownloadRequest
        DownloadRequest request = new DownloadRequest.Builder(selections, downloadLocation).build();

        // enqueue request to VdoDownloadManager for download
        try {
            vdoDownloadManager.enqueue(request);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Toast.makeText(getReactApplicationContext(), "error enqueuing download request", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private void updateVdoDownloadStatus() {
        vdoDownloadManager.query(new VdoDownloadManager.Query(), new VdoDownloadManager.QueryResultListener() {
            @Override
            public void onQueryResult(List<DownloadStatus> list) {
                for (DownloadStatus status: list) {
                    if(status.mediaInfo.mediaId.equals(Constants.MEDIA_ID)){
                        caseDownloadStatus = status;
                        if(status.status == VdoDownloadManager.STATUS_COMPLETED) {
                            isVdoDownloaded = true;
                        } else {
                            isDownloading = true;
                        }
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initializeVdoDownloadManager() {
        if (vdoDownloadManager==null) {
            vdoDownloadManager = VdoDownloadManager.getInstance(getReactApplicationContext());
        }
    }





}
