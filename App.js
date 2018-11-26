/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {Platform, StyleSheet, Text, Button, View, TouchableOpacity} from 'react-native';

const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' + 'Cmd+D or shake for dev menu',
  android:
    'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});

type Props = {};
export default class App extends Component<Props> {
  render() {
    return (
      <View style={styles.container}>
        <View style={styles.welcome}><Button title="Play Online" onPress={ () => buttonClicked("online-button")}/></View>
        <View style={styles.welcome}><Button title="Save Offline" onPress={ () => buttonClicked("download-button")}/></View>
        <View style={styles.welcome}><Button title="Play Offline" onPress={ () => buttonClicked("offline-button")}/></View>
      </View>
    );
  }
}

function buttonClicked(message) {
  console.log(message)
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
      alignItems: 'stretch'
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
