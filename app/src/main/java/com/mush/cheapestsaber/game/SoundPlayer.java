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

    private static final String TAG = SoundPlayer.class.getSimpleName();

    private final static int MAX_CHANNELS_PER_SOUND = 5;
    private Map<String, Integer> soundMap;
    private Map<String, List<MediaPlayer>> voiceMap;

    private Context applicationContext;

    public SoundPlayer(Context appContext) {
        this.applicationContext = appContext;

        voiceMap = new HashMap<>();

        soundMap = new HashMap<>();
        soundMap.put("Drum1", R.raw.drum1);
        soundMap.put("Drum2", R.raw.drum2);
        soundMap.put("Drum3", R.raw.drum3);
        soundMap.put("Drum4", R.raw.drum4);
    }

    public void play(String soundName) {
//        Log.i(TAG, "play " + soundName);
        startMediaPlayerForSound(soundName);
    }

    public MediaPlayer getMediaPlayerForSound(String name) {
        Integer resId = soundMap.get(name);
        if (resId == null) {
            return null;
        }
        List<MediaPlayer> list = voiceMap.get(name);
        if (list == null) {
            list = new ArrayList<>();
            voiceMap.put(name, list);
        }
        MediaPlayer mp = null;
        for (MediaPlayer aMp : list) {
            if (!aMp.isPlaying()) {
                mp = aMp;
                break;
            }
        }
        if (mp == null && list.size() >= MAX_CHANNELS_PER_SOUND) {
            Log.i(TAG, "too many channels");
            return null;
        }
        if (mp == null) {
            Log.i(TAG, "creating new MP for " + name);
            mp = MediaPlayer.create(applicationContext, resId);
            mp.setOnCompletionListener(this);
            list.add(mp);
        }
        return mp;
    }

    public void startMediaPlayerForSound(String soundName) {
        MediaPlayer mp = getMediaPlayerForSound(soundName);
        if (mp != null) {
            mp.seekTo(0);
            mp.start();
        }
    }

    public void release() {
        for (List<MediaPlayer> list : voiceMap.values()) {
            for (MediaPlayer mp : list) {
                mp.stop();
                mp.reset();
                mp.release();
            }
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //Log.i(TAG, "completed: ");
    }
}
