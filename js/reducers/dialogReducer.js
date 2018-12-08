import {DIALOG_VALUE_CHANGE, HIDE_DIALOG, SHOW_DIALOG} from "../constants/actionTypes";
import initialState from "../constants/initialState";

export const dialogReducer = (state = initialState.dialog, action) => {
    switch (action.type) {
        case SHOW_DIALOG:
            return {...state, visible: true, ...action.payload };
        case HIDE_DIALOG:
            return {...state, visible: false};
        case DIALOG_VALUE_CHANGE:
            return {...state, ...action.payload};
        default:
            return state;
    }
};