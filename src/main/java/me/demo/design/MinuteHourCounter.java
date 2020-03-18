package me.demo.design;

public interface MinuteHourCounter {
    void add(int count);

    int minuteCount();

    int hourCount();

}

class MinuteHourCounterImpl implements MinuteHourCounter {
    long hourStart = 0;

    public MinuteHourCounterImpl() {
        hourStart = System.currentTimeMillis();
    }

    @Override
    public void add(int count) {

    }

    @Override
    public int minuteCount() {
        //根据当前时间，获取分钟数，
        return 0;
    }

    @Override
    public int hourCount() {
        return 0;
    }
}

