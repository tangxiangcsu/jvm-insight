package com.jvminsight.jvmprofiler.reporter;

import cn.hutool.json.JSONUtil;
import com.jvminsight.jvmprofiler.common.ErrorCode;
import com.jvminsight.jvmprofiler.exception.JVMException;
import com.jvminsight.jvmprofiler.utils.ArgumentUtils;
import lombok.Data;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler.reporter
 * @NAME: KafKaOutputReporter
 * @USER: tangxiang
 * @DATE: 2024/7/30
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION:
 **/

@Data
public class KafKaOutputReporter implements Reporter{
    public final static String ARG_BROKER_LIST = "brokerList";
    public final static String ARG_SYNC_MODE = "syncMode";
    public final static String ARG_TOPIC_PREFIX = "topicPrefix";

    /**
     * 参数列表
     * brokerList: 参数列表
     * syncMode：同步异步方式
     * topicPrefix：主题前缀
     */
    private String brokerList = "localhost:9092";
    private boolean syncMode = false;
    private String topicPrefix;

    /**
     * KafKa生产者
     */
    private Producer<String, byte[]> producer;

    /**
     * 线程安全的主题
     */
    private ConcurrentHashMap<String, String> profilerTopics = new ConcurrentHashMap<>();

    /**
     * 更新参数列表项
     * @param parsedArgs
     */
    @Override
    public void updateArguments(Map<String, List<String>> parsedArgs) {
        String argValue = ArgumentUtils.getArgumentSingleValue(parsedArgs, ARG_BROKER_LIST);
        if (ArgumentUtils.needToUpdateArg(argValue)) {
            setBrokerList(argValue);
            System.out.println("Got argument value for brokerList: " + brokerList);
        }

        argValue = ArgumentUtils.getArgumentSingleValue(parsedArgs, ARG_SYNC_MODE);
        if (ArgumentUtils.needToUpdateArg(argValue)) {
            setSyncMode(Boolean.parseBoolean(argValue));
            System.out.println("Got argument value for syncMode: " + syncMode);
        }

        argValue = ArgumentUtils.getArgumentSingleValue(parsedArgs, ARG_TOPIC_PREFIX);
        if (ArgumentUtils.needToUpdateArg(argValue)) {
            setTopicPrefix(argValue);
            System.out.println("Got argument value for topicPrefix: " + topicPrefix);
        }

    }

    /**
     * 向KafKa中发送消息
     * @param profilerName
     * @param metrics
     */
    @Override
    public void report(String profilerName, Map<String, Object> metrics) {

        String topicName = getTopic(profilerName);

        String str = JSONUtil.toJsonStr(metrics);
        byte[] message = str.getBytes(StandardCharsets.UTF_8);
        Future<RecordMetadata> future = producer.send(new ProducerRecord<>(topicName, message));

        if(syncMode) {
            producer.flush();
            try{
                future.get();
            }catch (Exception e){
                throw new JVMException(ErrorCode.SYSTEM_ERROR, e.toString());
            }
        }
    }

    @Override
    public void close() {
        if(producer == null){
            return;
        }
        producer.flush();
        producer.close();
        producer = null;
    }

    public String getTopic(String profilerName){
        String topic = profilerTopics.getOrDefault(profilerName, null);
        if(topic == null || topic.isEmpty()){
            topic = topicPrefix == null? "":topicPrefix;
            topic += profilerName;
        }
        return topic;
    }

    private void initProducer(){
        synchronized (this) {
            if (producer != null) {
                return;
            }

            /**
             * Properties 是一个继承自HashTablede类，是一个专门为设计用于处理配置数据和属性文件
             *（1）Properties 提供了许多方便的方法来加载和存储属性
             *（2）Properties 类约定所有的键和值都是字符串类型。
             */
            Properties props = new Properties();
            props.put("bootstrap.servers", brokerList);
            props.put("retries", 10);
            props.put("batch.size", 16384);
            props.put("linger.ms", 0);
            props.put("buffer.memory", 16384000);
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            props.put("value.serializer", org.apache.kafka.common.serialization.ByteArraySerializer.class.getName());
            props.put("client.id", "jvm-profilers");

            if (syncMode) {
                props.put("acks", "all");
            }

            producer = new KafkaProducer<>(props);
        }
    }
}
