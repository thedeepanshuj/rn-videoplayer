import React, {Component} from 'react';
import {Platform, StyleSheet, Button, View, NativeModules} from 'react-native';
import {sampleVdoInfo} from './constants/vdocipher';
import {styles} from './styles';


type Props = {};
export default class App extends Component<Props> {
  render() {
    return (
      <View style={styles.container}>
        <View style={styles.welcome}><Button title="Play Online" onPress={ () => playOnlineButtonClicked()}/></View>
        <View style={styles.welcome}><Button title="Save Offline" onPress={ () => saveOfflineButtonClicked()}/></View>
        <View style={styles.welcome}><Button title="Play Offline" onPress={ () => playOfflineButtonClicked()}/></View>
        <View style={styles.welcome}><Button title="Delete" onPress={ () => deleteButtonClicked()}/></View>
      </View>
    );
  }
}

function playOnlineButtonClicked(){
  const vdoInfo = JSON.stringify(sampleVdoInfo);
  NativeModules.VdoCipherOnlineModule.play(vdoInfo);
}

function saveOfflineButtonClicked() {
    const vdoInfo = JSON.stringify(sampleVdoInfo);
    NativeModules.VdoCipherOfflineModule.download(vdoInfo);
}

function playOfflineButtonClicked() {
    const vdoInfo = JSON.stringify(sampleVdoInfo);
    NativeModules.VdoCipherOfflineModule.play(vdoInfo);
}

function deleteButtonClicked() {
    const vdoInfo = JSON.stringify(sampleVdoInfo);
    NativeModules.VdoCipherOfflineModule.delete(vdoInfo);
}

