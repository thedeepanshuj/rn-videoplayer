package com.vdocipherdemo.play_online;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.vdocipherdemo.play_online.PlayOnlineModule;

import java.util.ArrayList;
import java.util.List;

public class PlayOnlinePackage implements ReactPackage {

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> nativeModules = new ArrayList<>();

        nativeModules.add(new PlayOnlineModule(reactContext));

        return nativeModules;
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        List<ViewManager> viewManagers = new ArrayList<>();

        return viewManagers;
    }
}
