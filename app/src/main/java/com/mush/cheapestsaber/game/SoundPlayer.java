package com.mush.cheapestsaber.game;

import android.content.Context;
import android.media.MediaPlayer;
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

    private List<MediaPlayer> channels;

    private Map<String, Integer> soundMap;

    private Context applicationContext;

    public SoundPlayer(Context appContext) {
        this.applicationContext = appContext;

        channels = new ArrayList<>();
        channels.add(new MediaPlayer());

        soundMap = new HashMap<>();
        soundMap.put("Drum1", R.raw.drum1);
        soundMap.put("Drum2", R.raw.drum2);
        soundMap.put("Drum3", R.raw.drum3);
        soundMap.put("Drum4", R.raw.drum4);
    }

    public void play(String soundName) {
        if (channels.size() >= 6) {
            Log.i("sound", "too many channels");
            return;
        }
        Integer resId = soundMap.get(soundName);
        if (resId != null) {
//            Log.i("sound", "play: " + soundName);
            new Thread(new SoundPlayerThread(this, resId)).start();
//            MediaPlayer mp = MediaPlayer.create(applicationContext, resId);
//            channels.add(mp);
//            mp.setOnCompletionListener(this);
//            mp.start();
        }
    }

    public void startMediaPlayerForResource(int resId) {
        MediaPlayer mp = MediaPlayer.create(applicationContext, resId);
        channels.add(mp);
        mp.setOnCompletionListener(this);
        mp.start();
    }

    public void release() {
        for (MediaPlayer mp : channels) {
            mp.stop();
            mp.reset();
            mp.release();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //Log.i("sound", "completed: ");
        mp.reset();
        mp.release();
        channels.remove(mp);
    }
}
