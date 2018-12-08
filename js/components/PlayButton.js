import {styles} from "../styles";
import {Button, NativeModules, View} from "react-native";
import {STATUS_DOWNLOADED} from "../constants/downloadStatus";
import React from "react";
import {VdoCipherOfflineModule} from "../bridge/modules";

const PlayButton = ({mediaInfo}) => (
    <View style={styles.welcome}>
        <Button title="Play Online" onPress={() => play(mediaInfo)}/>
    </View>
);

function play(mediaInfo) {
    mediaInfo.downloadStatus===STATUS_DOWNLOADED ? VdoCipherOfflineModule.play(JSON.stringify(mediaInfo)) : VdoCipherOnlineModule.play(JSON.stringify(mediaInfo));
}

export default PlayButton;