import {dialogReducer} from "./dialogReducer";
import {mediasReducer} from "./mediasReducer";
import {persistCombineReducers} from "redux-persist";
import storage from 'redux-persist/es/storage';

const persistConfig = {
    key: 'root',
    storage: storage,
    blacklist: ['dialog']
};


const rootReducer = persistCombineReducers(persistConfig, {
    dialog: dialogReducer,
    medias: mediasReducer
});

export default rootReducer;

