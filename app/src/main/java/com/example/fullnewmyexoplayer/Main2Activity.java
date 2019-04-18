package com.example.fullnewmyexoplayer;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.google.android.exoplayer2.offline.ProgressiveDownloadAction;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends BaseExoPlayerActivity {


    private static final String TAG ="Main2Activity" ;
    private RecyclerView recyclerView;
    private List<model> models=new ArrayList<>();
    private modeAdapter adapter;
    //private ProgressBar mProgress;
    //private ThumbNailPlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main2);

        setRecyclerView();

        Log.e(TAG,"vlaue: "+basevalue);
    }

    private void setRecyclerView(){
        recyclerView=findViewById(R.id.rcy);

        models.add(new model("https://hw7.cdn.asset.aparat.com/aparat-video/626c9e8c56dd1827ca4b047b900e942413877501-1080p__23543.mp4","1","video one"));
        models.add(new model("https://hw7.cdn.asset.aparat.com/aparat-video/626c9e8c56dd1827ca4b047b900e942413877501-1080p__23543.mp4","1","video one"));
        models.add(new model("https://hw7.cdn.asset.aparat.com/aparat-video/d03a5812a6a401dfbbfa6e06c8c391fa13897167-240p__74714.mp4","1","video one"));
        models.add(new model("https://hw7.cdn.asset.aparat.com/aparat-video/626c9e8c56dd1827ca4b047b900e942413877501-1080p__23543.mp4","1","video one"));
        models.add(new model("https://hw7.cdn.asset.aparat.com/aparat-video/626c9e8c56dd1827ca4b047b900e942413877501-1080p__23543.mp4","1","video one"));
        models.add(new model("https://hw7.cdn.asset.aparat.com/aparat-video/626c9e8c56dd1827ca4b047b900e942413877501-1080p__23543.mp4","1","video one"));
        models.add(new model("https://hw7.cdn.asset.aparat.com/aparat-video/626c9e8c56dd1827ca4b047b900e942413877501-1080p__23543.mp4","1","video one"));
        models.add(new model("https://hw7.cdn.asset.aparat.com/aparat-video/626c9e8c56dd1827ca4b047b900e942413877501-1080p__23543.mp4","1","video one"));

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
                MediaDownloadService.startWithAction(
                        Main2Activity.this,
                        MediaDownloadService.class,
                        action,
                        false);

            }

            @Override
            public void onModelClick(model item) {
                playSingleVideo(item.getIndex_dataSource());

            }
        });
    }

    @Override
    public ConcatenatingMediaSource buildMediaSource(ArrayList<Uri> uris) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, getString(R.string.app_name)));

        cacheDataSourceFactory = new CacheDataSourceFactory(
                DownloadUtil.getCache(this),
                dataSourceFactory,
                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
        concatenatingMediaSource = new ConcatenatingMediaSource();
        for (int i = 0; i < models.size(); i++) {
            // MediaSource mediaSource=new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(models.get(i).getUri()));
            MediaSource mediaSource = new ExtractorMediaSource.Factory(cacheDataSourceFactory).
                    createMediaSource(Uri.parse(models.get(i).getUri()));
            concatenatingMediaSource.addMediaSource(mediaSource);
            models.get(i).setIndex_dataSource(i);

        }
        return concatenatingMediaSource;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main2;
    }

    @Override
    public void getCurrentWindow(int currentWindow) {
        Log.e(TAG,"current windo "+currentWindow);
    }

//    @Override
//    public void setupWidget() {
//        mProgressBar=findViewById(R.id.progress_bar);
//        super.mPlayerView=findViewById(R.id.player_view);
//        super.txtSpeed=findViewById(R.id.exo_speed);
//        super.imgFullscreen=findViewById(R.id.exo_fullscreen_icon);
//       // mPlayerView.setErrorMessageProvider(new PlayerErrorMessageProvider());
//
//    }

}
