package com.mush.cheapestsaber.game;

import android.content.Context;
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

    private List<String> lines;
    private int beatsPerMinute = 1;
    private int linesPerBeat = 1;
    private double lineStepSeconds = 1;
    private double lineTime;
    private double targetDuration = 0.25;

    private enum State {NONE, DEFINITION, MAIN};
    private State state;
    private Map<String, List<String>> definitionMap;
    private List<String> currentDefinition;
    private TargetSequence sequence;

    public SequenceLoader(Context context, int textFileResourceId) {
        InputStream inputStream = context.getResources().openRawResource(textFileResourceId);

        readFile(inputStream);

//        Log.i("loader", "lines:\n" + lines);
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
            Log.e("loader", "" + e);
        }
    }

    public void parseInto(TargetSequence intoSequence) {
        sequence = intoSequence;
        sequence.clear();
        definitionMap.clear();
        state = State.NONE;

        for (String line : lines) {
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
        String[] parts = command.split("[ ,]");
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
        sequence.addItem(new SequenceSound(delay).setText(parts[1]));
    }

    private void parseTarget(String[] parts, double delay) {
        String shape = parts[0];
        String offsetXStr = parts.length > 1 ? parts[1] : null;
        String offsetYStr = parts.length > 2 ? parts[2] : null;
        char sideChar = shape.charAt(0);
        int side = sideChar == 'L' ? Target.SIDE_LEFT : 0;
        side = sideChar == 'R' ? Target.SIDE_RIGHT : side;
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
            case ">/":
                dirX = 1;
                dirY = 1;
                break;
            case "</":
                dirX = -1;
                dirY = -1;
                break;
            case "V/":
                dirY = 1;
                dirX = -1;
                break;
            case "^/":
                dirY = -1;
                dirX = 1;
                break;
        }

        int offsetX = side == Target.SIDE_LEFT ? -1 : 1;
        int offsetY = 0;

        offsetX = offsetXStr != null ? Integer.parseInt(offsetXStr) : offsetX;
        offsetY = offsetYStr != null ? Integer.parseInt(offsetYStr) : offsetY;

        sequence.addItem(new Target(delay, targetDuration).setShape(side, dirX, dirY).setOffset(offsetX, offsetY));
    }

    private void parseDefine(String[] commands) {
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
                break;
            case "start":
                state = State.MAIN;
                lineTime = 0;
                break;
        }
    }

    private void calculateTimeStep() {
        double secondsPerBeat = 60.0 / beatsPerMinute;
        lineStepSeconds = secondsPerBeat / linesPerBeat;
    }
}
