import React, {Component} from 'react';
import {View} from 'react-native';
import {styles} from './styles';
import PlayButtonContainer from "./containers/PlayButtonContainer";
import DownloadButtonContainer from "./containers/DownloadButtonContainer";

type Props = {};
export default class App extends Component<Props> {
  render() {
    return (
      <View style={styles.container}>
          <PlayButtonContainer/>
          <DownloadButtonContainer/>
      </View>
    );
  }
}
