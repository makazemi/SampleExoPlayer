package com.example.fullnewmyexoplayer;

import android.app.Notification;
import android.support.annotation.Nullable;

import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloadManager.TaskState;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.scheduler.Scheduler;
import com.google.android.exoplayer2.ui.DownloadNotificationUtil;

import static com.example.fullnewmyexoplayer.C.DOWNLOAD_CHANNEL_ID;
import static com.example.fullnewmyexoplayer.C.DOWNLOAD_NOTIFICATION_ID;


public class MediaDownloadService extends DownloadService {
    public MediaDownloadService() {
        super(
                DOWNLOAD_NOTIFICATION_ID,
                DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
                DOWNLOAD_CHANNEL_ID,
                R.string.download_channel_name);
    }

    @Override
    protected DownloadManager getDownloadManager() {
        return DownloadUtil.getDownloadManager(this);
    }

    @Nullable
    @Override
    protected Scheduler getScheduler() {
        return null;
    }

    @Override
    protected Notification getForegroundNotification(TaskState[] taskStates) {
        return DownloadNotificationUtil.buildProgressNotification(
                this,
                R.drawable.exo_icon_play,
                DOWNLOAD_CHANNEL_ID,
                null,
                null,
                taskStates);
    }
}
