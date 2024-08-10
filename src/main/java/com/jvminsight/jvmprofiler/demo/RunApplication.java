package com.jvminsight.jvmprofiler.demo;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @PACKAGE_NAME: com.jvminsight.jvmprofiler.demo
 * @NAME: RunApplication
 * @USER: tangxiang
 * @DATE: 2024/8/10
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION:
 **/
public class RunApplication {
    public static void main(String[] args) {
        long totalRunningMillis = 1 * 60 * 1000;
        long sleepMillis = 1000;

        if (args.length >= 1) {
            totalRunningMillis = Long.parseLong(args[0]);
        }

        if (args.length >= 2) {
            sleepMillis = Long.parseLong(args[1]);
        }

        long startMillis = System.currentTimeMillis();
        long lastPrintMillis = 0;

        Random random = new Random();
        System.out.println("start to test Run Application");
        while (System.currentTimeMillis() - startMillis < totalRunningMillis) {
            if (System.currentTimeMillis() - lastPrintMillis >= 10000) {
                System.out.println("Hello World " + System.currentTimeMillis());
                lastPrintMillis = System.currentTimeMillis();
            }

            sleepMillis += random.nextInt(100);
            sleepMillis -= random.nextInt(100);

            privateSleepMethod(sleepMillis);

            AtomicLong atomicLong = new AtomicLong(sleepMillis);
            publicSleepMethod(atomicLong);
        }
    }

    private static void privateSleepMethod(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void publicSleepMethod(AtomicLong millis) {
        try {
            Thread.sleep(millis.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
