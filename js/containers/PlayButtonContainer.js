import PlayButton from "../components/PlayButton";
import connect from "react-redux/es/connect/connect";

const mapStateToProps = (state, ownProps) => ({
    mediaInfo: {mediaId: ownProps.mediaId, ...state.medias[ownProps.mediaId]}
});

export default connect(mapStateToProps, null)(PlayButton);