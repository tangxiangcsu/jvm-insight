package com.jvminsight.jvmprofiler;

import cn.hutool.json.JSONUtil;
import com.jvminsight.jvmprofiler.dto.CpuAndMemoryDto;
import com.jvminsight.jvmprofiler.ai.AiManager;
import com.jvminsight.jvmprofiler.ai.JvmCpuMemoryEntry;
import com.jvminsight.jvmprofiler.constants.AiConstants;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


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
    public void test_AiManger(){
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

    public CpuAndMemoryDto builderJSON(){
        /*CpuAndMemoryDto cpuAndMemoryDTO = new CpuAndMemoryDto(
                11890584L,
                Arrays.asList(
                        new CpuAndMemoryDto.BufferPool(0L, "direct", 0, 0L),
                        new CpuAndMemoryDto.BufferPool(0L, "mapped", 0, 0L)
                ),
                24330736L,
                1515627003374L,
                13565952L,
                257425408L,
                Arrays.asList(
                        new CpuAndMemoryDto.MemoryPool(251658240L, 251658240L, 1194496L, "Code Cache", 2555904L, 1173504L, "Non-heap memory", 2555904L),
                        new CpuAndMemoryDto.MemoryPool(-1L, -1L, 9622920L, "Metaspace", 9830400L, 9622920L, "Non-heap memory", 9830400L),
                        new CpuAndMemoryDto.MemoryPool(1073741824L, 1073741824L, 1094160L, "Compressed Class Space", 1179648L, 1094160L, "Non-heap memory", 1179648L),
                        new CpuAndMemoryDto.MemoryPool(1409286144L, 1409286144L, 24330736L, "PS Eden Space", 67108864L, 24330736L, "Heap memory", 67108864L),
                        new CpuAndMemoryDto.MemoryPool(11010048L, 11010048L, 0L, "PS Survivor Space", 11010048L, 0L, "Heap memory", 11010048L),
                        new CpuAndMemoryDto.MemoryPool(2863661056L, 2863661056L, 0L, "PS Old Gen", 179306496L, 0L, "Heap memory", 179306496L)
                ),
                0.0008024004394748531,
                0.23138430784607697,
                496918000L,
                null,
                "24103@machine01",
                "machine01",
                "3c2ec835-749d-45ea-a7ec-e4b9fe17c23a",
                "mytag",
                Arrays.asList(
                        new CpuAndMemoryDto.GC(0L, "PS Scavenge", 0),
                        new CpuAndMemoryDto.GC(0L, "PS MarkSweep", 0)
                )
        );
        return cpuAndMemoryDTO;*/
        return null;
    }

}
