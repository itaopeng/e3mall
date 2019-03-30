package cn.e3mall.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {
	
	@Test
	public void testJedis(){
		//创建一个jedis对象，参数：host、port
		Jedis jedis = new Jedis("192.168.25.133", 6379);
		//直接使用jedis操作redis 所有jedis命令都对应一个方法
		jedis.set("test123", "my first jedis");
		System.out.println(jedis.get("test123"));
		//关闭连接
		jedis.close();
	}
	@Test
	public void testJedisPool() throws Exception{
		//创建一个连接池对象 host、port
		JedisPool jedisPool = new JedisPool("192.168.25.133", 6379);
		//从连接池获得连接 一个jedis对象
		Jedis jedis = jedisPool.getResource();
		//使用jedis操作redis
		String str = jedis.get("test123");
		System.out.println(str);
		//每次使用完毕后关闭连接、连接池回收资源
		jedis.close();
		//关闭连接池
		jedisPool.close();
	}
	
	@Test
	public void testJedisCluster() throws Exception{
		//创建一个jedisCluster 对象 有一个参数nodes是一个set对象。set中包含若干个host and port对象
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.133", 7001));
		nodes.add(new HostAndPort("192.168.25.133", 7002));
		nodes.add(new HostAndPort("192.168.25.133", 7003));
		nodes.add(new HostAndPort("192.168.25.133", 7004));
		nodes.add(new HostAndPort("192.168.25.133", 7005));
		nodes.add(new HostAndPort("192.168.25.133", 7006));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		//直接使用jedisCluster操作redis
		jedisCluster.set("test", "123");
		String str = jedisCluster.get("test");
		System.out.println(str);
		//关闭jedisCluster对象
		jedisCluster.close();
	}
}
