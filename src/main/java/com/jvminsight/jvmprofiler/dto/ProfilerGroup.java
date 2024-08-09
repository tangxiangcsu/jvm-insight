package com.jvminsight.jvmprofiler.dto;

import com.jvminsight.jvmprofiler.profilers.Profiler;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler.dto
 * @NAME: ProfilerGroup
 * @USER: tangxiang
 * @DATE: 2024/8/3
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION: 设置立即启动解析器 and 按计划启动解析器
 **/
@Data
public class ProfilerGroup {

    private List<Profiler> oneTimeProfilers;
    private List<Profiler> periodicProfilers;

    public ProfilerGroup(List<Profiler> oneTimeProfilers, List<Profiler> periodicProfilers) {
        this.oneTimeProfilers = new ArrayList<>(oneTimeProfilers);
        this.periodicProfilers = new ArrayList<>(periodicProfilers);
    }
}
