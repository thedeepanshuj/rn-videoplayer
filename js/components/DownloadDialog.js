import React from "react";
import {SinglePickerMaterialDialog} from "react-native-material-dialog";

export const DownloadDialog = ({downloadOptions, trackOptions, onSelect, onCancel, isVisible, selectedItemIndex, dispatch}) => (
    <SinglePickerMaterialDialog
        title={"Download Options"}
        items={trackOptions.map(track => ({label: track.trackName, value: track}))}
        selectedItem={selectedItemIndex}
        visible={isVisible}
        onCancel={()=>onCancel()}
        onOk={(result) => onSelect(downloadOptions, result.selectedItem.value, dispatch)}
        cancelLabel={"Cancel"}
        okLabel={"Download"}/>);


