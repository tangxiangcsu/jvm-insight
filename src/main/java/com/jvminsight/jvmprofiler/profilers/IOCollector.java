package com.jvminsight.jvmprofiler.profilers;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler.profilers
 * @NAME: IOCollector
 * @USER: tangxiang
 * @DATE: 2024/8/3
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION:
 **/

@Data
public class IOCollector {
    /**
     * 存储大小字节数
     */
    private static final int KB_SIZE = 1024;
    private static final int MB_SIZE = 1024 * 1024;
    private static final int GB_SIZE = 1024 * 1024 * 1024;

    /**
     *
     */
    private static final String PROC_SELF_STATUS_FILE = "/proc/self/status";
    /**
     * 当前进程的IO信息
     */
    private static final String PROC_SELF_IO_FILE = "/proc/self/io";
    /**
     * 系统范围的统计信息
     * 整体CPU使用情况、各个CPU核心的使用情况、系统启动时间、上下文切换次数、进程创建次数
     */
    private static final String PROC_STAT_FILE = "/proc/stat";
    /**
     * 进程启动时传递的所有参数
     */
    private static final String PROC_SELF_CMDLINE_FILE = "/proc/self/cmdline";
    private static final String VALUE_SEPARATOR = ":";

    public static Map<String, String> getProcStatus(){
        return getProcFileAsMap(PROC_SELF_STATUS_FILE);
    }
    public static Map<String, String> getProcIO() {
        return getProcFileAsMap(PROC_SELF_IO_FILE);
    }
    public static List<Map<String, Object>> getProcStatCpuTime() {
        List<String[]> rows = getProcFileAsRowColumn(PROC_STAT_FILE);
        return getProcStatCpuTime(rows);
    }

    public static Long getBytesValue(Map<String, String> data, String key){
        if(data==null)return null;

        if(key==null)return null;

        String value = data.get(key);
        if(value==null)return null;
        return getBytesValueOrNull(value);
    }
    public static Map<String, String> getProcFileAsMap(String filePath){
        try{
            File file = new File(filePath);
            if (!file.exists() || file.isDirectory() || !file.canRead()) {
                return Collections.emptyMap();
            }
            Map<String, String> result = new HashMap<>();
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            for (String line : lines) {
                int index = line.indexOf(VALUE_SEPARATOR);
                if (index <= 0 || index >= line.length() - 1) {
                    continue;
                }
                String key = line.substring(0, index).trim();
                String value = line.substring(index + 1).trim();
                result.put(key, value);
            }
            return result;
        }catch (Exception e){
            System.out.println("Failed to read file " + filePath + e);
            return Collections.emptyMap();
        }
    }

    public static List<String[]> getProcFileAsRowColumn(String filePath){
        try{
            File file = new File(filePath);
            if (!file.exists() || file.isDirectory() || !file.canRead()) {
                return Collections.emptyList();
            }

            List<String[]> result = new ArrayList<>();
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            for (String line : lines) {
                result.add(line.split("\\s+"));
            }
            return result;
        } catch (Throwable ex) {
            System.out.println("Failed to read file " + filePath + ex);
            return Collections.emptyList();
        }
    }

    public static List<Map<String, Object>> getProcStatCpuTime(Collection<String[]> rows){
        if(rows == null){
            return Collections.emptyList();
        }
        final int minValuesInRow = 6;
        List<Map<String, Object>> result = new ArrayList<>();

        for(String[] row : rows){
            if(row.length >= minValuesInRow && row[0].toLowerCase().startsWith("cpu")){
                Map<String, Object> map = new HashMap<>();
                try {
                    map.put("cpu", row[0]);
                    map.put("user", Long.parseLong(row[1].trim()));
                    map.put("nice", Long.parseLong(row[2].trim()));
                    map.put("system", Long.parseLong(row[3].trim()));
                    map.put("idle", Long.parseLong(row[4].trim()));
                    map.put("iowait", Long.parseLong(row[5].trim()));
                    result.add(map);
                } catch (Throwable ex) {
                    continue;
                }
            }
        }
        return result;
    }

    public static String getPid(){
        return getPid(PROC_SELF_STATUS_FILE);
    }

    public static String getPid(String filePath) {
        Map<String, String> procStatus = getProcFileAsMap(filePath);
        if (procStatus != null) {
            return procStatus.get("Pid");
        }

        return null;
    }

    public static String getCmdline(){
        try{
            File file = new File(PROC_SELF_CMDLINE_FILE);
            if(!file.exists()||file.isDirectory()||!file.canRead()){
                return null;
            }
            String cmdline = new String(Files.readAllBytes(Paths.get(file.getPath())));
            return cmdline.replace((char)0, ' ');
        } catch (IOException e) {
            System.out.println("Failed to read file " + PROC_SELF_CMDLINE_FILE + e);
            return null;
        }
    }

    public static Long getBytesValueOrNull(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }

        str = str.toLowerCase();
        int scale = 1;

        try {
            if (str.endsWith("kb")) {
                str = str.substring(0, str.length() - 2).trim();
                scale = KB_SIZE;
            } if (str.endsWith("k")) {
                str = str.substring(0, str.length() - 1).trim();
                scale = KB_SIZE;
            } else if (str.endsWith("mb")) {
                str = str.substring(0, str.length() - 2).trim();
                scale = MB_SIZE;
            } else if (str.endsWith("m")) {
                str = str.substring(0, str.length() - 1).trim();
                scale = MB_SIZE;
            } else if (str.endsWith("gb")) {
                str = str.substring(0, str.length() - 2).trim();
                scale = GB_SIZE;
            } else if (str.endsWith("g")) {
                str = str.substring(0, str.length() - 1).trim();
                scale = GB_SIZE;
            } else if (str.endsWith("bytes")) {
                str = str.substring(0, str.length() - "bytes".length()).trim();
                scale = 1;
            }

            str = str.replace(",", "");

            if (!NumberUtils.isNumber(str)) {
                return null;
            }

            double doubleValue = Double.parseDouble(str);
            return (long)(doubleValue * scale);
        } catch (Throwable ex) {
            return null;
        }
    }

}
