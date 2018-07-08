package com.mush.cheapestsaber.game;

import android.media.MediaPlayer;

/**
 * Created by mush on 08/07/2018.
 */
public class SoundPlayerThread implements Runnable {

    private SoundPlayer soundPlayer;
    private String soundName;

    public SoundPlayerThread(SoundPlayer player, String name) {
        this.soundPlayer = player;
        this.soundName = name;
    }

    @Override
    public void run() {
        soundPlayer.startMediaPlayerForSound(soundName);
    }
}
