package com.mush.cheapestsaber.game;

/**
 * Created by mush on 07/07/2018.
 */
public class SequenceSound extends SequenceItem {

    private String text;

    public SequenceSound(double delayTime) {
        super(delayTime);
    }

    public SequenceSound setText(String text) {
        this.text = text;
        return this;
    }

    public String getText() {
        return text;
    }
}
