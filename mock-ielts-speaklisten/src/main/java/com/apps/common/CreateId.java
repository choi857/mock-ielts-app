package com.apps.common;

import java.util.Random;

public class CreateId {
    private static final long EPOCH = 1672531200000L; // 2023-01-01 00:00:00 UTC
    private static final int RANDOM_BITS = 23; // 随机数位数
    private static final long MAX_RANDOM = (1L << RANDOM_BITS) - 1; // 随机数最大值
    private static final Random random = new Random();
    private static long lastTimestamp = -1L;

    public static synchronized long generateId() {
        long timestamp = System.currentTimeMillis() - EPOCH; // 时间戳部分

        // 如果时间戳没有变化，重新生成随机数
        if (timestamp == lastTimestamp) {
            // 重新生成随机数，直到时间戳变化
            while (timestamp == lastTimestamp) {
                timestamp = System.currentTimeMillis() - EPOCH;
            }
        }

        lastTimestamp = timestamp;

        long randomPart = random.nextLong() & MAX_RANDOM; // 随机数部分
        return (timestamp << RANDOM_BITS) | randomPart;
    }
}
