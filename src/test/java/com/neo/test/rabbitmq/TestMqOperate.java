package com.neo.test.rabbitmq;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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

        //topic exchange
//        sendMsgByTopic();
//        receiveMsgByTopic("bs.risk", "risk.#");
//        receiveMsgByTopic("bs.alarm", "risk.*.error");

        /**
         * 在exchange、queue以及没有通过routingkey来binding时
         * publish的消息都会丢失
         * 理论上应该现有producer建立exchange，并且指定routingkey发送消息
         * consumer负责创建queue，并指定routingkey来接收消失
         */

//        exsitJudge();

        /**
         *  首先创建一个exchange、routingkey、
         *  然后在消费端分别创建两个队列，分先后
         *  相当于一个queue已经开始消费指定routingkey的消息
         *  之后又加多一个queue还是消费这个routingkey的消息
         */
//        sendMsgByTopicMulti();
//        receiveMsgByTopicMulti("bs.risk");
//        receiveMsgByTopicMulti("bs.alarm");

        /**
         * 演示dead letter exchange
         * exchange direct CFG_DIR_EXCHANGE_NAME    msg.risk.direct
         * routingkey CFG_ROUTING_KEY  trade.risk
         * queue    bs.risk
         * x-dead-letter-exchange       msg.risk.dead
         * x-dead-letter-routing-key    dead.trade.risk
         * dead-letter queue        bs.risk.queue
         */
        new Date();
