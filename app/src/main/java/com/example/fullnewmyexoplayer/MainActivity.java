package com.example.fullnewmyexoplayer;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.offline.ProgressiveDownloadAction;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
implements speedPlaybackDialog.NoticeDialogListener{

    private static final String TAG ="MainActivity" ;
    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";

    private PlayerView playerView;
    private MediaSource mVideoSource;
    private boolean mExoPlayerFullscreen = false;
    private FrameLayout mFullScreenButton;
    private ImageView mFullScreenIcon;
    private Dialog mFullScreenDialog;

    private int mResumeWindow;
    private long mResumePosition;
    private SimpleExoPlayer player;

    private RecyclerView recyclerView;
    private List<model> models=new ArrayList<>();
    private modeAdapter adapter;
    private CacheDataSourceFactory cacheDataSourceFactory;
    private ConcatenatingMediaSource concatenatingMediaSource;

    private TextView txtSpeed;
    private final String STATE_SPEED_VALUE = "stateSpeedValue";
    private final String STATE_SPEED_LABEL = "stateSpeedLabel";
    private float currentSpeedValue =1.0f;
    private String currentSpeedLabel="1.0X";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // playerView =findViewById(R.id.exoplayer);

        setupRecyclerView();

        Log.e(TAG,"value: "+value);



        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
            currentSpeedValue =savedInstanceState.getFloat(STATE_SPEED_VALUE);
            currentSpeedLabel=savedInstanceState.getString(STATE_SPEED_LABEL);

            Log.e(TAG,"savedInstanceState: mresumeWindow: "+mResumeWindow);
            Log.e(TAG,"savedInstanceState: mResumePosition: "+mResumePosition);
        }

    }

    private void setupRecyclerView(){
        recyclerView=findViewById(R.id.recyclerView);

        models.add(new model("https://hw7.cdn.asset.aparat.com/aparat-video/626c9e8c56dd1827ca4b047b900e942413877501-1080p__23543.mp4","1","video one"));
        models.add(new model("https://hw7.cdn.asset.aparat.com/aparat-video/0669e0a249abeede3774df5d246c9cab14037526-144p__78903.mp4","1","video one"));
        models.add(new model("https://hw6.cdn.asset.aparat.com/aparat-video/9c4d00599bb0ddda70c0cca10de5958414115912-144p__90147.mp4","1","video one"));
        models.add(new model("https://hw19.cdn.asset.aparat.com/aparat-video/2b00a083edfaea5341dd6ceab448d19d14116999-144p__86282.mp4","1","video one"));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter=new modeAdapter(this,models);
        recyclerView.setAdapter(adapter);

        setListener();

    }

    private void setListener(){
        adapter.setOnItemClickListener(new modeAdapter.OnItemClickListener() {
            @Override
            public void onDownloadClick(model item) {
                ProgressiveDownloadAction action=ProgressiveDownloadAction.createDownloadAction(
                        Uri.parse(item.getUri()), null, null);
//                ProgressiveDownloadAction action = new ProgressiveDownloadAction(
//                        Uri.parse(item.getUri()), false, null, null);
                MediaDownloadService.startWithAction(
                        MainActivity.this,
                        MediaDownloadService.class,
                        action,
                        false);
            }

            @Override
            public void onModelClick(model item) {
                playSingleVideo(item);

            }
        });
    }

    private void playSingleVideo(model item){
        Log.e(TAG,"current index: "+player.getCurrentWindowIndex());
        Log.e(TAG,"move index: "+item.getIndex_dataSource());

        player.seekTo(item.getIndex_dataSource(), 0);
        player.setPlayWhenReady(true);
    }

    private void initPlayer(){
        String userAgent = Util.getUserAgent(MainActivity.this, getApplicationContext().getApplicationInfo().packageName);
        DefaultDataSourceFactory dataSourceFactory=new DefaultDataSourceFactory(this,userAgent);
        cacheDataSourceFactory = new CacheDataSourceFactory(
                DownloadUtil.getCache(this),
                dataSourceFactory,
                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
        concatenatingMediaSource=new ConcatenatingMediaSource();
        for (int i=0;i<models.size();i++){
            // MediaSource mediaSource=new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(models.get(i).getUri()));
            MediaSource mediaSource=new ExtractorMediaSource.Factory(cacheDataSourceFactory).
                    createMediaSource(Uri.parse(models.get(i).getUri()));
            concatenatingMediaSource.addMediaSource(mediaSource);
            models.get(i).setIndex_dataSource(i);
        }
        playerView.setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS);

    }



    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);
        outState.putFloat(STATE_SPEED_VALUE, currentSpeedValue);
        outState.putString(STATE_SPEED_LABEL,currentSpeedLabel);

        super.onSaveInstanceState(outState);
    }



    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }


    private void openFullscreenDialog() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        ((ViewGroup) playerView.getParent()).removeView(playerView);
        mFullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_full_screen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }


    private void closeFullscreenDialog() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ((ViewGroup) playerView.getParent()).removeView(playerView);
        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(playerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_expand));
    }


    private void initFullscreenButton() {

        PlayerControlView controlView = playerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        //mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen)
                    openFullscreenDialog();
                else
                    closeFullscreenDialog();
            }
        });
    }


    private void initExoPlayer() {

        player = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
        playerView.setPlayer(player);


        boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;

        player.prepare(concatenatingMediaSource);

        if (haveResumePosition) {
            //playerView.getPlayer().seekTo(mResumeWindow, mResumePosition);
            player.seekTo(mResumeWindow,mResumePosition);
            Log.e(TAG,"in if mResumePosition : "+mResumePosition);
        }

        player.setPlaybackParameters(new PlaybackParameters(currentSpeedValue));

        player.setPlayWhenReady(true);
    }


    @Override
    protected void onResume() {

        super.onResume();
        Log.e(TAG,"onResume currentspeedlabel: "+currentSpeedLabel);
        Log.e(TAG,"onResume currentspeedvalue: "+currentSpeedValue);
       // txtSpeed.setText(currentSpeedLabel);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (playerView == null) {

           playerView =findViewById(R.id.exoplayer);




            initFullscreenDialog();
            initFullscreenButton();
            initPlayer();

           initSpeedButton();

        }
        //initSpeedButton();


        if (mExoPlayerFullscreen) {
            ((ViewGroup) playerView.getParent()).removeView(playerView);
            mFullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_full_screen_skrink));
            mFullScreenDialog.show();
        }
        initExoPlayer();
    }

    @Override
    protected void onPause() {

        super.onPause();

        if (playerView != null && playerView.getPlayer() != null) {
            mResumeWindow=player.getCurrentWindowIndex();
            mResumePosition=Math.max(0,player.getCurrentPosition());
            Log.e(TAG,"onPause: mresumeWindow: "+mResumeWindow);
            Log.e(TAG,"onPause: mResumePosition: "+mResumePosition);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (playerView != null && playerView.getPlayer() != null) {
            Log.e(TAG,"onStop: mresumeWindow: "+mResumeWindow);
            Log.e(TAG,"onStop: mResumePosition: "+mResumePosition);

        }
        playerView.getPlayer().release();

        if (mFullScreenDialog != null)
            mFullScreenDialog.dismiss();
    }

    private void initSpeedButton(){
        PlayerControlView controlView = playerView.findViewById(R.id.exo_controller);
        txtSpeed = controlView.findViewById(R.id.exo_speed);
        txtSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showNoticeDialog();
            }
        });

        //player.setPlaybackParameters(new PlaybackParameters(currentSpeedValue));
        txtSpeed.setText(currentSpeedLabel);
    }

    public void showNoticeDialog() {

        DialogFragment dialog = new speedPlaybackDialog();
        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
    }


    @Override
    public void onSpeedChange(String speed) {

        Log.e(TAG,"which is: "+speed);

        final String s1="0.5X";
        final String s2="0.75X";
        final String s3="1.0X";
        final String s4="1.25X";
        final String s5="1.5X";
        final String s6="1.75X";
        final String s7="2.0X";

        currentSpeedLabel=speed;

        switch (speed){
            case s1:
                player.setPlaybackParameters(new PlaybackParameters(0.5f));
                txtSpeed.setText(s1);
                currentSpeedValue =0.5f;

                break;
            case s2:
                player.setPlaybackParameters(new PlaybackParameters(0.75f));
                txtSpeed.setText(s2);
                currentSpeedValue =0.75f;
                break;
            case s3:
                player.setPlaybackParameters(new PlaybackParameters(1.0f));
                txtSpeed.setText(s3);
                currentSpeedValue =1.0f;
                break;
            case s4:
                player.setPlaybackParameters(new PlaybackParameters(1.25f));
                txtSpeed.setText(s4);
                currentSpeedValue =1.25f;
                break;
            case s5:
                player.setPlaybackParameters(new PlaybackParameters(1.75f));
                txtSpeed.setText(s5);
                currentSpeedValue =1.5f;
                break;
            case s6:
                player.setPlaybackParameters(new PlaybackParameters(1.75f));
                txtSpeed.setText(s6);
                currentSpeedValue =1.75f;
                break;
            case s7:
                player.setPlaybackParameters(new PlaybackParameters(2.0f));
                txtSpeed.setText(s7);
                currentSpeedValue =2.0f;
                break;
        }
    }
}
