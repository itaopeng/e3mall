package cn.e3mall.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkerTest {

	@Test
	public void testFreeMarker() throws Exception{
		//1、创建一个模板文件
		//2、创建一个Configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		//3、设置模板文件保存的目录
		configuration.setDirectoryForTemplateLoading(new File("C:/Users/Administrator/git/e3mall/e3-item-web/src/main/webapp/WEB-INF/ftl"));
		//4、模板文件的编码格式，一般utf-8
		configuration.setDefaultEncoding("utf-8");
		//5、加载模板文件，创建模板对象
		Template template = configuration.getTemplate("hello.ftl");
		//6、创建一个数据集 pojo 或map 一般map
		Map data = new HashMap<>();
		data.put("hello", "hello freemarker!");
		//7、创建writer对象，指定输出文件的路径及文件名。
		Writer out = new FileWriter("D:/temp/freemarker/hello.text");
		//8、生成静态页面
		template.process(data, out);
		//9、关闭流
		out.close();
	}
}
