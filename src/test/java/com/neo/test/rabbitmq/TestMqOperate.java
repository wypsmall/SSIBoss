package com.neo.test.rabbitmq;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by neowyp on 2016/7/6.
 * Author   : wangyunpeng
 * Date     : 2016/7/6
 * Time     : 12:26
 * Version  : V1.0
 * Desc     :
 */
@Slf4j
public class TestMqOperate {

    //queue name
    private final static String CFG_QUEUE_NAME = "bs.risk";
    private final static String CFG_HOST_IP = "10.69.112.76";
    private final static int CFG_HOST_PORT = 5672;
    private final static String CFG_VHOST = "/bs-arch";
    private final static String CFG_USER = "bsarch";
    private final static String CFG_PWD = "bsarch";
    private final static String CFG_DIR_EXCHANGE_NAME = "msg.risk.direct";
    private final static String CFG_FAN_EXCHANGE_NAME = "msg.risk.fanout";
    private final static String CFG_TOP_EXCHANGE_NAME = "msg.risk.topic";
    private final static String CFG_ROUTING_KEY = "trade.risk";

    public static void main(String[] args) {
        //direct exchange
//        sendMsgByDirect();
//        receiveMsgByDirect();

        //fanout exchange
//        sendMsgByFanout();
//        receiveMsgByFanout();
    }

    /**
     * 建立扇出的exchange，发消息，不需要declare队列
     * 如果没有declare队列，那么在没有队列连接前发送的消息都会丢失
     * 如果declare是临时队列，那么队列消失，也会丢失消息
     */
    private static void sendMsgByFanout() {
        try {
            //创建连接连接到MabbitMQ
            Connection connection = getConnection();
            //创建一个频道
            Channel channel = connection.createChannel();
            //声明exchange
            channel.exchangeDeclare(CFG_FAN_EXCHANGE_NAME, "fanout"); //direct fanout topic
            //使用“fanout”方式发送，即广播消息，不需要使用queue，发送端不需要关心谁接收
            //所以不需要使用channel.queueDeclare、channel.queueBind方法
            //发送的消息
            String message = "fanout => hello world!";

            //由于是fanout类型，在生产者这边没有declare队列，那么basicPublish的第三个参数无论是null。还是MessageProperties.PERSISTENT_TEXT_PLAIN
            //在没有绑定队列的时候，所发出去的消息都会丢失
            //也就是说，fanout作为广播，只要连上这个exchange的队列都会收到消息
            //只要保持queue不消失，广播的消息都会收到，如果queue消失，中间发送的消息也都收不到
            //广播指向在线的queue发消息
            channel.basicPublish(CFG_FAN_EXCHANGE_NAME, "", null, message.getBytes());
            log.info("[x] Sent [{}]", message);

            //关闭频道和连接
            channel.close();
            connection.close();
        } catch (Exception e) {
            log.error("send message error, info is:", e);
        }
    }

    /**
     * 接收fanout-exchange的消息
     * 1.取决与queue的性质，是永久还是自动删除
     *  a.如果永久性的，又有消息那就接收消息
     *  b.如果是临时的，在队列声明周期内没有消息发送，那就不可能接收到消息
     * 2.消息的持久性其实不重要
     * 3.fanout可以认为是广播，这么理解fanout类型，只要你听着，有消息你就会知道，如果你不听，即使有消息，你也听不到
     */
    private static void receiveMsgByFanout() {
        try {
            //打开连接和创建频道，与发送端一样
            Connection connection = getConnection();
            Channel channel = connection.createChannel();

            //【2】
            channel.queueDeclare(CFG_QUEUE_NAME, true, false, false, null);
            channel.queueBind(CFG_QUEUE_NAME, CFG_FAN_EXCHANGE_NAME, "");

            //【1】
            //该queue的类型为non-durable、exclusive、auto-delete的，将该queue绑定到上面的exchange上接收消息
//            String queueName = channel.queueDeclare().getQueue();
//            channel.queueBind(queueName, CFG_FAN_EXCHANGE_NAME, "");

            log.info("[*] Waiting for messages. To exit press CTRL+C");
            //创建队列消费者
            QueueingConsumer consumer = new QueueingConsumer(channel);
            //【2】
            int msgCount = channel.queueDeclarePassive(CFG_QUEUE_NAME).getMessageCount();
            //【1】
//            int msgCount = channel.queueDeclarePassive(queueName).getMessageCount();
            log.info("message count is {}.", msgCount);

            //指定消费队列
            //如果autoAck=true，那么这个方法执行完后所有消息都被接收到本地
            //如果autoAck=false，channel.basicAck()人工确认接收
            //【2】
            channel.basicConsume(CFG_QUEUE_NAME, false, consumer);
            //【1】
//            channel.basicConsume(queueName, false, consumer);

            for (int i = 0; i < 2; i++) {
                //consumer.nextDelivery(); 这个方法使用BlockingQueue.take()方法，所以进程阻塞，程序无法退出
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                Envelope envelope =  delivery.getEnvelope();
                String message = new String(delivery.getBody());
                log.info("[x] Received {}-[{}]", delivery.getProperties().getCorrelationId(), message);
//                channel.basicAck(envelope.getDeliveryTag(), false);
            }
            //关闭频道和连接
            channel.close();
            connection.close();

        } catch (Exception e) {
            log.error("receive message error, info is:", e);
        }
    }

