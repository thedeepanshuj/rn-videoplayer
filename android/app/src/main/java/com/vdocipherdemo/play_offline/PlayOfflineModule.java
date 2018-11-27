package com.vdocipherdemo.play_offline;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.vdocipher.aegis.offline.DownloadStatus;
import com.vdocipher.aegis.offline.VdoDownloadManager;
import com.vdocipherdemo.Constants;
import com.vdocipherdemo.shared_components.activities.PlayerActivity;

import java.util.List;

public class PlayOfflineModule extends ReactContextBaseJavaModule {

    private static final String MODULE_PLAY_OFFLINE = "PlayOfflineModule";
    private VdoDownloadManager vdoDownloadManager;

    public PlayOfflineModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }
    private boolean isVdoDownloaded = false;
    private boolean isDownloading = false;
    private DownloadStatus caseDownloadStatus = null;

    @Override
    public String getName() {
        return MODULE_PLAY_OFFLINE;
    }

    @ReactMethod
    public void play(){
        Toast.makeText(getReactApplicationContext(), MODULE_PLAY_OFFLINE, Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initializeVdoDownloadManager();
            updateVdoDownloadStatus();
            if (isVdoDownloaded) {
                Intent intent = new Intent(getReactApplicationContext(), PlayerActivity.class);
                intent.putExtra(Constants.PLAY_TYPE, Constants.PLAY_OFFLINE);
                getReactApplicationContext().startActivity(intent);
            } else if (isDownloading) {
                Toast.makeText(getReactApplicationContext(), "Downloaded " + caseDownloadStatus.downloadPercent + "%", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getReactApplicationContext(), "Vdo not yet saved offline", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getReactApplicationContext(),"Minimum api level required is 21", Toast.LENGTH_LONG).show();
        }
    }

    private void updateVdoDownloadStatus() {
        vdoDownloadManager.query(new VdoDownloadManager.Query(), new VdoDownloadManager.QueryResultListener() {
            @Override
            public void onQueryResult(List<DownloadStatus> list) {
                for (DownloadStatus status: list) {
                    if(status.mediaInfo.mediaId.equals(Constants.MEDIA_ID)){
                        caseDownloadStatus = status;

                        if (status.status==VdoDownloadManager.STATUS_COMPLETED) {
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
