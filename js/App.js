
import React, {Component} from 'react';
import {View, Text, DeviceEventEmitter} from 'react-native';
import {styles} from './styles';
import PlayButtonContainer from "./containers/PlayButtonContainer";
import DownloadButtonContainer from "./containers/DownloadButtonContainer";
import {applyMiddleware, compose, createStore} from "redux";
import rootReducer from "./reducers";
import {Provider} from "react-redux";
import { PersistGate } from 'redux-persist/integration/react'
import { persistStore } from 'redux-persist'
import initialState from "./constants/initialState";
import {sampleVdoInfo} from "./constants/vdocipher";
import eventListeners from "./event-listeners";
import logger from 'redux-logger'

type Props = {};
export default class App extends Component<Props> {

    constructor(props) {

        super(props);
        this.store = createStore(rootReducer, initialState, compose(applyMiddleware(logger)));
        this.persisted = persistStore(this.store, null, () => {this.store.getState() });
        // eventListeners(this.store.dispatch, this.store.getState);
    }

    componentDidMount(): void {
        eventListeners(this.store.dispatch)
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
