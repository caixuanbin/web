package cn.sessiontech.xcx.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class GenerateUtils {

	//生成主键 UUID形式
	public static String generateUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 生成带有时间的唯一流水号
	 * @return
	 */
	public static String generateReqSerialNumber(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String number = sdf.format(new Date())+new Random().nextInt(10000);
		return number;
	}

	
}
