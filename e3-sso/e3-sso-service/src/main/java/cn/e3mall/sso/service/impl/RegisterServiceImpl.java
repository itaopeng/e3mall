package cn.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private TbUserMapper userMapper;
	
	@Override
	public E3Result checkDate(String param, int type) {
		//根据不同的type生成不同的查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		if(type==1){
			criteria.andUsernameEqualTo(param);
		}else if(type==2){
			criteria.andPhoneEqualTo(param);
		}else if(type==2){
			criteria.andEmailEqualTo(param);
		}else{
			return E3Result.build(400, "数据类型错误");
		}
		//执行查询
		List<TbUser> list = userMapper.selectByExample(example);
		//判断结果中是否包含数据
		if(list!=null && list.size()>0){
			//如果有返回false
			return E3Result.ok(false);
		}
		//如果没有返回true
		return E3Result.ok(true);
	}

	@Override
	public E3Result Register(TbUser tbUser) {
		//数据有限性校验
		if(StringUtils.isBlank(tbUser.getUsername()) || StringUtils.isBlank(tbUser.getPassword()) 
				|| StringUtils.isBlank(tbUser.getPhone())){
			return E3Result.build(400, "用户数据不完整，注册失败。");
		}
		// 1、用户名 2、手机号 3、电话
		E3Result e3Result = checkDate(tbUser.getUsername(), 1);
		if(!(boolean) e3Result.getData()){
			return E3Result.build(400, "用户名被占用");
		}
		E3Result e3Result2 = checkDate(tbUser.getPhone(), 1);
		if(!(boolean) e3Result2.getData()){
			return E3Result.build(400, "用户名被占用");
		}
		//补全pojo
		tbUser.setCreated(new Date());
		tbUser.setUpdated(new Date());
		//对password 进行MD5加密
		String md5Pass = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
		tbUser.setPassword(md5Pass);
		userMapper.insert(tbUser);
		return E3Result.ok();
	}
}
