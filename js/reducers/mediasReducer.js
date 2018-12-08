import {DELETE_SUCCESS, DOWNLOAD_COMPLETED, DOWNLOAD_PROGRESS, DOWNLOAD_QUEUED} from "../constants/actionTypes";
import {STATUS_DOWNLOADED, STATUS_DOWNLOADING, STATUS_NOT_DOWNLOADED} from "../constants/downloadStatus";
import {REHYDRATE} from "redux-persist";

export const mediasReducer = (state = {}, action) => {
    console.log("action", action.type, action.payload);
    let mediaId = null;
    switch (action.type) {
        case DOWNLOAD_QUEUED:
            mediaId = action.payload.mediaId;

            return {...state, [mediaId]: {...state[mediaId], downloadStatus: STATUS_DOWNLOADING}};
        case DOWNLOAD_COMPLETED:
            mediaId = action.payload.mediaId;
            return {...state, [mediaId]: {...state[mediaId], downloadStatus: STATUS_DOWNLOADED}};
        case DOWNLOAD_PROGRESS:
            mediaId = action.payload.mediaId;
            return {...state, [mediaId]: {...state[mediaId], downloadStatus: STATUS_DOWNLOADING}};
        case DELETE_SUCCESS:
            mediaId = action.payload.mediaId;
            return {...state, [mediaId]: {...state[mediaId], downloadStatus: STATUS_NOT_DOWNLOADED}};
        default:
            return {...state};

    }
};