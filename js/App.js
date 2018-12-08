import React, {Component} from 'react';
import {View, Text, ScrollView} from 'react-native';
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
import DownloadDialogContainer from "./containers/DownloadDialogContainer";
import eventListeners from "./event_listeners/EventListener";



type Props = {};
export default class App extends Component<Props> {

    constructor(props) {
        super(props);
        this.store = createStore(rootReducer, initialState);
        this.persisted = persistStore(this.store);
        eventListeners(this.store.dispatch, this.store.getState);
    }

    render() {
    return (
        <Provider store={this.store}>
            <PersistGate loading={<Text>Loading...</Text>} persistor={this.persisted}>
                <View style={styles.container}>
                    <PlayButtonContainer mediaId={sampleVdoInfo.mediaId}/>
                    <DownloadButtonContainer mediaId={sampleVdoInfo.mediaId}/>
                    <DownloadDialogContainer/>
                </View>
            </PersistGate>
        </Provider>
    );
  }
}
