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
        this.props.mediaId = sampleVdoInfo.mediaId;
        const store = createStore(rootReducer, initialState);
        this.persisted = persistStore(store);
    }

    render() {
    return (
        <Provider>
            <PersistGate loading={<Text>Loading...</Text>} persistor={this.persisted}>
                <View style={styles.container}>
                    <PlayButtonContainer mediaId:{this.props.mediaId}/>
                    <DownloadButtonContainer mediaId:{this.props.mediaId}/>
                </View>
            </PersistGate>
        </Provider>
    );
  }
}
