package com.jvminsight.jvmprofiler.profilers;

import com.jvminsight.jvmprofiler.constants.ProfilerConstants;
import com.jvminsight.jvmprofiler.reporter.Reporter;
import lombok.Data;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler.profilers
 * @NAME: CpuAndMemoryProfiler
 * @USER: tangxiang
 * @DATE: 2024/7/28
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION: 采集核心方法都是由ManagementFactory提供的接口
 **/
@Data
public class CpuAndMemoryProfiler extends ProfilerComm implements Profiler {
    /**
     * 解析器名称
     */
    public final static String PROFILER_NAME = "CpuAndMemory";
    /**
     * ProcessCpuLoad 属性名和index
     */
    private static final String ATTRIBUTE_NAME_ProcessCpuLoad = "ProcessCpuLoad";
    private static final int ATTRIBUTE_INDEX_ProcessCpuLoad = 0;
    /**
     * SystemCpuLoad 属性名和index
     */
    private static final String ATTRIBUTE_NAME_SystemCpuLoad = "SystemCpuLoad";
    private static final int ATTRIBUTE_INDEX_SystemCpuLoad = 1;
    /**
     * ProcessCpuTime属性名和index
     */
    private static final String ATTRIBUTE_NAME_ProcessCpuTime = "ProcessCpuTime";
    private static final int ATTRIBUTE_INDEX_ProcessCpuTime = 2;
    /**
     * 报告器
     */
    private Reporter reporter;
    /**
     * 默认扫描时间间隔
     */
    private long intervalMillis = ProfilerConstants.DEFAULT_METRIC_INTERVAL;
    /**
     *  JVMBean管理和监控应用程序
     */
    private MBeanServer mBeanServer;
    /**
     * JVM唯一标识MBean的对象名称
     */
    private ObjectName objectName;
    /**
     * 负责管理JVM内存情况
     */
    private MemoryMXBean memoryMXBean;
    /**
     * 负责管理GC情况
     */
    private List<GarbageCollectorMXBean> gcMXBeans;
    /**
     * 内存缓冲区情况
     */
    private List<MemoryPoolMXBean> memoryPoolMXBeans;

    public CpuAndMemoryProfiler(Reporter reporter){
        this.reporter = reporter;
        init();
    }

    public void init(){
        try{
            mBeanServer = ManagementFactory.getPlatformMBeanServer();
            objectName = ObjectName.getInstance("java.lang:type=OperatingSystem");
        }catch (Exception e){
            System.out.println("Load mBeanServer and objectName to throw Exception " + e.toString());
        }
        try{
            memoryMXBean = ManagementFactory.getMemoryMXBean();
        }catch (Exception e){
            System.out.println("Load memoryMXBean throw Exception "+e);
        }

        try{
            gcMXBeans =ManagementFactory.getGarbageCollectorMXBeans();
        }catch (Exception e){
            System.out.println("Load GarbageCollectorMXBeans throw Exception "+e);
        }

        try{
            memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
        }catch (Exception e){
            System.out.println("Load MemoryPoolMXBean throw Exception "+ e);
        }
    }

    /**
     * 获取CPU属性
     *
     * @return AttributeList
     */
    private AttributeList getCpuAttributes(){
        try {
            String[] names = new String[]{ATTRIBUTE_NAME_ProcessCpuLoad, ATTRIBUTE_NAME_SystemCpuLoad, ATTRIBUTE_NAME_ProcessCpuTime};
            AttributeList list = mBeanServer.getAttributes(objectName, names);
            if (list.size() != names.length) {
                System.out.println("Failed to get all attributes");
                return new AttributeList();
            }
            return list;
        } catch (Throwable ex) {
            System.out.println("Failed to get CPU MBean attributes" + ex);
            return null;
        }
    }
    @Override
    public long getIntervalMillis() {
        return 0;
    }

    @Override
    public void setReporter(Reporter reporter) {

    }

