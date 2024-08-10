package com.jvminsight.jvmprofiler.dto;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler.dto
 * @NAME: ThreadInfoMetricDto
 * @USER: tangxiang
 * @DATE: 2024/8/4
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION:
 **/
import lombok.Data;

@Data
public class ThreadInfoMetricDto {
    private long epochMillis;
    private String name;
    private String host;
    private String processUuid;
    private String appId;
    private String tag;
    private String cluster;
    private String role;
    private long totalStartedThreadCount;
    private long newThreadCount;
    private int liveThreadCount;
    private int peakThreadCount;
}
