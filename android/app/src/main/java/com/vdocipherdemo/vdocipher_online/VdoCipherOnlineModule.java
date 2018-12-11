package com.vdocipherdemo.vdocipher_online;

import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.gson.Gson;
import com.vdocipherdemo.Constants;
import com.vdocipherdemo.shared_components.vdo_player.PlayerActivity;
import com.vdocipherdemo.shared_components.VdoInfo;

public class VdoCipherOnlineModule extends ReactContextBaseJavaModule {

    private final static String MODULE_PLAY_ONLINE = "VdoCipherOnlineModule";

    @Override
    public String getName() {
        return MODULE_PLAY_ONLINE;
    }

    public VdoCipherOnlineModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @ReactMethod
    public void play(String vdoInfoString) {

        VdoInfo vdoInfo = new Gson().fromJson(vdoInfoString, VdoInfo.class);

        Intent intent = new Intent(getReactApplicationContext(), PlayerActivity.class);
        intent.putExtra(Constants.PLAY_TYPE, Constants.PLAY_ONLINE);
        intent.putExtra(Constants.VDO_INFO, vdoInfo);
        getReactApplicationContext().startActivity(intent);

    }


}
