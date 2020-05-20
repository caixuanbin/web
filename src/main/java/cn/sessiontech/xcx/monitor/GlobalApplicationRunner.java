package cn.sessiontech.xcx.monitor;

import cn.sessiontech.xcx.enums.WxEnum;
import cn.sessiontech.xcx.utils.RedisKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author xbcai
 * @classname GlobalApplicationRunner
 * @description 全局监听器，可以在容器初始化后做些事情
 * @date 2019/3/26 16:01
 */
@Component
@Slf4j
public class GlobalApplicationRunner implements ApplicationRunner {

	@Autowired
	private RedisKeyUtils redisKeyUtils;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		//删除全局token
		deleteToken();

	}

	/**
	 * 删除全局token
	 */
	private void deleteToken(){
		Boolean exists = redisKeyUtils.exists(WxEnum.WX_ACCESS_TOKEN.getKey());
		log.info("是否存在token:{}",exists);
		if(exists){
			redisKeyUtils.del(WxEnum.WX_ACCESS_TOKEN.getKey());
		}

	}



}