    /**
     * routingkey
     * exchange            -->            queue
     * msg.risk.direct trade.risk        bs.risk
     * 消息需要持久花
     */
    private static void sendMsgByDirect() {
        try {
            //创建连接连接到MabbitMQ
            Connection connection = getConnection();
            //创建一个频道
            Channel channel = connection.createChannel();
            //声明exchange
            channel.exchangeDeclare(CFG_DIR_EXCHANGE_NAME, "direct"); //direct fanout topic
            //指定一个队列 durable = true
            channel.queueDeclare(CFG_QUEUE_NAME, true, false, false, null);
            //binding routingkey
            channel.queueBind(CFG_QUEUE_NAME, CFG_DIR_EXCHANGE_NAME, CFG_ROUTING_KEY);
            //发送的消息
            String message = "hello world!";
            //往队列中发出一条消息，持久化 MessageProperties.PERSISTENT_TEXT_PLAIN
            channel.basicPublish(CFG_DIR_EXCHANGE_NAME, CFG_ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            log.info("[x] Sent [{}]", message);

            //关闭频道和连接
            channel.close();
            connection.close();
        } catch (Exception e) {
            log.error("send message error, info is:", e);
        }
    }

    /**
     * 接收消息，只接收1条记录
     */
    private static void receiveMsgByDirect() {
        try {
            //打开连接和创建频道，与发送端一样
            Connection connection = getConnection();
            Channel channel = connection.createChannel();
            //声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列。
            channel.queueDeclare(CFG_QUEUE_NAME, true, false, false, null);

            log.info("[*] Waiting for messages. To exit press CTRL+C");
            //创建队列消费者
            QueueingConsumer consumer = new QueueingConsumer(channel);
            int msgCount = channel.queueDeclarePassive(CFG_QUEUE_NAME).getMessageCount();
            log.info("message count is {}.", msgCount);

            //指定消费队列
            //如果autoAck=true，那么这个方法执行完后所有消息都被接收到本地
            //如果autoAck=false，channel.basicAck()人工确认接收
            channel.basicConsume(CFG_QUEUE_NAME, false, consumer);


            for (int i = 0; i < 2; i++) {
                //consumer.nextDelivery(); 这个方法使用BlockingQueue.take()方法，所以进程阻塞，程序无法退出
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                Envelope envelope =  delivery.getEnvelope();
                String message = new String(delivery.getBody());
                log.info("[x] Received {}-[{}]", delivery.getProperties().getCorrelationId(), message);
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
            //关闭频道和连接
            channel.close();
            connection.close();

        } catch (Exception e) {
            log.error("receive message error, info is:", e);
        }
    }

    private static Connection getConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        //设置MabbitMQ所在主机ip或者主机名
        factory.setHost(CFG_HOST_IP);
        factory.setPort(CFG_HOST_PORT);
        factory.setVirtualHost(CFG_VHOST);
        factory.setUsername(CFG_USER);
        factory.setPassword(CFG_PWD);
        //创建一个连接
        return factory.newConnection();
    }


}
