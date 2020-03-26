package me.demo.design;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/***
 * 1.按时间戳记录访问日志
 * 2.异步删除1小时之前的数据
 * 3.实时统计1小时内的流量，1分钟内的流量
 *
 */
class MinuteHourCounterImpl implements MinuteHourCounter {
    //可以考虑用map实现
    private List<TrafficData> traffic = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        MinuteHourCounter counter = new MinuteHourCounterImpl();
        long start = System.currentTimeMillis();
        IntStream.range(0, 8).parallel().forEach(e -> {
            Random random = new Random();
            while (true)
                try {
                    Thread.sleep(1000);
                    counter.add(random.nextInt(100));
                    if (System.currentTimeMillis() - start > 720000) {
                        break;
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
        });
        System.out.println(counter.hourCount());
        System.out.println(counter.minuteCount());
    }

    public MinuteHourCounterImpl() {
        //启动异步线程删除1小时之前的数据
//        new Thread(() -> {
//            try {
//                TimeUnit.SECONDS.sleep(60);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            List<TrafficData> toDeleteTraffic = traffic.parallelStream()
//                    .filter(e -> (e.time - System.currentTimeMillis()) > 3600000)
//                    .collect(Collectors.toList());
//            //// TODO: 2020-03-24 如何 保持线程安全
//            traffic.removeAll(toDeleteTraffic);
//        }).start();
    }

    @Override
    public void add(int count) {
        traffic.add(new TrafficData(count));
    }

    @Override
    public int minuteCount() {
        return traffic.parallelStream()
                .filter(e -> (e.time - System.currentTimeMillis()) <= 60000)
                .mapToInt(e -> e.count)
                .sum();
    }

    @Override
    public int hourCount() {
        return traffic.parallelStream()
                .filter(e -> (e.time - System.currentTimeMillis()) <= 3600000)
                .mapToInt(e -> e.count)
                .sum();
    }


    static class TrafficData {
        long time;
        int count;

        public TrafficData(int count) {
            this.count = count;
            this.time = System.currentTimeMillis();
        }
    }
}

public interface MinuteHourCounter {
    void add(int count);

    int minuteCount();

    int hourCount();
}


