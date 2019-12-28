package cn.e3mall.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class ActiveMqTest {


	/***
	 * 点对点发送消息
	 * <p>Title: testQueueProducer</p>
	 * <p>Description: </p>
	 * @throws Exception
	 */
	
	@Test
	public void testQueueProducer() throws Exception{
		//创建一个工厂对象 需要指定服务ip和端口
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//使用工厂对象创建一个Connection
		Connection connection = factory.createConnection();
		//开启连接 调用connection 的start方法
		connection.start();
		//创建一个session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);//false(事务，一般为false); 自动发送或手动发送  
		//使用session对象  创建一个destination 对象  两种形式queue topic，现在应该用queue
		Queue queue = session.createQueue("test-queue");
		//使用session 创建producer 对象
		MessageProducer producer = session.createProducer(queue);
		// 创建message 对象 可以使用testmessage
		TextMessage textMessage = session.createTextMessage("hallo activemq");
		// 发送消息
		producer.send(textMessage);
		//关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testQueueConsumer() throws Exception{
		//创建一个工厂对象 需要指定服务ip和端口
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//使用工厂对象创建一个Connection
		Connection connection = factory.createConnection();
		//开启连接 调用connection 的start方法
		connection.start();
		//创建一个session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);//false(事务，一般为false); 自动发送或手动发送  
		//使用session对象  创建一个destination 对象  两种形式queue topic，现在应该用queue
		Queue queue = session.createQueue("spring-queue");
		//使用session 创建Consumer 对象
		MessageConsumer consumer = session.createConsumer(queue);
		// 接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				try {
					String text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		//等待
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
	
	/***
	 * 发送消息
	 * <p>Title: testQueueProducer</p>
	 * <p>Description: </p>
	 * @throws Exception
	 */
	
	@Test
	public void testTopicProducer() throws Exception{
		//创建一个工厂对象 需要指定服务ip和端口
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//使用工厂对象创建一个Connection
		Connection connection = factory.createConnection();
		//开启连接 调用connection 的start方法
		connection.start();
		//创建一个session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);//false(事务，一般为false); 自动发送或手动发送  
		//使用session对象  创建一个destination 对象  两种形式queue topic，现在应该用queue
		Topic topic = session.createTopic("test-topic");
		//使用session 创建producer 对象
		MessageProducer producer = session.createProducer(topic);
		// 创建message 对象 可以使用testmessage
		TextMessage textMessage = session.createTextMessage("topic message");
		// 发送消息
		producer.send(textMessage);
		//关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testTopicConsumer() throws Exception{
		//创建一个工厂对象 需要指定服务ip和端口
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//使用工厂对象创建一个Connection
		Connection connection = factory.createConnection();
		//开启连接 调用connection 的start方法
		connection.start();
		//创建一个session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);//false(事务，一般为false); 自动发送或手动发送  
		//使用session对象  创建一个destination 对象  两种形式queue topic，现在应该用queue
		Topic topic = session.createTopic("test-topic");
		//使用session 创建Consumer 对象
		MessageConsumer consumer = session.createConsumer(topic);
		// 接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				try {
					String text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		System.out.println("topic 消费者3已经启动");
		//等待
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
}
