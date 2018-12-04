import {styles} from "../styles";
import {Button, NativeModules, View} from "react-native";
import {STATUS_DOWNLOADED} from "../constants/download_status";
import React from "react";

const PlayButton = ({mediaInfo}) => (
    <View style={styles.welcome}>
        <Button title="Play Online" onPress={() => play(mediaInfo)}/>
    </View>
);

function play(mediaInfo) {
    if (mediaInfo.downloadStatus===STATUS_DOWNLOADED)
        NativeModules.VdoCipherOfflineModule.play(mediaInfo);
    else
        NativeModules.VdoCipherOnlineModule.play(mediaInfo);
}

PlayButton.propTypes = {
    mediaInfo: PropTypes.shape({
        mediaId: PropTypes.string.isRequired,
        otp: PropTypes.string,
        playbackInfo: PropTypes.string,
        name: PropTypes.string,
        downloadStatus: PropTypes.number.isRequired
    })
}

export default PlayButton;