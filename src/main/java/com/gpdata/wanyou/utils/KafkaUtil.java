//package com.gpdata.wanyou.utils;
//
//import kafka.consumer.Consumer;
//import kafka.consumer.ConsumerConfig;
//import kafka.consumer.ConsumerIterator;
//import kafka.consumer.KafkaStream;
//import kafka.javaapi.consumer.ConsumerConnector;
//import kafka.message.MessageAndMetadata;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.Producer;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.UUID;
//import com.alibaba.fastjson.JSONObject;
//import com.gpdata.wanyou.dq.service.ValidateRangeRecordService;
//
///**
// * kafka工具类，主要实现生产及消费
// *
// * @author gaosong 2016-11-15
// */
//// TODO: 先注释这里,
//@Component
//public class KafkaUtil {
//    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaUtil.class);
//    @Autowired
//    private ValidateRangeRecordService validateRangeRecordService;
//
//    // 生产者对象
//    private static Producer<String, String> producer;
//    // 消费者对象
//    private static KafkaStream<byte[], byte[]> kafkaStream;
//
//    private KafkaUtil() {
//        // 实例化消费者对象
//        Properties props = new Properties();
//        props.put("zookeeper.connect", ConfigUtil.getConfig("zookeeper.connect"));
//        props.put("zookeeper.connection.timeout.ms", "6000");
//        props.put("group.id", ConfigUtil.getConfig("orginal.group.id"));
//        props.put("auto.commit.enable", "true");
//        props.put("auto.commit.interval.ms", "1000");
//        // 创建消费者连接
//        ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
//        // map：key 对象消费的topic的名称，value 是几个线程去消费。
//        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
//        topicCountMap.put(ConfigUtil.getConfig("orginal.topic"), 3);
//
//        // createMessageStreams key 指的是传入的topicCountMap的key 消费者的topic名称。
//        // value：是一个list
//        // KafkaStream<byte[], byte[]> 元素：key和value均为byte[]的消息流对象
//        // 分别代表消息的key和消费的message
//        // list元素个数和topicCountMap 的value数据一致即：几个线程去消费
//        Map<String, List<KafkaStream<byte[], byte[]>>> createMessageStreams = consumerConnector
//                .createMessageStreams(topicCountMap);
//        List<KafkaStream<byte[], byte[]>> list = createMessageStreams.get(ConfigUtil.getConfig("orginal.topic"));
//
//        for (KafkaStream<byte[], byte[]> kafkaStream : list) {
//            // 多线程类启动
//            new Thread(new ConsumerData(kafkaStream)).start();
//        }
//    }
//
//    // 实例化生产者对象
//    static {
//        Properties props = new Properties();
//        props.put("bootstrap.servers", ConfigUtil.getConfig("bootstrap.servers"));
//        props.put("acks", "all");
//        props.put("retries", 0);
//        props.put("batch.size", 16384);
//        props.put("linger.ms", 1);
//        props.put("buffer.memory", 33554432);
//        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        producer = new KafkaProducer<>(props);
//    }
//
//    /**
//     * 生产原始数据
//     *
//     * @return 是否成功
//     */
//    public static boolean produce(String key, String message) {
//        try {
//            LOGGER.debug("kafka生产原始数据 : key={}, message={}", key, message);
//            producer.send(new ProducerRecord<String, String>(ConfigUtil.getConfig("orginal.topic"), key, message));
//            return true;
//        } catch (Exception ex) {
//            LOGGER.error("kafka生产消息出错，key={} ：", key, ex);
//            return false;
//        } finally {
//            // producer.close();
//        }
//    }
//
//    /**
//     * 生产已经数据质量处理过的数据
//     *
//     * @param message
//     * @return
//     */
//    public static boolean produceDealed(String message) {
//        try {
//            String key = UUID.randomUUID().toString();
//            LOGGER.debug("kafka生产已经数据质量处理过的数据 : message={}", message);
//            producer.send(new ProducerRecord<String, String>(ConfigUtil.getConfig("dealed.topic"), key, message));
//            return true;
//        } catch (Exception ex) {
//            LOGGER.error("kafka生产已经数据质量处理过的数据出错，value={} ：", message, ex);
//            return false;
//        } finally {
//            // producer.close();
//        }
//    }
//
//    // 多线程消费类
//    class ConsumerData implements Runnable {
//
//        private KafkaStream<byte[], byte[]> kafkaStream;
//
//        public ConsumerData(KafkaStream<byte[], byte[]> _kafkaStream) {
//            // TODO Auto-generated constructor stub
//            this.kafkaStream = _kafkaStream;
//        }
//
//        String hdfs = ConfigUtil.getConfig("HDFS.url");
//
//        @Override
//        public void run() {
//            ConsumerIterator<byte[], byte[]> iterator = kafkaStream.iterator();
//            // 不写多线程类此处会阻塞
//            while (iterator.hasNext()) {
//                MessageAndMetadata<byte[], byte[]> next = iterator.next();
//                System.out.println(String.format("key :%s, message:%s ,offset:%s ,partition:%s",
//                        next.key() == null ? null : new String(next.key()), new String(next.message()), next.offset(),
//                        next.partition()));
//                try {
//                    String key = next.key() == null ? null : new String(next.key());
//                    List<List> messageList = new String(next.message()) == null ? null
//                            : JSONObject.parseArray(new String(next.message()), List.class);
//                    if (key != null && messageList.size() > 1) {
//                        Integer datasourceId = Integer.parseInt(key.split("_")[0]);
//                        Integer tableId = Integer.parseInt(key.split("_")[1]);
//                        List<List> kafkaList = validateRangeRecordService.dataValidate(datasourceId, tableId,
//                                messageList);
//                        Map<String, Object> kafkaMap = new HashMap<>();
//                        kafkaMap.put("key", key);
//                        kafkaMap.put("data", kafkaList);
//                        String kafkaString = JSONObject.toJSONString(kafkaMap);
//                        boolean bool = produceDealed(kafkaString);
//                    }
//                } catch (Exception e) {
//                    LOGGER.error("Exception : {}" + e.toString());
//                }
//            }
//        }
//    }
//}
