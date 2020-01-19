package cn.e3mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.LoginService;

/***
 * 用户登录处理
 * <p>Title: LoginServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${SESSION_EXPIRE}") 
	private Integer SESSION_EXPIRE;
	
	@Override
	public E3Result userlogin(String username, String password) {
		// 判断用户名密码是否正确
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example);
		if(list == null || list.size()==0){
			//返回登陆失败
			return E3Result.build(400, "用户名或密码错误！");
		}
		//取用户信息
		TbUser user = list.get(0);
		// 校验密码
		if(!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))){
			// 不正确返回失败
			return E3Result.build(400, "用户名或密码错误！");
		}
		// 正确写入token
		String token = UUID.randomUUID().toString();
		user.setPassword(null);
		jedisClient.set("SESSION:"+token, JsonUtils.objectToJson(user));
		// 写入session 设置过期时间
		jedisClient.expire("SESSION:"+token, SESSION_EXPIRE);
		// 把token返回
		return E3Result.ok(token);
	}

}
