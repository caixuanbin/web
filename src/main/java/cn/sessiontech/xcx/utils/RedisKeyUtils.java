package cn.sessiontech.xcx.utils;

import cn.sessiontech.xcx.enums.RedisEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xbcai
 * 操作redis 客户端工具
 */
@Component
public  class RedisKeyUtils {
	private static final Long RELEASE_SUCCESS = 1L;
	private static final String LOCK_SUCCESS = "OK";
	private static final String SET_IF_NOT_EXIST = "NX";
	private static final String SET_WITH_EXPIRE_TIME = "PX";


	@Autowired
	private JedisPool jedisPool;

	/**
	 * 模糊查找
	 *
	 * @param key
	 * @return
	 */
	public Set<String> keys(String key) {
		Jedis jedis = jedisPool.getResource();
		Set<String> result = jedis.keys(key + "*");
		jedis.close();
		return result;
	}

	/**
	 * 批量删除
	 *
	 * @param keys
	 */
	public void delBatch(Set<String> keys) {
		Jedis jedis = jedisPool.getResource();
		java.util.Iterator<String> its = keys.iterator();
		while (its.hasNext()) {
			jedis.del(its.next());
		}
		jedis.close();
	}

	public String set(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.set(key, value);
		jedis.close();
		return result;
	}

	public String set(byte[] key, byte[] value) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.set(key, value);
		jedis.close();
		return result;
	}

	public String get(String key) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.get(key);
		jedis.close();

		return result;
	}

	public byte[] get(byte[] key) {
		Jedis jedis = jedisPool.getResource();
		byte[] result = jedis.get(key);
		jedis.close();

		return result;
	}

	public Long del(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.del(key);
		jedis.close();

		return result;
	}

	public Long del(byte[] key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.del(key);
		jedis.close();

		return result;
	}

	public Long del(String... keys) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.del(keys);
		jedis.close();

		return result;
	}

	public Long del(byte[]... keys) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.del(keys);
		jedis.close();

		return result;
	}

	public Boolean exists(String key) {
		Jedis jedis = jedisPool.getResource();
		Boolean result = jedis.exists(key);
		jedis.close();

		return result;
	}

	/**
	 * 设置过期时间
	 *
	 * @param key
	 * @param seconds
	 * @param value
	 * @return
	 */
	public String setex(String key, int seconds, String value) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.setex(key, seconds, value);
		jedis.close();
		return result;
	}

	public Long expire(String key, int seconds) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.expire(key, seconds);
		jedis.close();

		return result;
	}

	public Long expire(byte[] key, int seconds) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.expire(key, seconds);
		jedis.close();

		return result;
	}

	public Long ttl(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.ttl(key);
		jedis.close();

		return result;
	}

	/**
	 * 根据指定的key 递增1并返回递增后的结果；
	 *
	 * @param key
	 * @return
	 */
	public Long incr(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.incr(key);
		jedis.close();

		return result;
	}

	/**
	 * incrby根据指定值做递增或递减操作并返回递增或递减后的结果(incrby递增或递减取决于传入值的正负)；
	 *
	 * @param key
	 * @return
	 */
	public Long incrBy(String key, Integer value) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.incrBy(key, value);
		jedis.close();
		return result;
	}

	/**
	 * 根据传入的key递减1并返回递减后的结果；
	 *
	 * @param key
	 * @return
	 */
	public Long decr(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.decr(key);
		jedis.close();
		return result;
	}

	/**
	 * decrby根据指定值做递增或递减操作并返回递增或递减后的结果(decrby递增或递减取决于传入值的正负)；
	 *
	 * @param key
	 * @return
	 */
	public Long decrBy(String key, Integer value) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.decrBy(key, value);
		jedis.close();
		return result;
	}

	public Long hset(String key, String field, String value) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.hset(key, field, value);
		jedis.close();

		return result;
	}

	public String hget(String key, String field) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.hget(key, field);
		jedis.close();

		return result;
	}

	public Map<String, String> hgetAll(String key) {
		Jedis jedis = jedisPool.getResource();
		Map<String, String> result = jedis.hgetAll(key);
		jedis.close();

		return result;
	}

	public Long hdel(String key, String... fields) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.hdel(key, fields);
		jedis.close();

		return result;
	}

	public String hmset(String key, Map<String, String> map) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.hmset(key, map);
		jedis.close();
		return result;
	}

	public List<String> hmget(String key, String... fields) {
		Jedis jedis = jedisPool.getResource();
		List<String> result = jedis.hmget(key, fields);
		jedis.close();
		return result;
	}

	/**
	 * 设置List集合
	 *
	 * @param key
	 * @param list
	 */
	public void setList(String key, List<?> list) {
		Jedis jedis = jedisPool.getResource();
		try {
			if (list == null || list.size() == 0) {
				jedis.set(key.getBytes(), "".getBytes());
			} else {//如果list为空,则设置一个空
				jedis.set(key.getBytes(), SerializeUtil.serializeList(list));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
	}

	/**
	 * 获取List集合
	 *
	 * @param key
	 * @return
	 */
	public List<?> getList(String key) {
		Jedis jedis = jedisPool.getResource();
		if (jedis == null || !jedis.exists(key)) {
			return null;
		}
		byte[] data = jedis.get(key.getBytes());
		jedis.close();
		return SerializeUtil.unserializeList(data);
	}

	/**
	 * 尝试获取分布式锁
	 *
	 * @param lockKey    锁
	 * @param requestId  客户端标识
	 * @param expireTime 超期时间
	 * @return 是否获取成功
	 */
	public boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
		jedis.close();
		if (LOCK_SUCCESS.equals(result)) {
			return true;
		}
		return false;
	}


	/**
	 * 释放分布式锁
	 *
	 * @param lockKey   锁
	 * @param requestId 请求标识
	 * @return 是否释放成功
	 */
	public boolean releaseDistributedLock(String lockKey, String requestId) {
		Jedis jedis = jedisPool.getResource();
		String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
		Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
		jedis.close();
		if (RELEASE_SUCCESS.equals(result)) {
			return true;
		}
		return false;

	}


	public String getVerifyCodeKey(String type) {
		if (type.equals(RedisEnum.LOGIN_SMS_VERIFY_CODE.getCode())) {
			return RedisEnum.LOGIN_SMS_VERIFY_CODE.getCode();
		} else if (type.equals(RedisEnum.FORGET_PASSWD_SMS_VERIFY_CODE.getCode())) {
			return RedisEnum.FORGET_PASSWD_SMS_VERIFY_CODE.getCode();
		}
		return "";
	}

}
