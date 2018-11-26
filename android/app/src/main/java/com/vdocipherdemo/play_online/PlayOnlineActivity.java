package com.vdocipherdemo.play_online;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vdocipher.aegis.media.ErrorDescription;
import com.vdocipher.aegis.media.Track;
import com.vdocipher.aegis.player.VdoPlayer;
import com.vdocipher.aegis.player.VdoPlayerFragment;
import com.vdocipherdemo.Constants;
import com.vdocipherdemo.R;

public class PlayOnlineActivity extends AppCompatActivity implements VdoPlayer.InitializationListener {

    private VdoPlayerFragment playerFragment;
    private VdoPlayer player;
    private String mOtp;
    private String mPlaybackInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_online);

        mOtp = Constants.OTP;
        mPlaybackInfo = Constants.PLAYBACK_INFO;

        playerFragment = (VdoPlayerFragment) getFragmentManager().findFragmentById(R.id.online_player_fragment);
        playerFragment.initialize(PlayOnlineActivity.this);
    }

    @Override
    public void onInitializationSuccess(VdoPlayer.PlayerHost playerHost, VdoPlayer vdoPlayer, boolean wasRestored) {
        this.player = vdoPlayer;
        this.player.addPlaybackEventListener(playbackEventListener);

        VdoPlayer.VdoInitParams vdoParams = VdoPlayer.VdoInitParams.createParamsWithOtp(mOtp, mPlaybackInfo);
        player.load(vdoParams);

    }

    @Override
    public void onInitializationFailure(VdoPlayer.PlayerHost playerHost, ErrorDescription errorDescription) {

    }

    private VdoPlayer.PlaybackEventListener playbackEventListener = new VdoPlayer.PlaybackEventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            boolean playing = player != null && playbackState == VdoPlayer.STATE_READY && player.getPlayWhenReady();
        }

        @Override
        public void onSeekTo(long l) {

        }

        @Override
        public void onProgress(long l) {

        }

        @Override
        public void onBufferUpdate(long l) {

        }

        @Override
        public void onPlaybackSpeedChanged(float v) {

        }

        @Override
        public void onLoading(VdoPlayer.VdoInitParams vdoInitParams) {

        }

        @Override
        public void onLoaded(VdoPlayer.VdoInitParams vdoInitParams) {
            player.setPlayWhenReady(true);
        }

        @Override
        public void onLoadError(VdoPlayer.VdoInitParams vdoInitParams, ErrorDescription errorDescription) {

        }

        @Override
        public void onMediaEnded(VdoPlayer.VdoInitParams vdoInitParams) {

        }

        @Override
        public void onError(VdoPlayer.VdoInitParams vdoInitParams, ErrorDescription errorDescription) {

        }

        @Override
        public void onTracksChanged(Track[] tracks, Track[] tracks1) {

        }
    };
}
