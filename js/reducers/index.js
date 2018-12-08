import {mediasReducer} from "./mediasReducer";
import {persistCombineReducers} from "redux-persist";
import storage from 'redux-persist/es/storage';

const persistConfig = {
    key: 'root',
    storage: storage
};


const rootReducer = persistCombineReducers(persistConfig, {
    medias: mediasReducer
});

export default rootReducer;

