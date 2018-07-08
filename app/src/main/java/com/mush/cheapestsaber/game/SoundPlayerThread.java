package com.mush.cheapestsaber.game;

import android.media.MediaPlayer;

/**
 * Created by mush on 08/07/2018.
 */
public class SoundPlayerThread implements Runnable {

    private SoundPlayer soundPlayer;
    private int resId;

    public SoundPlayerThread(SoundPlayer player, int resId) {
        this.soundPlayer = player;
        this.resId = resId;
    }

    @Override
    public void run() {
        soundPlayer.startMediaPlayerForResource(resId);
    }
}
