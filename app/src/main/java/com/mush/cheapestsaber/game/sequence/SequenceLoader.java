package com.mush.cheapestsaber.game.sequence;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mush on 07/07/2018.
 */
public class SequenceLoader {

    private static final String TAG = SequenceLoader.class.getSimpleName();

    private List<String> lines;
    private int beatsPerMinute = 1;
    private int linesPerBeat = 1;
    private double lineStepSeconds = 1;
    private double lineTime;
    private double targetDuration = 0.35;

    private enum State {NONE, DEFINITION, MAIN};
    private State state;
    private Map<String, List<String>> definitionMap;
    private List<String> currentDefinition;
    private TargetSequence sequence;
    private int sequenceDifficulty;

    public SequenceLoader(Context context, String fileName) {

        InputStream inputStream = context.getResources().openRawResource(
                context.getResources().getIdentifier(fileName,
                        "raw", context.getPackageName()));

        readFile(inputStream);

//        Log.i(TAG, "lines:\n" + lines);
        definitionMap = new HashMap<>();
    }

    private void readFile(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        lines = new ArrayList<>();

        try {
            while (( line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            Log.e(TAG, "" + e);
        }
    }

    public void parseInto(TargetSequence intoSequence, int difficulty) {
        sequence = intoSequence;
        sequence.clear();
        sequenceDifficulty = difficulty;
        definitionMap.clear();
        state = State.NONE;

        // looping by index as preprocess() changes the content
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] commands = line.trim().isEmpty() ? null : line.split(";");
            if (commands == null) {
                state = State.NONE;
            } else {
                switch (state) {
                    case NONE:
                        parseNone(commands);
                        break;
                    case DEFINITION:
                        parseDefine(commands);
                        break;
                    case MAIN:
                        parseMain(commands);
                        break;
                }
            }
        }

//        sequence.log();
    }

    private void startMain() {
        preprocess();
        state = State.MAIN;
        lineTime = 0;
    }

    private void preprocess() {
        final String PLAY = "@play ";
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] commands = line.trim().isEmpty() ? null : line.split(";");
            if (commands != null){
                for (String command : commands) {
                    String trimCommand = command.trim();
                    if (trimCommand.startsWith(PLAY)) {
                        String name = trimCommand.substring(PLAY.length()).trim();
                        merge(name, i);
                    }
                }
            }
        }

//        Log.d(TAG, "lines:");
//        for (int i = 0; i < lines.size(); i++) {
//            String line = lines.get(i);
//            Log.d(TAG, i + ": " + line);
//        }
    }

    private void merge(String name, int startIndex) {
        Log.i(TAG, "merge " + name + " at " + startIndex);
        List<String> definition = definitionMap.get(name);
        Log.d(TAG, "definition: " + definition);
        if (definition != null) {
            for (int i = 0; i < definition.size(); i++) {
                int index = startIndex + i;
                String addLine = definition.get(i);
                if (index < lines.size()) {
                    lines.set(index, lines.get(index) + ";" + addLine);
                }
            }
        }
    }

    private void parseMain(String[] commands) {
        for (String aCommand : commands) {
            String command = aCommand.trim();
            if (!command.isEmpty()) {
                parseMain(command);
            }
        }
        lineTime += lineStepSeconds;
    }

    private void parseMain(String command) {
        String[] parts = command.split("[\t ,]+");
        if (parts.length == 0) {
            return;
        }

        SequenceItem lastItem = sequence.getLastItem();
        double delay = lastItem != null ? lineTime - lastItem.getStartTime() : 0;

        //Log.i("parseInto", parts[0]);
        switch (parts[0].trim()) {
            case "play":
                //Log.i("parseInto", "is play");
                parsePlay(parts, delay);
                break;
            default:
                parseTarget(parts, delay);
        }
    }

    private void parsePlay(String[] parts, double delay) {
        //Log.i("parseInto", "is play, delay: "+ delay+" text: "+ parts[1] );
        sequence.addItem(new SequenceSound(delay).setName(parts[1]));
    }

    private Integer getTargetDifficulty(String first) {
        try {
            return Integer.parseInt(first);

        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private int getTargetSide(String shape) {
        char sideChar = shape.charAt(0);
        int side;
        switch (sideChar) {
            case 'L':
                side = Target.SIDE_LEFT;
                break;
            case 'R':
                side = Target.SIDE_RIGHT;
                break;
            default:
                side = 0;
        }
        return side;
    }

    private void parseTarget(String[] parts, double delay) {
        int startIndex = 0;

        Integer difficulty = getTargetDifficulty(parts[startIndex]);

        if (difficulty != null) {
            if (difficulty != sequenceDifficulty) {
                return;
            } else {
                startIndex++;
            }
        }

        String shape = parts[startIndex];
        String offsetXStr = parts.length > startIndex + 1 ? parts[startIndex + 1] : null;
        String offsetYStr = parts.length > startIndex + 2 ? parts[startIndex + 2] : null;

        int side = getTargetSide(shape);
        if (side == 0) {
            return;
        }

        int dirX = 0;
        int dirY = 0;
        String dirStr = shape.substring(1);
        switch (dirStr) {
            case ">":
                dirX = 1;
                break;
            case "<":
                dirX = -1;
                break;
            case "V":
                dirY = 1;
                break;
            case "^":
                dirY = -1;
                break;
            case ">V": case "V>":
                dirX = 1;
                dirY = 1;
                break;
            case "<^": case "^<":
                dirX = -1;
                dirY = -1;
                break;
            case "V<": case "<V":
                dirX = -1;
                dirY = 1;
                break;
            case "^>": case ">^":
                dirX = 1;
                dirY = -1;
                break;
        }

        float offsetX = side == Target.SIDE_LEFT ? -1 : 1;
        float offsetY = 0;

        offsetX = offsetXStr != null ? Float.parseFloat(offsetXStr) : offsetX;
        offsetY = offsetYStr != null ? Float.parseFloat(offsetYStr) : offsetY;

        sequence.addItem(new Target(delay, targetDuration).setShape(side, dirX, dirY).setOffset(offsetX, offsetY));
    }

    private void startDefine(String definitionName) {
        currentDefinition = new ArrayList<>();
        definitionMap.put(definitionName.trim(), currentDefinition);
        state = State.DEFINITION;
        Log.i(TAG, "start define: " + definitionName);
    }

    private void parseDefine(String[] commands) {
        String line = TextUtils.join(";", commands);
        currentDefinition.add(line);
        Log.d(TAG, "parse define: " +line);
    }

    private void parseNone(String[] commands) {
        for (String aCommand : commands) {
            String command = aCommand.trim();
            if (!command.isEmpty()) {
                parseNone(command);
            }
        }
    }

    private void parseNone(String command) {
        String[] parts = command.split(" ");
        if (parts.length == 0) {
            return;
        }
        switch (parts[0]) {
            case "bpm":
                beatsPerMinute = Integer.parseInt(parts[1]);
                beatsPerMinute = beatsPerMinute <= 0 ? 1 : beatsPerMinute;
                calculateTimeStep();
                break;
            case "lpb":
                linesPerBeat = Integer.parseInt(parts[1]);
                linesPerBeat = linesPerBeat <= 0 ? 1 : linesPerBeat;
                calculateTimeStep();
                break;
            case "define":
                startDefine(parts[1]);
                break;
            case "start":
                startMain();
                break;
        }
    }

    private void calculateTimeStep() {
        double secondsPerBeat = 60.0 / beatsPerMinute;
        lineStepSeconds = secondsPerBeat / linesPerBeat;
    }
}
