package com.vdocipherdemo.play_online;

import android.content.Intent;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.vdocipherdemo.Constants;
import com.vdocipherdemo.shared_components.activities.PlayerActivity;

public class PlayOnlineModule extends ReactContextBaseJavaModule {

    private final static String MODULE_PLAY_ONLINE = "PlayOnlineModule";

    @Override
    public String getName() {
        return MODULE_PLAY_ONLINE;
    }

    public PlayOnlineModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @ReactMethod
    public void play() {
        Toast.makeText(getReactApplicationContext(), MODULE_PLAY_ONLINE, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getReactApplicationContext(), PlayerActivity.class);
        intent.putExtra(Constants.PLAY_TYPE, Constants.PLAY_ONLINE);
        getReactApplicationContext().startActivity(intent);

    }


}
