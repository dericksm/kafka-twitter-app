package com.github.dericksm.kafka;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import com.github.dericksm.kafka.storm.WordCounterBolt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

import java.util.Arrays;

@SpringBootApplication
@EnableScheduling
public class KafkaIntroApplication {


    public static void main(String[] args) {
        var app = new SpringApplication(KafkaIntroApplication.class);
        app.run(args);

        // Spout
        String localHost = "localhost";
        String zookeeperHost = "localhost:2181";
        ZkHosts zkHosts = new ZkHosts(zookeeperHost);

        SpoutConfig kafkaConfig = new SpoutConfig(zkHosts, "tweets-topic", "", "storm");
        KafkaSpout kafkaSpout = new KafkaSpout(kafkaConfig);
        kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme(){
            @Override
            public Fields getOutputFields() {
                return new Fields("tweet");
            }
        });

        // Topology
        Config config = new Config();
        config.setDebug(false);

        config.setMaxTaskParallelism(5);
        config.put(Config.NIMBUS_HOST, localHost);
        config.put(Config.NIMBUS_THRIFT_PORT, 63197);
        config.put(Config.STORM_ZOOKEEPER_PORT, 2181);
        config.put(Config.STORM_ZOOKEEPER_SERVERS, Arrays.asList(localHost));

        TopologyBuilder b = new TopologyBuilder();
        b.setSpout("twitter-spout", kafkaSpout);
        b.setBolt("WordCounterBolt", new WordCounterBolt());

        try {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("WordCountStorm", config, b.createTopology());
        } catch (Exception e) {
            throw new IllegalStateException("Couldn't initialize the topology", e);
        }
    }


}
