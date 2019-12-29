package cn.e3mall.solrj.activemq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MessageController {

	@Test
	public void msgConsumer() throws Exception{
		//初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext-activemq.xml");
		//等待
		System.in.read();
	}
}
