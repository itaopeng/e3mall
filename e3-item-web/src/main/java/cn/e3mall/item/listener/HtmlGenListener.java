package cn.e3mall.item.listener;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class HtmlGenListener implements MessageListener {

	@Autowired 
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Autowired
	private ItemService itemService;
	@Value("${HTML_GEN_PATH}")
	private String HTML_GEN_PATH;
	/**
	 * <p>Title: onMessage</p>
	 * <p>Description: </p>
	 * @param arg0
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	public void onMessage(Message message) {
		try {
			//创建一个模板，参考jsp
			//从消息中取商品id
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			Long itemId = new Long(text);
			//等待事物提交
			Thread.sleep(1000);
			//根据商品id查询商品信息，商品基本信息和商品描述
			TbItem tbItem = itemService.getItemById(itemId);
			Item item = new Item(tbItem);
			TbItemDesc itemDesc = itemService.getItemDescById(itemId);
			//创建一个数据集，把商品数据封装
			Map data = new HashMap();
			data.put("item", item);
			data.put("itemDesc", itemDesc);
			//加载模板对象
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			//创建一个输出流，制定输出目录及文件名
			Writer out = new FileWriter(HTML_GEN_PATH+itemId+".html");
			//生成静态页面
			template.process(data, out);
			//关闭流
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
