package org.firstinspires.ftc.teamcode.util.general.profiling;

import java.util.Vector;

public class TimeProfile implements Profile<TimeProfile>{

    private int startTime, endTime, avgDelta, elapsedTime;
    private Vector<Integer> deltaHistory;

    public TimeProfile(){}

    @Override
    public TimeProfile copy() {
        TimeProfile timeProfile = new TimeProfile();
        timeProfile.startTime = this.startTime;
        timeProfile.elapsedTime = this.elapsedTime;
        timeProfile.endTime = this.endTime;
        timeProfile.avgDelta = this.avgDelta;
        timeProfile.deltaHistory = (Vector<Integer>) this.deltaHistory.clone();
        return timeProfile;
    }

    public TimeProfile setStartTime(int startTime){
        this.startTime = startTime;
        return this;
    }

    public int getStartTime(){
        return startTime;
    }

    public TimeProfile setEndTime(int endTime){
        this.endTime = endTime;
        return this;
    }

    public int getEndTime(){
        return endTime;
    }

    public int getElapsedTime(){
        return elapsedTime;
    }

    public int getElapsedTime(int currentTime){
        return currentTime - startTime;
    }

    public TimeProfile addDelta(int delta){
        this.deltaHistory.add(delta);
        return this;
    }

    public TimeProfile updateAvgDelta(){
        double sum = 0;
        for (Integer n: this.deltaHistory) {
            sum += n;
        }
        sum /= this.deltaHistory.size();
        avgDelta = (int) sum;
        return this;
    }

    public int getAvgDelta(){
        return avgDelta;
    }
}
