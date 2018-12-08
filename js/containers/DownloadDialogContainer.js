import {dialogValueChange, hideDialog} from "../actions/index";
import {DownloadDialog} from "../components/DownloadDialog";
import {connect} from "react-redux";
import {VdoCipherModule} from "../bridge/modules";
import {ToastAndroid} from "react-native"

const onOptionSelect = async (downloadOptions, selectedTrackOption, dispatch) => {
    const downloadParams = JSON.stringify({downloadOptions, selectedTrackOption});
    VdoCipherModule.download(downloadParams).then().catch((error) => {ToastAndroid.show(JSON.stringify(error), ToastAndroid.SHORT)});
    console.log(dispatch)
};

const getListOfTracks = (downloadTracks) => ((downloadTracks && downloadTracks.length>0) ? downloadTracks : []);

function getSelectedItem(dialog) {
    const tracks = getListOfTracks(dialog.downloadTracks);
    let track = null;
    if (dialog.selectedIndex >= 0) {
        track = tracks[dialog.selectedIndex];
    } else {
        track = tracks.length > 0 ? tracks[tracks.length - 1] : null;
    }

    return track ? {label: track.trackName, value: track} : {label: "", value: ""};
}

const mapStateToProps = (state, ownProps) => ({
    downloadOptions: state.dialog.downloadOptions,
    onSelect: onOptionSelect,
    trackOptions: getListOfTracks(state.dialog.downloadTracks),
    isVisible: state.dialog.visible,
    selectedItemIndex: getSelectedItem(state.dialog),
    dispatch: ownProps
});

const mapDispatchToProps = (dispatch, ownProps) => ({
   onCancel: () => dispatch(hideDialog()),
    onValueChange: (selectedIndex) => dispatch(dialogValueChange({selectedIndex}))
});


export default connect(mapStateToProps, mapDispatchToProps)(DownloadDialog)