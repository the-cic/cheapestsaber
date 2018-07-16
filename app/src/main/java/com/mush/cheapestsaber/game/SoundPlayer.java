package com.mush.cheapestsaber.game;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import com.mush.cheapestsaber.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mush on 08/07/2018.
 */
public class SoundPlayer implements MediaPlayer.OnCompletionListener {

    private static final String TAG = SoundPlayer.class.getSimpleName();

    SoundPool pool;

    private Context applicationContext;

    private int soundId1;
    private int soundId2;
    private int soundId3;
    private int soundId4;
    private int soundId5;

    private Map<String, Integer> soundMap;

    public SoundPlayer(Context appContext) {
        this.applicationContext = appContext;

        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(7);
        builder.setAudioAttributes(new AudioAttributes.Builder().setFlags(AudioAttributes.USAGE_GAME).build());
        pool = new SoundPool.Builder().build();

        soundId1 = pool.load(appContext,  R.raw.drum1, 1);
        soundId2 = pool.load(appContext,  R.raw.drum2, 1);
        soundId3 = pool.load(appContext,  R.raw.drum3, 1);
        soundId4 = pool.load(appContext,  R.raw.drum4, 1);
        soundId5 = pool.load(appContext,  R.raw.drumlow1, 1);

        soundMap = new HashMap<>();
        soundMap.put("Drum1", soundId1);
        soundMap.put("Drum2", soundId2);
        soundMap.put("Drum3", soundId3);
        soundMap.put("Drum4", soundId4);
        soundMap.put("Boom1", soundId5);
    }

    public void play(String soundName) {
//        Log.i(TAG, "play " + soundName);
        Integer soundId = soundMap.get(soundName);
        if (soundId != null) {
            pool.play(soundId, 1, 1, 0, 0, 1);
        }
    }

    public void release() {
        pool.release();
        pool = null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //Log.i(TAG, "completed: ");
    }
}
