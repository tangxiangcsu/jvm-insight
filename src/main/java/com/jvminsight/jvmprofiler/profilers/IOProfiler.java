package com.jvminsight.jvmprofiler.profilers;

import com.jvminsight.jvmprofiler.constants.ProfilerConstants;
import com.jvminsight.jvmprofiler.reporter.Reporter;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler.profilers
 * @NAME: IOProfiler
 * @USER: tangxiang
 * @DATE: 2024/8/2
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION:
 **/
@Data
public class IOProfiler extends ProfilerComm implements Profiler{

    public final static String PROFILER_NAME = "IO";

    private long intervalMillis = ProfilerConstants.DEFAULT_METRIC_INTERVAL;

    private Reporter reporter;

    public IOProfiler(Reporter reporter) {
        setReporter(reporter);
    }


    @Override
    public long getIntervalMillis() {
        return this.intervalMillis;
    }

    @Override
    public void setReporter(Reporter reporter) {
        this.reporter = reporter;
    }

    @Override
    public void profile() {
        Map<String, String> procMap = IOCollector.getProcIO();
        Long rchar = IOCollector.getBytesValue(procMap, "rchar");
        Long wchar = IOCollector.getBytesValue(procMap, "wchar");
        Long read_bytes = IOCollector.getBytesValue(procMap, "read_bytes");
        Long write_bytes = IOCollector.getBytesValue(procMap, "write_bytes");

        List<Map<String, Object>> cpuTime = IOCollector.getProcStatCpuTime();

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("epochMillis", System.currentTimeMillis());
        map.put("name", getProcessName());
        map.put("host", getHostName());
        map.put("processUuid", getProcessUuid());
        map.put("appId", getAppId());

        if (getTag() != null) {
            map.put("tag", getTag());
        }

        if (getCluster() != null) {
            map.put("cluster", getCluster());
        }

        if (getRole() != null) {
            map.put("role", getRole());
        }

        Map<String, Object> selfMap = new HashMap<String, Object>();
        map.put("self", selfMap);

        Map<String, Object> ioMap = new HashMap<String, Object>();
        selfMap.put("io", ioMap);

        ioMap.put("rchar", rchar);
        ioMap.put("wchar", wchar);
        ioMap.put("read_bytes", read_bytes);
        ioMap.put("write_bytes", write_bytes);

        map.put("stat", cpuTime);

        if (reporter != null) {
            reporter.report(PROFILER_NAME, map);
        }
    }
}
