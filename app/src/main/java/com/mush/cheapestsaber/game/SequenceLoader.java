package com.mush.cheapestsaber.game;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mush on 07/07/2018.
 */
public class SequenceLoader {

    String text;

    public SequenceLoader(Context context, int textFileResourceId) {
        InputStream inputStream = context.getResources().openRawResource(textFileResourceId);

        text = readFile(inputStream);

        Log.i("loader", text);
    }

    private String readFile(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder sb = new StringBuilder();

        try {
            while (( line = reader.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
        } catch (IOException e) {
            return null;
        }

        return sb.toString();
    }
}
