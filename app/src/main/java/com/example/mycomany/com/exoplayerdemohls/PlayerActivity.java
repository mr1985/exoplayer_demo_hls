package com.example.mycomany.com.exoplayerdemohls;

public class PlayerActivity extends android.support.v7.app.AppCompatActivity {

    //Player
    private com.google.android.exoplayer2.ui.SimpleExoPlayerView simpleExoPlayerView;
    private com.google.android.exoplayer2.SimpleExoPlayer player;

    //Logs
    final private String TAG = "PlayerActivity";

    //HLS
    final private String VIDEO_URL = "https://nhkworld.webcdn.stream.ne.jp/www11/nhkworld-tv/domestic/263942/live_wa_s.m3u8";

    //IMA
    private com.google.android.exoplayer2.ext.ima.ImaAdsLoader imaAdsLoader;
    final private String AD_TAG_URI = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/ad_rule_samples&ciu_szs=300x250&ad_rule=1&impl=s&gdfp_req=1&env=vp&output=vmap&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ar%3Dpreonly&cmsid=496&vid=short_onecue&correlator=";


    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        android.support.design.widget.FloatingActionButton fab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                android.support.design.widget.Snackbar.make(view, "Replace with your own action", android.support.design.widget.Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //ExoPlayer implementation
        //Create a default TrackSelector
        com.google.android.exoplayer2.upstream.BandwidthMeter bandwidthMeter = new com.google.android.exoplayer2.upstream.DefaultBandwidthMeter();
        com.google.android.exoplayer2.trackselection.TrackSelection.Factory videoTrackSelectionFactory = new com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection.Factory(bandwidthMeter);
        com.google.android.exoplayer2.trackselection.TrackSelector trackSelector = new com.google.android.exoplayer2.trackselection.DefaultTrackSelector(videoTrackSelectionFactory);

        // Create a default LoadControl
        com.google.android.exoplayer2.LoadControl loadControl = new com.google.android.exoplayer2.DefaultLoadControl();
        //Bis. Create a RenderFactory
        com.google.android.exoplayer2.RenderersFactory renderersFactory = new com.google.android.exoplayer2.DefaultRenderersFactory(this);

        //Create the player
        player = com.google.android.exoplayer2.ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
        simpleExoPlayerView = new com.google.android.exoplayer2.ui.SimpleExoPlayerView(this);
        simpleExoPlayerView = (com.google.android.exoplayer2.ui.SimpleExoPlayerView) findViewById(R.id.player_view);


        //Set media controller
        simpleExoPlayerView.setUseController(true);
        simpleExoPlayerView.requestFocus();

        // Bind the player to the view.
        simpleExoPlayerView.setPlayer(player);

        // Set the media source
        android.net.Uri mp4VideoUri = android.net.Uri.parse(VIDEO_URL);

        //Measures bandwidth during playback. Can be null if not required.
        com.google.android.exoplayer2.upstream.DefaultBandwidthMeter bandwidthMeterA = new com.google.android.exoplayer2.upstream.DefaultBandwidthMeter();

        //Produces DataSource instances through which media data is loaded.
        com.google.android.exoplayer2.upstream.DefaultDataSourceFactory dataSourceFactory = new com.google.android.exoplayer2.upstream.DefaultDataSourceFactory(this, com.google.android.exoplayer2.util.Util.getUserAgent(this, "PiwikVideoApp"), bandwidthMeterA);

        //Produces Extractor instances for parsing the media data.
        com.google.android.exoplayer2.extractor.ExtractorsFactory extractorsFactory = new com.google.android.exoplayer2.extractor.DefaultExtractorsFactory();

        //FOR LIVE STREAM LINK:
        com.google.android.exoplayer2.source.MediaSource videoSource = new com.google.android.exoplayer2.source.hls.HlsMediaSource(mp4VideoUri, dataSourceFactory, 1, null, null);
        final com.google.android.exoplayer2.source.MediaSource mediaSource = videoSource;


        // player.prepare(videoSource); // Remove this line


           imaAdsLoader = new com.google.android.exoplayer2.ext.ima.ImaAdsLoader(this, android.net.Uri.parse(AD_TAG_URI));

        com.google.android.exoplayer2.source.ads.AdsMediaSource.AdsListener adsListener = new com.google.android.exoplayer2.source.ads.AdsMediaSource.AdsListener() {
            @Override
            public void onAdLoadError(java.io.IOException error) {
                error.printStackTrace();
            }

            @Override
            public void onAdClicked() {

            }

            @Override
            public void onAdTapped() {

            }
        };
        com.google.android.exoplayer2.source.ads.AdsMediaSource adsMediaSource = new com.google.android.exoplayer2.source.ads.AdsMediaSource(
                mediaSource,
                dataSourceFactory,
                imaAdsLoader,
                simpleExoPlayerView.getOverlayFrameLayout(),
                null,
                adsListener
        );

        player.prepare(adsMediaSource);

        //ExoPLayer events listener
        player.addListener(new com.google.android.exoplayer2.Player.EventListener() {

            @Override
            public void onTimelineChanged(com.google.android.exoplayer2.Timeline timeline, Object manifest) {
                android.util.Log.v(TAG, "Listener-onTimelineChanged...");
            }

            @Override
            public void onTracksChanged(com.google.android.exoplayer2.source.TrackGroupArray trackGroups, com.google.android.exoplayer2.trackselection.TrackSelectionArray trackSelections) {
                android.util.Log.v(TAG, "Listener-onTracksChanged...");
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                android.util.Log.v(TAG, "Listener-onLoadingChanged...isLoading:" + isLoading);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                android.util.Log.v(TAG, "Listener-onPlayerStateChanged..." + playbackState);

                switch (playbackState) {


                    case com.google.android.exoplayer2.Player.STATE_IDLE:
                        android.util.Log.v(TAG, "STATE IDLE");
                        break;

                    case com.google.android.exoplayer2.Player.STATE_BUFFERING:
                        android.util.Log.v(TAG, "STATE BUFFERING");
                        break;

                    case com.google.android.exoplayer2.Player.STATE_READY:
                        android.util.Log.v(TAG, "STATE READY");
                        break;

                    case com.google.android.exoplayer2.Player.STATE_ENDED:
                        android.util.Log.v(TAG, "STATE ENDED");
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                android.util.Log.v(TAG, "Listener-onRepeatModeChanged...");
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(com.google.android.exoplayer2.ExoPlaybackException error) {
                android.util.Log.v(TAG, "Listener-onPlayerError...");
                player.stop();
                player.prepare(adsMediaSource);
                player.setPlayWhenReady(true);

            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                android.util.Log.v(TAG, "Listener-onPositionDiscontinuity...");

            }

            @Override
            public void onPlaybackParametersChanged(com.google.android.exoplayer2.PlaybackParameters playbackParameters) {
                android.util.Log.v(TAG, "Listener-onPlaybackParametersChanged...");
            }

            @Override
            public void onSeekProcessed() {

            }
        });

         //IMA event listeners
        com.google.ads.interactivemedia.v3.api.AdsLoader adsLoader = imaAdsLoader.getAdsLoader();
        adsLoader.addAdsLoadedListener(new com.google.ads.interactivemedia.v3.api.AdsLoader.AdsLoadedListener() {
            @Override
            public void onAdsManagerLoaded(com.google.ads.interactivemedia.v3.api.AdsManagerLoadedEvent adsManagerLoadedEvent) {

                com.google.ads.interactivemedia.v3.api.AdsManager imaAdsManager = adsManagerLoadedEvent.getAdsManager();
                imaAdsManager.addAdEventListener(new com.google.ads.interactivemedia.v3.api.AdEvent.AdEventListener() {
                    @Override
                    public void onAdEvent(com.google.ads.interactivemedia.v3.api.AdEvent adEvent) {
                        android.util.Log.v("AdEvent: ", adEvent.getType().toString());
                        switch (adEvent.getType()) {
                            case LOADED:
                                break;

                            case PAUSED:
                                break;

                            case STARTED:
                                break;

                            case COMPLETED:
                                break;

                            case ALL_ADS_COMPLETED:
                                break;

                            default:
                                break;


                        /*    Full list of events. Implement what you need.

                        LOADED, TAPPED, PAUSED, LOG, CLICKED, RESUMED, SKIPPED, STARTED, MIDPOINT,
                        COMPLETED, AD_PROGRESS, ICON_TAPPED, AD_BREAK_ENDED, AD_BREAK_READY,
                        FIRST_QUARTILE, THIRD_QUARTILE, AD_BREAK_STARTED, ALL_ADS_COMPLETED,
                        CUEPOINTS_CHANGED, CONTENT_PAUSE_REQUESTED,CONTENT_RESUME_REQUESTED

                        */
                        }

                    }
                });

            }
        });


    }

    //Android Life cycle
    @Override
    protected void onStop() {
        player.release();
        super.onStop();
        android.util.Log.v(TAG, "onStop()...");
    }

    @Override
    protected void onStart() {
        super.onStart();
        android.util.Log.v(TAG, "onStart()...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        android.util.Log.v(TAG, "onResume()...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        android.util.Log.v(TAG, "onPause()...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.util.Log.v(TAG, "onDestroy()...");
        player.release();
    }


}