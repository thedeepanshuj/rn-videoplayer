import React, {Component} from 'react';
import {View, Text} from 'react-native';
import {styles} from './styles';
import PlayButtonContainer from "./containers/PlayButtonContainer";
import DownloadButtonContainer from "./containers/DownloadButtonContainer";
import {createStore} from "redux";
import rootReducer from "./reducers";
import {Provider} from "react-redux";
import { PersistGate } from 'redux-persist/integration/react'
import { persistStore } from 'redux-persist'
import initialState from "./constants/initialState";
import {sampleVdoInfo} from "./constants/vdocipher";



type Props = {};
export default class App extends Component<Props> {

    constructor(props) {
        super(props);
        this.store = createStore(rootReducer, initialState);
        this.persisted = persistStore(this.store);
    }

    render() {
    return (
        <Provider store={this.store}>
            <PersistGate loading={<Text>Loading...</Text>} persistor={this.persisted}>
                <View style={styles.container}>
                    <PlayButtonContainer mediaId={sampleVdoInfo.mediaId}/>
                    <DownloadButtonContainer mediaId={sampleVdoInfo.mediaId}/>
                </View>
            </PersistGate>
        </Provider>
    );
  }
}
