package com.example.android.bakingapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;
import com.example.android.bakingapp.utils.Config;
import com.example.android.bakingapp.utils.Utils;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;

/**
 * Created by aditibhattacharya on 05/02/2018.
 */

public class RecipeStepDetailFragment extends Fragment implements Player.EventListener {

    private static final String TAG = RecipeStepDetailFragment.class.getSimpleName();
    private Unbinder mUnbinder;
    private Context mContext;

    private Recipe mSelectedRecipe;
    private Step mSelectedStep;
    private int mStepId;
    private int mStepCount;
    private String mStepDesc;
    private String mVideoUrl;
    private SimpleExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private BandwidthMeter mBandwidthMeter;
    TrackSelector mTrackSelector;


    // ButterKnife View binding
    @BindView(R.id.textview_step_desc)          TextView mTextViewStepDesc;
    @BindView(R.id.rlayout_player)              RelativeLayout mRlayoutPlayer;
    @BindView(R.id.playerview_recipe_video)     SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.imageview_no_media)          ImageView mImageViewNoMedia;



    /** Empty Constructor */
    public RecipeStepDetailFragment() {
    }


    /**
     * Override this method to get the context method as the fragment is used by multiple activities
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        // Get arguments passed on from host activity
        if (getArguments() != null) {
            ArrayList<Recipe> recipes = getArguments().getParcelableArrayList(Config.INTENT_KEY_SELECTED_RECIPE);

            if (recipes != null) {
                mSelectedRecipe = recipes.get(0);
                mStepId = getArguments().getInt(Config.INTENT_KEY_SELECTED_STEP);
                mStepCount = getArguments().getInt(Config.INTENT_KEY_STEP_COUNT);
            }
        }

        // Get step details
        getStepDetails();

        // Display steps description
        displayStepsData();

        // Create exoplayer to show recipe video
        createMediaPlayer();

        return rootView;
    }


    /**
     * Method to get description and video for the selected step
     */
    public void getStepDetails() {
        mSelectedStep = mSelectedRecipe.getRecipeSteps().get(mStepId);
        mStepDesc = mSelectedStep.getStepDescription();
        mVideoUrl = mSelectedStep.getStepVideoURL();
    }


    /**
     * Method to display Step Details
     */
    private void displayStepsData() {

        // Remove the step number from step description (Sample raw data => 1. Step description)
        if (mStepId > 0) {
            int index = mStepDesc.indexOf(". ");
            mStepDesc = mStepDesc.substring(index + 2);
        }

        mTextViewStepDesc.setText(mStepDesc);
    }


    /**
     * Method to create ExoPlayer instance, and attach media to it
     */
    public void createMediaPlayer() {

        releasePlayer();

        if (!Utils.isEmptyString(mVideoUrl)) {
            initializeMediaSession();
            initializePlayer(Uri.parse(mVideoUrl));

            // hide the overlay default image
            ButterKnife.apply(mImageViewNoMedia, Utils.VISIBILITY, View.GONE);

        } else {
            // show the overlay default image as no video found for the step
            ButterKnife.apply(mImageViewNoMedia, Utils.VISIBILITY, View.VISIBLE);
        }
    }


    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat
        mMediaSession = new MediaSessionCompat(mContext, TAG);

        // Enable callbacks from MediaButtons and TransportControls
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Stop MediaButtons from restarting the player when the app is not visible
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        // MediaSessionCallback has methods that handle callbacks from a media controller
        mMediaSession.setCallback(new MediaSessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }


    /**
     * Initialize the player
     * @param mediaUri - uri of the media to be played
     */
    private void initializePlayer(Uri mediaUri) {

        if (mExoPlayer == null) {

            // Create the player instance
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(mBandwidthMeter);
            mTrackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            // Create the player
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, mTrackSelector);

            // Attach player to the view
            mExoPlayerView.setPlayer(mExoPlayer);


            //------------------------
            // Prepare the player
            //------------------------

            //Measures bandwidth during playback. Can be null if not required.
            mBandwidthMeter = new DefaultBandwidthMeter();

            // Create datasource instance through which media is loaded
            String userAgent = Util.getUserAgent(mContext, TAG);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext, userAgent);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
            mExoPlayer.prepare(mediaSource);

            mExoPlayer.setPlayWhenReady(true);
        }
    }


    /**
     * Media Session Callbacks which enables all external clients to control the player
     */
    private class MediaSessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == Player.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mExoPlayer.getCurrentPosition(), 1f);
        } else if (playbackState == Player.STATE_READY) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mExoPlayer.getCurrentPosition(), 1f);
        }

        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {
    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    }

    @Override
    public void onSeekProcessed() {
    }


    @Override
    public void onDetach() {
        super.onDetach();
        releasePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    /**
     * Release the player, and deactivate media session
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }

        if (mTrackSelector !=  null) {
            mTrackSelector = null;
        }
    }

}