    /**
     * 监控器，获取监控属性
     */
    @Override
    public void profile() {
        Double processCpuLoad = null;
        Double systemCpuLoad = null;
        Long processCpuTime = null;

        AttributeList cpuAttributes = getCpuAttributes();
        /**
         * CPU使用情况
         */
        if(cpuAttributes!=null && cpuAttributes.size()>0){
            Attribute processCpuLoadAtt = (Attribute) cpuAttributes.get(ATTRIBUTE_INDEX_ProcessCpuLoad);
            processCpuLoad = (Double) processCpuLoadAtt.getValue();
            if (processCpuLoad == Double.NaN) {
                processCpuLoad = null;
            }
            Attribute systemCpuLoadAtt = (Attribute) cpuAttributes.get(ATTRIBUTE_INDEX_SystemCpuLoad);
            systemCpuLoad = (Double) systemCpuLoadAtt.getValue();
            if (systemCpuLoad == Double.NaN) {
                systemCpuLoad = null;
            }

            Attribute processCpuTimeAtt = (Attribute) cpuAttributes.get(ATTRIBUTE_INDEX_ProcessCpuTime);
            processCpuTime = (Long) processCpuTimeAtt.getValue();

        }

        /**
         * 堆内存使用情况
         */
        Double heapMemoryTotalUsed = null;
        Double heapMemoryCommitted = null;
        Double heapMemoryMax = null;
        /**
         * 非堆内存使用情况
         */
        Double nonHeapMemoryTotalUsed = null;
        Double nonHeapMemoryCommitted = null;
        Double nonHeapMemoryMax = null;
        if(memoryMXBean != null){
            MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
            heapMemoryTotalUsed = new Double(heapMemoryUsage.getUsed());
            heapMemoryCommitted = new Double(heapMemoryUsage.getCommitted());
            heapMemoryMax = new Double(heapMemoryUsage.getMax());

            MemoryUsage noHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
            nonHeapMemoryTotalUsed = new Double(noHeapMemoryUsage.getUsed());
            nonHeapMemoryCommitted = new Double(noHeapMemoryUsage.getCommitted());
            nonHeapMemoryMax =  new Double(noHeapMemoryUsage.getMax());

        }
        /**
         * GC情况
         */
        List<Map<String, Object>> gcMetrics = new ArrayList<>();
        if(gcMXBeans != null){
            for(GarbageCollectorMXBean gcMXBean: gcMXBeans){
                Map<String, Object> gcMap = new HashMap<>();
                gcMap.put("name", gcMXBean.getName());
                gcMap.put("collectionCount", new Long(gcMXBean.getCollectionCount()));
                gcMap.put("collectionTime", new Long(gcMXBean.getCollectionTime()));
                gcMetrics.add(gcMap);
            }
        }
        /**
         * 内存池情况指标
         *
         * name：内存池的名称，例如 "Code Cache"、"Metaspace" 等。
         * type：内存池的类型，可能是 Heap memory 或 Non-heap memory。
         * usageCommitted：当前分配给该内存池的内存量（字节）。
         * usageMax：该内存池可以使用的最大内存量（字节）。如果没有限制，则返回 -1。
         * usageUsed：当前该内存池已使用的内存量（字节）。
         * peakUsageCommitted：该内存池峰值使用期间分配的内存量（字节）。
         * peakUsageMax：该内存池峰值使用期间可以使用的最大内存量（字节）。
         * peakUsageUsed：该内存池峰值使用期间已使用的内存量（字节）。
         *
         */
        List<Map<String, Object>> memoryPoolsMetrics = new ArrayList<>();

        for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
            Map<String, Object> memoryPoolMap = new HashMap<>();

            memoryPoolMap.put("name", pool.getName());
            memoryPoolMap.put("type", pool.getType().toString());
            memoryPoolMap.put("usageCommitted", pool.getUsage().getCommitted());
            memoryPoolMap.put("usageMax", pool.getUsage().getMax());
            memoryPoolMap.put("usageUsed", pool.getUsage().getUsed());
            memoryPoolMap.put("peakUsageCommitted", pool.getPeakUsage().getCommitted());
            memoryPoolMap.put("peakUsageMax", pool.getPeakUsage().getMax());
            memoryPoolMap.put("peakUsageUsed", pool.getPeakUsage().getUsed());
            memoryPoolsMetrics.add(memoryPoolMap);
        }

        /**
         * 直接缓冲区和映射缓冲区的使用情况
         */
        List<Map<String, Object>> bufferPoolsMetrics = new ArrayList<>();
        List<BufferPoolMXBean> bufferPools = ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class);
        if (bufferPools != null) {
            for (BufferPoolMXBean pool : bufferPools) {
                Map<String, Object> bufferPoolMap = new HashMap<>();

                bufferPoolMap.put("name", pool.getName());
                bufferPoolMap.put("count", new Long(pool.getCount()));
                bufferPoolMap.put("memoryUsed", new Long(pool.getMemoryUsed()));
                bufferPoolMap.put("totalCapacity", new Long(pool.getTotalCapacity()));

                bufferPoolsMetrics.add(bufferPoolMap);
            }
        }

        Map<String, Object> metricMap = new HashMap<String, Object>();

        metricMap.put("epochMillis", System.currentTimeMillis());
        metricMap.put("name", getProcessName());
        metricMap.put("host", getHostName());
        metricMap.put("processUuid", getProcessUuid());
        metricMap.put("appId", getAppId());

        if (getTag() != null) {
            metricMap.put("tag", getTag());
        }

        if (getCluster() != null) {
            metricMap.put("cluster", getCluster());
        }

        if (getRole() != null) {
            metricMap.put("role", getRole());
        }

        metricMap.put("processCpuLoad", processCpuLoad);
        metricMap.put("systemCpuLoad", systemCpuLoad);
        metricMap.put("processCpuTime", processCpuTime);

        metricMap.put("heapMemoryTotalUsed", heapMemoryTotalUsed);
        metricMap.put("heapMemoryCommitted", heapMemoryCommitted);
        metricMap.put("heapMemoryMax", heapMemoryMax);

        metricMap.put("nonHeapMemoryTotalUsed", nonHeapMemoryTotalUsed);
        metricMap.put("nonHeapMemoryCommitted", nonHeapMemoryCommitted);
        metricMap.put("nonHeapMemoryMax", nonHeapMemoryMax);

        metricMap.put("gc", gcMetrics);

        metricMap.put("memoryPools", memoryPoolsMetrics);
        metricMap.put("bufferPools", bufferPoolsMetrics);



        if(reporter != null){
            reporter.report(PROFILER_NAME, metricMap);
        }
    }
}
