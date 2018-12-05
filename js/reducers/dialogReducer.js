import {HIDE_DIALOG, SHOW_DIALOG} from "../constants/action_types";

export const dialogReducer = (state = false, action) => {

    switch (action.type) {
        case SHOW_DIALOG:
            return true;
        case HIDE_DIALOG:
            return false;
        default:
            return state;
    }
};