//        sendMsgUseDLX("normal message [" + new Date() + "]");
//        sendMsgUseDLX("dead message [" + new Date() + "]");
//        receiveMsgUseDLX();
        receiveMsgDeadLetter();
    }

    /**
     * if message begin with 'dead' will not process, and transfor to dead letter exchange
     *
     * @param message
     */
    private static void sendMsgUseDLX(String message) {
        try {
            //创建连接连接到MabbitMQ
            Connection connection = getConnection();
            //创建一个频道
            Channel channel = connection.createChannel();
            //声明exchange
            channel.exchangeDeclare(CFG_DIR_EXCHANGE_NAME, "direct"); //direct fanout topic
            //设置dead letter exchange and routingkey
            Map<String, Object> args = new HashMap<String, Object>();
            args.put("x-dead-letter-exchange", "msg.risk.dead");
            args.put("x-dead-letter-routing-key", "dead.trade.risk");
            //指定一个队列 durable = true
            channel.queueDeclare(CFG_QUEUE_NAME, true, false, false, args);
            //binding routingkey
            channel.queueBind(CFG_QUEUE_NAME, CFG_DIR_EXCHANGE_NAME, CFG_ROUTING_KEY);
            //发送的消息
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
     * consume message
     * if message contain "dead" reject
     */
    private static void receiveMsgUseDLX() {
        try {
            //打开连接和创建频道，与发送端一样
            Connection connection = getConnection();
            Channel channel = connection.createChannel();

            //创建队列消费者
            QueueingConsumer consumer = new QueueingConsumer(channel);

            //指定消费队列
            //如果autoAck=true，那么这个方法执行完后所有消息都被接收到本地
            //如果autoAck=false，channel.basicAck()人工确认接收
            channel.basicQos(1);
            channel.basicConsume(CFG_QUEUE_NAME, false, consumer);

            for (int i = 0; i < 10; i++) {
                //consumer.nextDelivery(); 这个方法使用BlockingQueue.take()方法，所以进程阻塞，程序无法退出
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                Envelope envelope = delivery.getEnvelope();
                String message = new String(delivery.getBody());
                log.info("[x] Received {}-[{}]", envelope, message);
                if (message.contains("dead")) {
                    channel.basicReject(envelope.getDeliveryTag(), false);
                } else {
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
            //关闭频道和连接
            channel.close();
            connection.close();

        } catch (Exception e) {
            log.error("receive message error, info is:", e);
        }
    }

    /**
     * x-dead-letter-exchange       msg.risk.dead
     * x-dead-letter-routing-key    dead.trade.risk
     * dead-letter queue        bs.risk.dead
     * dead letter config must be create pre
     * include exchange routing-key queue
     * then consume message
     */
    private static void receiveMsgDeadLetter() {
        try {
            //打开连接和创建频道，与发送端一样
            Connection connection = getConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare("msg.risk.dead", "direct"); //direct fanout topic
            //创建队列消费者
            QueueingConsumer consumer = new QueueingConsumer(channel);

            //指定消费队列
            channel.queueDeclare("bs.risk.dead", true, false, false, null);

            channel.queueBind("bs.risk.dead", "msg.risk.dead", "dead.trade.risk");
            //如果autoAck=true，那么这个方法执行完后所有消息都被接收到本地
            //如果autoAck=false，channel.basicAck()人工确认接收
            channel.basicQos(1);
            channel.basicConsume("bs.risk.dead", false, consumer);

            for (int i = 0; i < 10; i++) {
                //consumer.nextDelivery(); 这个方法使用BlockingQueue.take()方法，所以进程阻塞，程序无法退出
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                Envelope envelope = delivery.getEnvelope();
                String message = new String(delivery.getBody());
                log.info("[x] Received {}-[{}]", envelope, message);
                log.info("[x] Received Head info {}", delivery.getProperties().getHeaders());

                channel.basicAck(envelope.getDeliveryTag(), false);

            }
            //关闭频道和连接
            channel.close();
            connection.close();

        } catch (Exception e) {
            log.error("receive message error, info is:", e);
        }
    }

    /**
     * 创建exchange和routingkey
     */
    private static void sendMsgByTopicMulti() {
        try {
            //创建连接连接到MabbitMQ
            Connection connection = getConnection();
            //创建一个频道
            Channel channel = connection.createChannel();
            //声明exchange
            channel.exchangeDeclare(CFG_TOP_EXCHANGE_NAME, "topic"); //direct fanout topic

            String[] routing_keys = new String[]{
                    "risk.trade.info",
            };
            for (String routing_key : routing_keys) {
                String message = "message info [" + UUID.randomUUID().toString() + "]";
                channel.basicPublish(CFG_TOP_EXCHANGE_NAME, routing_key, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
                log.info("[x] Sent [{}]", message);
            }
            //关闭频道和连接
            channel.close();
            connection.close();
        } catch (Exception e) {
            log.error("send message error, info is:", e);
        }
    }

    /**
     * 绑定exchange和routingkey和queue，queueName根据入参决定
     * 相当与创建一个新的queue，复制一路消息到这个队列
     *
     * @param queueName
     */
    private static void receiveMsgByTopicMulti(String queueName) {
        try {
            //打开连接和创建频道，与发送端一样
            Connection connection = getConnection();
            Channel channel = connection.createChannel();
            //声明exchange
//            channel.exchangeDeclare(CFG_TOP_EXCHANGE_NAME, "topic"); //direct fanout topic
            //声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列。
            channel.queueDeclare(queueName, true, false, false, null);

            channel.queueBind(queueName, CFG_TOP_EXCHANGE_NAME, "risk.trade.info");

            log.info("[*] Waiting for messages. To exit press CTRL+C");
            //创建队列消费者
            QueueingConsumer consumer = new QueueingConsumer(channel);
            //指定消费队列
            channel.basicConsume(queueName, false, consumer);


            for (int i = 0; i < 20; i++) {
                //consumer.nextDelivery(); 这个方法使用BlockingQueue.take()方法，所以进程阻塞，程序无法退出
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                Envelope envelope = delivery.getEnvelope();
                String message = new String(delivery.getBody());
                log.info("[x] Received {} : [{}]", queueName, message);
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
            //关闭频道和连接
            channel.close();
            connection.close();

        } catch (Exception e) {
            log.error("receive message error, info is:", e);
        }
    }

    private static void exsitJudge() {
        Connection connection = null;
        Channel channel = null;
        try {
            //创建连接连接到MabbitMQ
            connection = getConnection();
            //创建一个频道
            channel = connection.createChannel();

//            AMQP.Exchange.DeclareOk exRes = channel.exchangeDeclarePassive(CFG_TOP_EXCHANGE_NAME);
//            log.info("exRes is {}", exRes);
            AMQP.Queue.DeclareOk qRes = channel.queueDeclarePassive("bs.alarm");
            log.info("qRes is {}", qRes);
            //每次获取消息数量，perfetch=1
//            channel.basicQos(1);
            //关闭频道和连接
            channel.close();
            connection.close();
        } catch (Exception e) {
            log.error("send message error, info is:", e);
        } finally {
            try {
                //关闭频道和连接
                channel.close();
//                connection.close();
            } catch (Exception e) {
                log.error("finally channel.close() exception :", e);
            }
            try {
                //关闭频道和连接
//                channel.close();
                connection.close();
            } catch (Exception e) {
                log.error("finally connection.close() exception :", e);
            }
        }
    }

    /**
     * 设计以下场景
     * //      exchange            routingkey              queue
     * //      msg.risk.topic      risk.trade.info
     * //                          risk.pay.info
     * //                          risk.trade.error
     * //                          risk.pay.error
     * //
     * //                          risk.#                  bs.risk
     * //                          risk.*.error            bs.alarm
     * 在发送message只需要指定routingkey，不需要指定queue
     * 在接收message指定routingkey与queue
     */
    private static void sendMsgByTopic() {
        try {
            //创建连接连接到MabbitMQ
            Connection connection = getConnection();
            //创建一个频道
            Channel channel = connection.createChannel();
            //声明exchange
            channel.exchangeDeclare(CFG_TOP_EXCHANGE_NAME, "topic"); //direct fanout topic

            String[] routing_keys = new String[]{
                    "risk.trade.info",
                    "risk.pay.info",
                    "risk.trade.error",
                    "risk.pay.error"};
            for (String routing_key : routing_keys) {
                String message = "message info [" + UUID.randomUUID().toString() + "]";
                channel.basicPublish(CFG_TOP_EXCHANGE_NAME, routing_key, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
                log.info("[x] Sent [{}]", message);
            }
            //关闭频道和连接
            channel.close();
            connection.close();
        } catch (Exception e) {
            log.error("send message error, info is:", e);
        }
    }

    /**
     * @param queueName  bs.risk     bs.alarm
     * @param pattarnKey risk.#      risk.*.error
     */
    private static void receiveMsgByTopic(String queueName, String pattarnKey) {
        try {
            //打开连接和创建频道，与发送端一样
            Connection connection = getConnection();
            Channel channel = connection.createChannel();
            //声明exchange
//            channel.exchangeDeclare(CFG_TOP_EXCHANGE_NAME, "topic"); //direct fanout topic
            //声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列。
            channel.queueDeclare(queueName, true, false, false, null);

            channel.queueBind(queueName, CFG_TOP_EXCHANGE_NAME, pattarnKey);

            log.info("[*] Waiting for messages. To exit press CTRL+C");
            //创建队列消费者
            QueueingConsumer consumer = new QueueingConsumer(channel);
            //指定消费队列
            channel.basicConsume(queueName, false, consumer);


            for (int i = 0; i < 20; i++) {
                //consumer.nextDelivery(); 这个方法使用BlockingQueue.take()方法，所以进程阻塞，程序无法退出
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                Envelope envelope = delivery.getEnvelope();
                String message = new String(delivery.getBody());
                log.info("[x] Received {}-{} : [{}]", queueName, pattarnKey, message);
                //long deliveryTag 一次投递的标记
                //boolean multiple true确认所有消息，包括提供的投递标记， false表示仅确认当前的投递标签的消息
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
            //关闭频道和连接
            channel.close();
            connection.close();

        } catch (Exception e) {
            log.error("receive message error, info is:", e);
        }
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
     * a.如果永久性的，又有消息那就接收消息
     * b.如果是临时的，在队列声明周期内没有消息发送，那就不可能接收到消息
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
                Envelope envelope = delivery.getEnvelope();
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
                Envelope envelope = delivery.getEnvelope();
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
