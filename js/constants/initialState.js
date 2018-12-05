import {STATUS_NOT_DOWNLOADED} from "./download_status";

const initialState = {
    medias: {
        "3f29b5434a5c615cda18b16a6232fd75": {
            name : "Home page video",
            otp: "20150519versASE31ba8fc50a0ac49b8e74b9c40f49e099755cd36dc8adccaa3",
            playbackInfo: "eyJ2aWRlb0lkIjoiM2YyOWI1NDM0YTVjNjE1Y2RhMThiMTZhNjIzMmZkNzUifQ==",
            downloadStatus: STATUS_NOT_DOWNLOADED
        }

    },
    showDialog: false
};

export default initialState;