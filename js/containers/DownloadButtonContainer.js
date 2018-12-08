import {STATUS_DOWNLOADED, STATUS_DOWNLOADING, STATUS_NOT_DOWNLOADED} from "../constants/downloadStatus";
import {ToastAndroid} from "react-native";
import {DownloadButton} from "../components/DownloadButton";
import connect from "react-redux/es/connect/connect";
import {VdoCipherOfflineModule} from "../bridge/modules";

function getButtonText(mediaInfo) {
    switch (mediaInfo.downloadStatus) {
        case STATUS_NOT_DOWNLOADED: return 'DOWNLOAD';
        case STATUS_DOWNLOADING:    return 'DOWNLOADING';
        case STATUS_DOWNLOADED: return 'DELETE';
    }
}

const getButtonDisableStatus = (mediaInfo, state) => (mediaInfo.downloadStatus === STATUS_DOWNLOADING)

const downloadMedia = async (mediaInfo, dispatch) => {
    console.log("downloadMediaEntry");
    VdoCipherOfflineModule.download(JSON.stringify(mediaInfo));
    // VdoCipherModule.getDownloadOptions(JSON.stringify(mediaInfo))
    //     .then(({downloadOptions, downloadTracks}) => {
    //         console.log("downloadOptions", downloadOptions)
    //     })
    //     .catch((error) => {
    //         console.log("error : ",error);
    //         ToastAndroid.show(JSON.stringify(error), ToastAndroid.SHORT)
    //     })

};

const deleteMedia = (mediaInfo) => {
    console.log("delete :", mediaInfo)

    // VdoCipherModule.delete();
    // NativeModules.VdoCipherOfflineModule.delete(JSON.stringify(mediaInfo));
};

function getOnClick(mediaInfo) {
    switch (mediaInfo.downloadStatus) {
        case STATUS_NOT_DOWNLOADED: return downloadMedia;
        case STATUS_DOWNLOADING:    return null;
        case STATUS_DOWNLOADED:     return deleteMedia;
    }
}

const mapStateToProps = (state, ownProps) => ({
    buttonText: getButtonText(state.medias[ownProps.mediaId]),
    onClick: getOnClick(state.medias[ownProps.mediaId]),
    isDisabled: getButtonDisableStatus(state.medias[ownProps.mediaId], state),
    mediaInfo: state.medias[ownProps.mediaId]
});

export default connect(mapStateToProps, null)(DownloadButton)