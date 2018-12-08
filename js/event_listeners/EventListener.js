import { DeviceEventEmitter } from 'react-native';
import {EVENT_COMPLETED, EVENT_DELETED, EVENT_FAILED, EVENT_PROGRESS, EVENT_QUEUED} from "../constants/DownloadEvents";

const eventListeners = (dispatch, getState) => {

    DeviceEventEmitter.addListener(EVENT_QUEUED, (event) => {
        console.log(event);
    });
    DeviceEventEmitter.addListener(EVENT_PROGRESS, () => {
        console.log(event);
    });
    DeviceEventEmitter.addListener(EVENT_COMPLETED, () => {
        console.log(event);
    });
    DeviceEventEmitter.addListener(EVENT_DELETED, () => {
        console.log(event);
    });
    DeviceEventEmitter.addListener(EVENT_FAILED, () => {
        console.log(event);
    });
};

export default eventListeners;