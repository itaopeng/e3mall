package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;

public interface RegisterService {

	E3Result checkDate(String param,int type);
	E3Result Register(TbUser tbUser);
}
