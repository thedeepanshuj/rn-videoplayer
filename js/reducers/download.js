import {DELETE_SUCCESS, DOWNLOAD_COMPLETED, DOWNLOAD_PROGRESS, DOWNLOAD_QUEUED} from "../constants/action_types";
import {STATUS_DOWNLOADED, STATUS_DOWNLOADING, STATUS_NOT_DOWNLOADED} from "../constants/download_status";

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