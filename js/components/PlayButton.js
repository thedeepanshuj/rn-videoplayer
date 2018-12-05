import {styles} from "../styles";
import {Button, NativeModules, View} from "react-native";
import {STATUS_DOWNLOADED} from "../constants/downloadStatus";
import React from "react";

const PlayButton = ({mediaInfo}) => (
    <View style={styles.welcome}>
        <Button title="Play Online" onPress={() => play(mediaInfo)}/>
    </View>
);

function play(mediaInfo) {
    if (mediaInfo.downloadStatus===STATUS_DOWNLOADED)
        NativeModules.VdoCipherOfflineModule.play(JSON.stringify(mediaInfo));
    else
        NativeModules.VdoCipherOnlineModule.play(JSON.stringify(mediaInfo));
}

export default PlayButton;