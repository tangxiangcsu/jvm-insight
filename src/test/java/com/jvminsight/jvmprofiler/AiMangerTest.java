package com.jvminsight.jvmprofiler;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvminsight.jvmprofiler.dto.CpuAndMemoryDto;
import com.jvminsight.jvmprofiler.ai.AiManager;
import com.jvminsight.jvmprofiler.ai.JvmCpuMemoryEntry;
import com.jvminsight.jvmprofiler.constants.AiConstants;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;


/**
 * @PACKAGE_NAME: PACKAGE_NAME
 * @NAME: AiMangerTest
 * @USER: tangxiang
 * @DATE: 2024/7/29
 * @DESCRIPTION:
 **/
@SpringBootTest
public class AiMangerTest {
    @Resource
    AiManager aiManager;

    @Test
    public void test_AiManger() throws JsonProcessingException {
        /**
         * 封装Prompt
         */
        String JVMMessage = getGenerateJvmForCpuAndMemoryOptimizationMessage(new JvmCpuMemoryEntry());
        String result = aiManager.doSyncRequest(AiConstants.GENERATE_JVM_CPU_AND_MEMORY_OPTIMIZATION_SYSTEM_MESSAGE, JSONUtil.toJsonStr(builderJSON()), null);
        // 截取需要的 JSON 信息
        int start = result.indexOf("[");
        int end = result.lastIndexOf("]");
        String json = result.substring(start, end + 1);
        //AnswerContentDTO answerContentDTO = JSONUtil.toBean(json, AnswerContentDTO.class);
        System.out.println(json);
    }

    public String getGenerateJvmForCpuAndMemoryOptimizationMessage(JvmCpuMemoryEntry entry){
        StringBuilder JVMMessage = new StringBuilder();
        JVMMessage.append(entry.getName());
        JVMMessage.append(entry.getDesc());
        JVMMessage.append(entry.getType());
        JVMMessage.append(entry.getProblemNumber());
        JVMMessage.append(entry.getSuggestionNumber());
        return JVMMessage.toString();
    }

    public CpuAndMemoryDto builderJSON() throws JsonProcessingException {
        String json = "{\"heapMemoryMax\":2.109734912E9,\"nonHeapMemoryTotalUsed\":8130568.0,\"bufferPools\":[{\"totalCapacity\":0,\"name\":\"mapped\",\"count\":0,\"memoryUsed\":0},{\"totalCapacity\":0,\"name\":\"direct\",\"count\":0,\"memoryUsed\":0},{\"totalCapacity\":0,\"name\":\"mapped - \\u0027non-volatile memory\\u0027\",\"count\":0,\"memoryUsed\":0}],\"heapMemoryTotalUsed\":1.9922944E7,\"epochMillis\":1723376934076,\"nonHeapMemoryCommitted\":1.3238272E7,\"heapMemoryCommitted\":1.32120576E8,\"memoryPools\":[{\"peakUsageMax\":5898240,\"usageMax\":5898240,\"peakUsageUsed\":1202816,\"name\":\"CodeHeap \\u0027non-nmethods\\u0027\",\"peakUsageCommitted\":2555904,\"usageUsed\":1186560,\"type\":\"Non-heap memory\",\"usageCommitted\":2555904},{\"peakUsageMax\":-1,\"usageMax\":-1,\"peakUsageUsed\":4699696,\"name\":\"Metaspace\",\"peakUsageCommitted\":4915200,\"usageUsed\":4699696,\"type\":\"Non-heap memory\",\"usageCommitted\":4915200},{\"peakUsageMax\":122880000,\"usageMax\":122880000,\"peakUsageUsed\":1429248,\"name\":\"CodeHeap \\u0027profiled nmethods\\u0027\",\"peakUsageCommitted\":2555904,\"usageUsed\":1429248,\"type\":\"Non-heap memory\",\"usageCommitted\":2555904},{\"peakUsageMax\":1073741824,\"usageMax\":1073741824,\"peakUsageUsed\":523608,\"name\":\"Compressed Class Space\",\"peakUsageCommitted\":655360,\"usageUsed\":523608,\"type\":\"Non-heap memory\",\"usageCommitted\":655360},{\"peakUsageMax\":-1,\"usageMax\":-1,\"peakUsageUsed\":11534336,\"name\":\"G1 Eden Space\",\"peakUsageCommitted\":26214400,\"usageUsed\":11534336,\"type\":\"Heap memory\",\"usageCommitted\":26214400},{\"peakUsageMax\":2109734912,\"usageMax\":2109734912,\"peakUsageUsed\":8388608,\"name\":\"G1 Old Gen\",\"peakUsageCommitted\":105906176,\"usageUsed\":8388608,\"type\":\"Heap memory\",\"usageCommitted\":105906176},{\"peakUsageMax\":-1,\"usageMax\":-1,\"peakUsageUsed\":0,\"name\":\"G1 Survivor Space\",\"peakUsageCommitted\":0,\"usageUsed\":0,\"type\":\"Heap memory\",\"usageCommitted\":0},{\"peakUsageMax\":122880000,\"usageMax\":122880000,\"peakUsageUsed\":291456,\"name\":\"CodeHeap \\u0027non-profiled nmethods\\u0027\",\"peakUsageCommitted\":2555904,\"usageUsed\":291456,\"type\":\"Non-heap memory\",\"usageCommitted\":2555904}],\"processCpuLoad\":0.08335573935607224,\"systemCpuLoad\":0.1235764904981842,\"processCpuTime\":328125000,\"processUuid\":\"abb532fb-68ef-4af4-9ef2-fe811fa34c6b\",\"nonHeapMemoryMax\":-1.0,\"tag\":\"mytag\",\"gc\":[{\"collectionTime\":0,\"name\":\"G1 Young Generation\",\"collectionCount\":0},{\"collectionTime\":0,\"name\":\"G1 Old Generation\",\"collectionCount\":0}]}";

        // 将 JSON 字符串转换为 CpuAndMemoryDto 对象
        CpuAndMemoryDto dto = JSONUtil.toBean(json, CpuAndMemoryDto.class);
        return dto;
    }

}
