package com.jvminsight.jvmprofiler.dto;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler.dto
 * @NAME: Histogram
 * @USER: tangxiang
 * @DATE: 2024/7/31
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION:
 **/
@Data
public class Histogram {

    private AtomicLong count = new AtomicLong(0);
    private AtomicLong sum = new AtomicLong(0);
    private AtomicLong min = new AtomicLong(Long.MAX_VALUE);
    private AtomicLong max = new AtomicLong(Long.MIN_VALUE);
    public void appendValue(long value){
        count.incrementAndGet();
        sum.addAndGet(value);
        max.updateAndGet(x -> Math.min(value,x));
        max.updateAndGet(x -> Math.max(value,x));
    }

    public long getCount() {
        return count.get();
    }

    public long getSum() {
        return sum.get();
    }

    public long getMin() {
        return min.get();
    }

    public long getMax() {
        return max.get();
    }
}
