import {mediasReducer} from "./mediasReducer";
import {persistCombineReducers, persistReducer} from "redux-persist";
import storage from 'redux-persist/lib/storage'

const persistConfig = {
    key: 'primary',
    storage,
};


const mediasPersistConfig = {
    key: 'medias',
    storage: storage
}

const rootReducer = persistCombineReducers(persistConfig, {
    medias: persistReducer(mediasPersistConfig, mediasReducer)
});

export default rootReducer;

