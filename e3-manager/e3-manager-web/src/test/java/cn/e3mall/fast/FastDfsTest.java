package cn.e3mall.fast;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import cn.e3mall.common.utils.FastDFSClient;

public class FastDfsTest {
	
		@Test
		public void testUpload() throws Exception{
			//创建一个配置文件 文件名任意 内容就是tracker服务器地址
			//使用全局对象加载配置文件
			ClientGlobal.init("D:/java/github/e3-manager/e3-manager-web/src/main/resources/conf/client.conf");
			//创建一个 TrackerClient对象
			TrackerClient trackerClient = new TrackerClient();
			//通过TrackerClient获得trackerService对象
			TrackerServer trackerServer = trackerClient.getConnection();
			//创建StorageService引用,可以是null
			StorageServer storageServer = null;
			//创建一个 StorageClient，参数需要TrackerServer和StorageServer
			StorageClient storageClient = new StorageClient(trackerServer, storageServer);
			//使用storageClient上传文件
			String[] strings = storageClient.upload_file("F:/jj/logo.jpg", "jpg", null);
			for (String string : strings) {
				System.out.println(string);
			}
		}
		
		@Test
		public void testFastDfsClient() throws Exception{
			FastDFSClient fastDFSClient = new FastDFSClient("D:/java/github/e3-manager/e3-manager-web/src/main/resources/conf/client.conf");
			String string = fastDFSClient.uploadFile("F:/gg/newsArticle_files/201705230071495516496258.jpg");
			System.out.println(string);
		}
}
