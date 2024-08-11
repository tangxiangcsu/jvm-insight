

# 🌈 什么是AI-JVM-Profiler？
一款对应用程序无侵害的JVM内存解析系统，
![本地路径](ai-jvm.png "相对路径演示")



# 📚 妙用AI-JVM-Profiler
## 多种解析器，挑选一下吧
### 🚀 CpuAndMemory解析器
CpuAndMenory负责解析程序运行过程中CPU、堆内堆外内存和GC的各种情况，收集指标如下：

**1. 基本指标**

| Title      | metricValue |Note| Options
| ----------- | ----------- |----------- |----------- |
| appId      | null       |程序的唯一标识| |
| name   | *        | 应用程序的名称| |
|processUuid|a0b98ea4-0995-451e-ba89-8ee40c563eef|进程标识
|tag|mytag|标签
|heapMemoryMax|2109734912|堆内存的最大值
|nonHeapMemoryMax|-1|非堆内存的最大值
|nonHeapMemoryTotalUsed|14656800|当前非堆内存的已使用量
|nonHeapMemoryCommitted|18350080|非堆内存的已提交量
|heapMemoryCommitted|132120576|堆内存的已提交量
|heapMemoryTotalUsed|15865344|堆内存的已使用量
|systemCpuLoad|0.00305371|系统CPU负载率
|processCpuLoad|0.00052097|进程CPU负载率
|processCpuTime|437500000| | 纳秒
|epochMillis|1722683736110|数据采集的时间|毫秒，自1979.01.01 00：00：00
**2. GC解析**

|title|metricValue|Note|Options
| ----------- | ----------- |----------- |----------- |
|name|G1 Young Generation|垃圾回收器的名称|"G1 Young Generation"、"G1 Old Generation"、"PS MarkSweep" 
|collectionCount|2|垃圾回收次数|累加
|collectionTime|1000|总回收时间（Mills）|累加

**3. 内存解析，支持解析堆内内存（Eden、Old、Survivor）和堆外内存**

|title|metricValue|Note|Options
| ----------- | ----------- |----------- |----------- |
|name|G1 Eden Space|内存池名称|Eden、Survivor..
|type|Heap memory|堆类型|堆、非堆
|usageCommitted|35651584|向操作系统申请资源|-
|usageMax|-1|最大容量|-
|usageUsed|-1|已使用容量|-
|peakUsageCommitted|24117248|峰值向操作系统申请资源|-
|peakUsageMax|35651584|峰值最大已使用|-
|peakUsageUsed|24117248|峰值已使用|-

**4. JVM缓冲池**

|title|metricValue|Note|Options
| ----------- | ----------- |----------- |----------- |
|totalCapacity|0|缓冲区总容量
|name|mapped|缓冲区类型|dirct、map
|count|0|缓冲区数量
|memoryUsed|0|已使用内存
### 🚀 Method可持续时间分析解析器

|title|metricValue|Note|Options
| ----------- | ----------- |----------- |----------- |
|metricName|duration.max||max,min,sum,count|
|className|UserserviceImpl|所监控的类名|
|methodName|getUser|监控的方法|
|metricValue|2814|运行时间|
|epochMillis|1722749944630|检测时间

### 🚀 StackTrace栈追踪解析器

栈追踪解析器使用JMX收集JVM运行过程中的线程信息，通过封装的threadInfo来收集栈使用信息，不限但包括main、Attach Listener、Signal Dispatcher
|title|metricValue|Note|Options
| ----------- | ----------- |----------- |----------- |
| threadName | main |
| stacktrace | list | 栈追踪链路显示 | list|
| threadState | RUNNABLE | 线程状态 | 就绪、执行、阻塞、停止|
| startEpoch | 1722749894625 | 开始收集时间
| endEpoch | 1722749899630 | 收集结束时间


### 🚀 IO解析器

IO解析器主要监控Linux系统中的`/proc/self/io`文件，收集当前程序运行过程中的IO指标，如下

|title|metricValue|Note|Options
| ----------- | ----------- |----------- |----------- |
| rchar |  | 读字符数 | - |
| wchar |  | 写字符数 | - |
| read_bytes |  | 读字节数 | - |
| write_bytes |  | 写字节数 | - |

### 🚀 JVM线程解析器
 JVM线程解析器，监控程序执行过程中总线程数、峰值线程数、实时/活动线程数和新线程数。

|title|metricValue|Note|Options
| ----------- | ----------- |----------- |----------- |
| totalStartedThreadCount |  | 总线程数 | - |
| newThreadCount |  | 新线程数 | - |
| liveThreadCount |  | 活跃线程数 | - |
| peakThreadCount |  | 峰值线程数 | - |

