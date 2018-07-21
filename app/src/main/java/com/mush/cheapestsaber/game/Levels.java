package com.mush.cheapestsaber.game;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mush on 13/07/2018.
 */
public class Levels {

    public class Level {
        public String fileName;
        public String title;
        public String difficulty;

        public Level(String line) {
            String[] parts = line.split(":");
            fileName = parts[0];
            title = parts[1];
            difficulty = parts[2];
        }
    }

    private static final String TAG = Levels.class.getSimpleName();
    private static final String fileName = "index";

    public List<Level> levels;

    public Levels(Context context) {
        InputStream inputStream = context.getResources().openRawResource(
                context.getResources().getIdentifier(fileName,
                        "raw", context.getPackageName()));

        readFile(inputStream);
    }

    private void readFile(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        levels = new ArrayList<>();

        try {
            while (( line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    Level level = new Level(line);
                    levels.add(level);
                    Log.i(TAG, "" + line);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "" + e);
        }
    }

}
