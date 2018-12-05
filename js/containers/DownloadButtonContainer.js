import {STATUS_DOWNLOADED, STATUS_DOWNLOADING, STATUS_NOT_DOWNLOADED} from "../constants/downloadStatus";
import {NativeModules} from "react-native";
import {DownloadButton} from "../components/DownloadButton";
import connect from "react-redux/es/connect/connect";

function getButtonText(mediaInfo) {
    switch (mediaInfo.downloadStatus) {
        case STATUS_NOT_DOWNLOADED:
            return 'DOWNLOAD';
        case STATUS_DOWNLOADING:
            return 'DOWNLOADING';
        case STATUS_DOWNLOADED:
            return 'DELETE';
    }
}

function getButtonDisableStatus(mediaInfo) {
    return mediaInfo.downloadStatus === STATUS_DOWNLOADING;
}

const downloadMedia = (mediaInfo) => {
    NativeModules.VdoCipherOfflineModule.download(JSON.stringify(mediaInfo));
};

const deleteMedia = (mediaInfo) => {
    console.log(mediaInfo);
    NativeModules.VdoCipherOfflineModule.delete(JSON.stringify(mediaInfo));
};

function getOnClick(mediaInfo) {
    switch (mediaInfo.downloadStatus) {
        case STATUS_NOT_DOWNLOADED:
            return downloadMedia;
        case STATUS_DOWNLOADING:
            return null;
        case STATUS_DOWNLOADED:
            return deleteMedia;
    }
}

const mapStateToProps = (state, ownProps) => ({
    buttonText: getButtonText(state.medias[ownProps.mediaId]),
    onClick: getOnClick(state.medias[ownProps.mediaId]),
    isDisabled: getButtonDisableStatus(state.medias[ownProps.mediaId]),
    mediaInfo: state.medias[ownProps.mediaId]
});

export default connect(mapStateToProps, null)(DownloadButton)