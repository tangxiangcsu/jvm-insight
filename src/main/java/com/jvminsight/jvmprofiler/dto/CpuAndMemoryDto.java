package com.jvminsight.jvmprofiler.dto;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler.DTO
 * @NAME: CpuAndMemoryDTO
 * @USER: tangxiang
 * @DATE: 2024/7/29
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION:
 **/
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class CpuAndMemoryDto {
    private long epochMillis;
    private String name;
    private String host;
    private String processUuid;
    private String appId;
    private String tag;
    private String cluster;
    private String role;
    private Double processCpuLoad;
    private Double systemCpuLoad;
    private Long processCpuTime;
    private Double heapMemoryTotalUsed;
    private Double heapMemoryCommitted;
    private Double heapMemoryMax;
    private Double nonHeapMemoryTotalUsed;
    private Double nonHeapMemoryCommitted;
    private Double nonHeapMemoryMax;
    private List<GcMetric> gc;
    private List<MemoryPoolMetric> memoryPools;
    private List<BufferPoolMetric> bufferPools;
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class GcMetric {
        private String name;
        private Long collectionCount;
        private Long collectionTime;
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class MemoryPoolMetric {
        private String name;
        private String type;
        private Long usageCommitted;
        private Long usageMax;
        private Long usageUsed;
        private Long peakUsageCommitted;
        private Long peakUsageMax;
        private Long peakUsageUsed;
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class BufferPoolMetric {
        private String name;
        private Long count;
        private Long memoryUsed;
        private Long totalCapacity;
    }
}
