import {
    DELETE_SUCCESS,
    DOWNLOAD_COMPLETED, DOWNLOAD_ERROR,
    DOWNLOAD_PROGRESS,
    DOWNLOAD_QUEUED,
    HIDE_DIALOG,
    SHOW_DIALOG
} from "../constants/actionTypes";

export const showDialog = () => {
    return {type: SHOW_DIALOG}
};

export const hideDialog = () => {
    return {type: HIDE_DIALOG}
};

export const downloadQueued = (mediaId) => {
    return {
        type: DOWNLOAD_QUEUED,
        payload: { mediaId }
    }
};

export const downloadProgress = (mediaId) => {
    return {
        type: DOWNLOAD_PROGRESS,
        payload: { mediaId }
    }
};

export const downloadCompleted = (mediaId) => {
    return {
        type: DOWNLOAD_COMPLETED,
        payload: { mediaId }
    }
};

export const downloadFailed = (mediaId) => {
    return {
        type: DOWNLOAD_ERROR,
        payload: {mediaId}
    }
};

export const deleteSuccess = (mediaId) => {
    return {
        type: DELETE_SUCCESS,
        payload: {mediaId}
    }
};

