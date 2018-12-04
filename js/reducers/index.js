import {

    STATUS_DOWNLOADED,
    STATUS_DOWNLOADING,
    STATUS_NOT_DOWNLOADED
} from "../constants/download_status";
import {
    DELETE_SUCCESS,
    DOWNLOAD_COMPLETED,
    DOWNLOAD_PROGRESS,
    DOWNLOAD_QUEUED,
    HIDE_DIALOG,
    SHOW_DIALOG
} from "../constants/action_types";

export const dialog = (state = false, action) => {

    switch (action.type) {
        case SHOW_DIALOG:
            return true;
        case HIDE_DIALOG:
            return false;
        default:
            return state;
    }
}


export const download = (state = {}, action) => {
    const mediaId = action.payload.mediaId;
    switch (action.type) {
        case DOWNLOAD_QUEUED:
            return {...state, [mediaId]: {...[mediaId], status: STATUS_DOWNLOADING}};
        case DOWNLOAD_COMPLETED:
            return {...state, [mediaId]: {...[mediaId], status: STATUS_DOWNLOADED}};
        case DOWNLOAD_PROGRESS:
            return {...state, [mediaId]: {...[mediaId], status: STATUS_DOWNLOADING}};
        case DELETE_SUCCESS:
            return {...state, [mediaId]: {...[mediaId], status: STATUS_NOT_DOWNLOADED}};
        default:
            return {...state};

    }
};