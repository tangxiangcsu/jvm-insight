package com.jvminsight.jvmprofiler.dto;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler.dto
 * @NAME: IOProfilerDto
 * @USER: tangxiang
 * @DATE: 2024/8/4
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION:
 **/
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class IOProfilerDto {
    private long epochMillis;
    private String name;
    private String host;
    private String processUuid;
    private String appId;
    private String tag;
    private String cluster;
    private String role;
    private Self self;
    private List<Map<String, Object>> stat;

    @Data
    public static class Self {
        private Io io;
    }

    @Data
    public static class Io {
        private Long rchar;
        private Long wchar;
        private Long readBytes;
        private Long writeBytes;
    }
}