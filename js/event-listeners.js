import { DeviceEventEmitter } from 'react-native';
import {EVENT_COMPLETED, EVENT_DELETED, EVENT_FAILED, EVENT_PROGRESS, EVENT_QUEUED} from "./constants/DownloadEvents";
import {deleteSuccess, downloadCompleted, downloadFailed, downloadProgress, downloadQueued} from "./actions";

const eventListeners = (dispatch) => {

    DeviceEventEmitter.addListener(EVENT_QUEUED, (event) => {
        dispatch(downloadQueued(event.mediaId))
    });

    DeviceEventEmitter.addListener(EVENT_PROGRESS, (event) => {
        dispatch(downloadProgress(event.mediaId))
    });

    DeviceEventEmitter.addListener(EVENT_COMPLETED, (event) => {
        dispatch(downloadCompleted(event.mediaId))
    });

    DeviceEventEmitter.addListener(EVENT_DELETED, (event) => {
        dispatch(deleteSuccess(event.mediaId))
    });

    DeviceEventEmitter.addListener(EVENT_FAILED, (event) => {
        dispatch(downloadFailed(event.mediaId))
    });
};

export default eventListeners;