## 怎么运行
使用`mvn clean package`将项目进行打包，比如我们手写了一个
demo方法位于com.jvminsight.jvmprofiler.demo.RunApplication
请使用一下命令来监控该程序在运行期间的各种指标
```java
java -javaagent:target/jvm-insight-1.0-SNAPSHOT.jar=reporter=com.jvminsight.jvmprofiler.reporter.ConsoleOutputReporter,tag=mytag,metricInterval=5000,durationProfiling=com.jvminsight.jvmprofiler.demo.RunApplication.publicSleepMethod,argumentProfiling=com.jvminsight.jvmprofiler.demo.RunApplication.publicSleepMethod.1,sampleInterval=100 -cp target/jvm-insight-1.0-SNAPSHOT.jar com.jvminsight.jvmprofiler.demo.RunApplication
```
收集得到指标如下
```json
{
    "heapMemoryMax": 2109734912,
    "nonHeapMemoryTotalUsed": 8130568,
    "bufferPools": [
        {
            "totalCapacity": 0,
            "name": "mapped",
            "count": 0,
            "memoryUsed": 0
        },
        {
            "totalCapacity": 0,
            "name": "direct",
            "count": 0,
            "memoryUsed": 0
        },
        {
            "totalCapacity": 0,
            "name": "mapped - \u0027non-volatile memory\u0027",
            "count": 0,
            "memoryUsed": 0
        }
    ],
    "heapMemoryTotalUsed": 19922944,
    "epochMillis": 1723376934076,
    "nonHeapMemoryCommitted": 13238272,
    "heapMemoryCommitted": 132120576,
    "memoryPools": [
        {
            "peakUsageMax": 5898240,
            "usageMax": 5898240,
            "peakUsageUsed": 1202816,
            "name": "CodeHeap \u0027non-nmethods\u0027",
            "peakUsageCommitted": 2555904,
            "usageUsed": 1186560,
            "type": "Non-heap memory",
            "usageCommitted": 2555904
        },
        {
            "peakUsageMax": -1,
            "usageMax": -1,
            "peakUsageUsed": 4699696,
            "name": "Metaspace",
            "peakUsageCommitted": 4915200,
            "usageUsed": 4699696,
            "type": "Non-heap memory",
            "usageCommitted": 4915200
        },
        {
            "peakUsageMax": 122880000,
            "usageMax": 122880000,
            "peakUsageUsed": 1429248,
            "name": "CodeHeap \u0027profiled nmethods\u0027",
            "peakUsageCommitted": 2555904,
            "usageUsed": 1429248,
            "type": "Non-heap memory",
            "usageCommitted": 2555904
        },
        {
            "peakUsageMax": 1073741824,
            "usageMax": 1073741824,
            "peakUsageUsed": 523608,
            "name": "Compressed Class Space",
            "peakUsageCommitted": 655360,
            "usageUsed": 523608,
            "type": "Non-heap memory",
            "usageCommitted": 655360
        },
        {
            "peakUsageMax": -1,
            "usageMax": -1,
            "peakUsageUsed": 11534336,
            "name": "G1 Eden Space",
            "peakUsageCommitted": 26214400,
            "usageUsed": 11534336,
            "type": "Heap memory",
            "usageCommitted": 26214400
        },
        {
            "peakUsageMax": 2109734912,
            "usageMax": 2109734912,
            "peakUsageUsed": 8388608,
            "name": "G1 Old Gen",
            "peakUsageCommitted": 105906176,
            "usageUsed": 8388608,
            "type": "Heap memory",
            "usageCommitted": 105906176
        },
        {
            "peakUsageMax": -1,
            "usageMax": -1,
            "peakUsageUsed": 0,
            "name": "G1 Survivor Space",
            "peakUsageCommitted": 0,
            "usageUsed": 0,
            "type": "Heap memory",
            "usageCommitted": 0
        },
        {
            "peakUsageMax": 122880000,
            "usageMax": 122880000,
            "peakUsageUsed": 291456,
            "name": "CodeHeap \u0027non-profiled nmethods\u0027",
            "peakUsageCommitted": 2555904,
            "usageUsed": 291456,
            "type": "Non-heap memory",
            "usageCommitted": 2555904
        }
    ],
    "processCpuLoad": 0.08335573935607224,
    "systemCpuLoad": 0.1235764904981842,
    "processCpuTime": 328125000,
    "processUuid": "abb532fb-68ef-4af4-9ef2-fe811fa34c6b",
    "nonHeapMemoryMax": -1,
    "tag": "mytag",
    "gc": [
        {
            "collectionTime": 0,
            "name": "G1 Young Generation",
            "collectionCount": 0
        },
        {
            "collectionTime": 0,
            "name": "G1 Old Generation",
            "collectionCount": 0
        }
    ]
}
```

##🔬 智能JVM调优建议
对于新手，可能当程序不停的出现各种GC、CPU|内存飙升的现象而手足无措，
我们引入智谱大模型通过**微调技术实现对JVM调优的建议以及你当前程序可能存在的问题。
### 📕我应该怎么引入智能化？
1. 修改application.yml的ai.apikey为你自己的
2. 调用aiManager.doSyncRequest() 方法

我们发送如上指令到大模型中进行分析，得到优化意见如下

```json
[
  {
    "problems": [
      {
        "description": "CPU 使用率较低，可能存在未充分利用的情况",
        "key": "（1）"
      },
      {
        "description": "堆内存使用率偏低，可能分配过大",
        "key": "（2）"
      },
      {
        "description": "年轻代和年老代 GC 活动数据为零，可能没有进行垃圾回收",
        "key": "（3）"
      }
    ],
    "suggestions": [
      {
        "description": "调整堆内存大小以匹配实际需求",
        "key": "（1）"
      },
      {
        "description": "检查应用程序是否存在长时间运行的同步操作，导致 CPU 未充分利用",
        "key": "（2）"
      },
      {
        "description": "监控并分析 GC 日志，以确定 GC 策略是否需要调整",
        "key": "（3）"
      }
    ]
  }
]
```
## 🤗 email
如果你有任何问题，请尽管联系我 xingtang@csu.edu.cn
