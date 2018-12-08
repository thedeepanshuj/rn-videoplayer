import {styles} from "../styles";
import {Button, View} from "react-native";
import React from "react";

export const DownloadButton = ({buttonText, mediaInfo, onClick, isDisabled, dispatch}) => (

    <View style={styles.welcome}>
        <Button title={buttonText} onPress={() => onClick(mediaInfo, dispatch)} disabled={isDisabled}/>
    </View>
);