package com.vdocipherdemo.shared_components.vdo_player;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.vdocipher.aegis.media.ErrorDescription;
import com.vdocipher.aegis.media.Track;
import com.vdocipher.aegis.player.VdoPlayer;
import com.vdocipher.aegis.player.VdoPlayerFragment;
import com.vdocipherdemo.Constants;
import com.vdocipherdemo.R;

public class PlayerActivity extends AppCompatActivity implements VdoPlayer.InitializationListener {

    private VdoPlayerFragment playerFragment;
    private VdoPlayer player;
    private String playType;
    private VdoInfo vdoInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent intent = getIntent();
        playType = intent.getStringExtra(Constants.PLAY_TYPE);
        vdoInfo = (VdoInfo) intent.getSerializableExtra(Constants.VDO_INFO);

        playerFragment = (VdoPlayerFragment) getFragmentManager().findFragmentById(R.id.online_player_fragment);
        playerFragment.initialize(PlayerActivity.this);
    }

    @Override
    public void onInitializationSuccess(VdoPlayer.PlayerHost playerHost, VdoPlayer vdoPlayer, boolean wasRestored) {
        this.player = vdoPlayer;
        this.player.addPlaybackEventListener(playbackEventListener);

        VdoPlayer.VdoInitParams vdoParams;
        if (this.playType.equals(Constants.PLAY_ONLINE)) {
            vdoParams = VdoPlayer.VdoInitParams.createParamsWithOtp(vdoInfo.getOtp(), vdoInfo.getPlaybackInfo());
        } else if (this.playType.equals(Constants.PLAY_OFFLINE)) {
            vdoParams = VdoPlayer.VdoInitParams.createParamsForOffline(vdoInfo.getMediaId());
        } else {
            Toast.makeText(getApplicationContext(), "No PlayType selected", Toast.LENGTH_SHORT).show();
            return;
        }

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
