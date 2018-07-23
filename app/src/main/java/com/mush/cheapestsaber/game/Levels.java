package com.mush.cheapestsaber.game;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mush on 13/07/2018.
 */
public class Levels {

    public class Level {
        public String fileName;
        public String title;
        public String[] difficultyNames;

        public boolean hasAllFields() {
            return fileName != null && title != null && difficultyNames != null;
        }
    }

    private static final String TAG = Levels.class.getSimpleName();
    private static final String fileName = "index";

    public List<Level> levels;
    private Context context;

    public Levels(Context context) {
        this.context = context;

        InputStream inputStream = context.getResources().openRawResource(
                context.getResources().getIdentifier(fileName,
                        "raw", context.getPackageName()));

        readIndexFile(inputStream);
    }

    private void readIndexFile(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        levels = new ArrayList<>();

        try {
            while (( line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    Level level = readLevelFile(line);
                    if (level != null) {
                        levels.add(level);
                        Log.i(TAG, level.fileName);
                        Log.i(TAG, level.title);
                        Log.i(TAG, Arrays.toString(level.difficultyNames));

                    }
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "" + e);
        }
    }

    private Level readLevelFile(String levelFileName) {
        InputStream inputStream = context.getResources().openRawResource(
                context.getResources().getIdentifier(levelFileName,
                        "raw", context.getPackageName()));

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        Level level = new Level();
        level.fileName = levelFileName;

        String line;
        try {
            while (( line = reader.readLine()) != null && !level.hasAllFields()) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String parts[] = line.split("[\t ,]+");
                    switch (parts[0]) {
                        case "title":
                            level.title = TextUtils.join(" ", getLineParams(parts));
                            break;
                        case "difficulty":
                            level.difficultyNames = getLineParams(parts);
                            break;
                    }
                }
            }

            return level;

        } catch (IOException e) {
            Log.e(TAG, "" + e);
            return null;
        }
    }

    private String[] getLineParams(String[] line) {
        String[] params = new String[line.length - 1];
        if (params.length > 0) {
            System.arraycopy(line, 1, params, 0, params.length);
        }
        return params;
    }
}
