package com.mush.cheapestsaber.game.sequence;

/**
 * Created by mush on 07/07/2018.
 */
public class SequenceSound extends SequenceItem {

    private String name;
    private double leadTime = 0.1;

    public SequenceSound(double delayTime) {
        super(delayTime);
    }

    public SequenceSound setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    protected boolean calculateActive(double currentTime) {
        return getStartTime() - leadTime < currentTime;
    }

}
