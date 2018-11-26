package com.vdocipherdemo.play_online;

import android.content.Intent;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class PlayOnlineModule extends ReactContextBaseJavaModule {

    public final static String MODULE_ONLINE = "PlayOnlineModule";

    @Override
    public String getName() {
        return MODULE_ONLINE;
    }

    public PlayOnlineModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @ReactMethod
    public void play() {
        Toast.makeText(getReactApplicationContext(), MODULE_ONLINE, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getReactApplicationContext(), PlayOnlineActivity.class);
        getReactApplicationContext().startActivity(intent);

    }


}
