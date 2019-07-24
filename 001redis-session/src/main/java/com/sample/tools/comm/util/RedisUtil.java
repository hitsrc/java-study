package com.sample.tools.comm.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.Serializable;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.SerializationUtils;

/**
 * Redis客户端工具类
 * @author swq
 * 
 */
public class RedisUtil {

	private static JedisPool pool = null;

    /** 
     * redis过期时间,以秒为单位
     */  
    public final static int EXRP_MINUTES = 60;           //一分钟
    public final static int EXRP_HOUR = 3600;            //一小时
    public final static int EXRP_DAY = 3600 * 24;        //一天
    public final static int EXRP_MONTH = 3600 * 24 * 30; //一个月
    
    static {
    	if(pool == null) {
	    	Properties properties = PropertiesUtil.getPropertiesByFileName("redis.properties");
	
	        String host = properties.getProperty("host");
	        int port = Integer.valueOf(properties.getProperty("port"));
	        String password = properties.getProperty("password");
	        int maxIdle = Integer.valueOf(properties.getProperty("maxIdle"));
	        long maxWaitMillis = Long.valueOf(properties.getProperty("maxWaitMillis"));
	        int maxTotal = Integer.valueOf(properties.getProperty("maxTotal"));
	        boolean testOnBorrow = Boolean.valueOf(properties.getProperty("testOnBorrow"));
	        
	        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
	        jedisPoolConfig.setMaxTotal(maxTotal);
	        jedisPoolConfig.setMaxIdle(maxIdle);
	        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
	        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
	        if (password != null && !"".equals(password)) {
	            // redis 设置了密码
	            pool = new JedisPool(jedisPoolConfig, host, port, 10000, password);
	        } else {
	            // redis 未设置密码
	            pool = new JedisPool(jedisPoolConfig, host, port, 10000);
	        }
    	}
    }


    private static Jedis getJedis() {
		// 从当前线程中获取Jedis
		Jedis jedis = pool.getResource();
		return jedis;
    }
    
    public static boolean checkConnection() {
    	boolean connected = false;
    	try(Jedis jedis = getJedis()){
    		connected = jedis.isConnected();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	return connected;
    }
    
    public static Long expire(String key, int seconds) {
        Long time = -1L;
    	try(Jedis jedis = getJedis()){
    		time = jedis.expire(key, seconds);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	return time;
    }

    /**
     * 返回满足pattern表达式的所有key
     * keys(*)
     * 返回所有的key
     *
     * @param pattern
     * @return
     */
    public static Set<String> keys(String pattern) {
    	Set<String> keySet = null;
    	try(Jedis jedis = getJedis()){
    		keySet = jedis.keys(pattern);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
        return keySet;
    }

    /**
     * 获取指定key的值,如果key不存在返回null，如果该Key存储的不是字符串，会抛出一个错误
     *
     * @param key
     * @return
     */
    public static String get(String key) {
    	String value = null;
    	try(Jedis jedis = getJedis()){
    		value = jedis.get(key);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
        return value;
    }
    
    /**
     * 获取指定key-field的值,如果key不存在返回null，如果该Key存储的不是字符串，会抛出一个错误
     *
     * @param key
     * @return
     */
    public static Object get(String key, String field) {
    	Object value = null;
    	try(Jedis jedis = getJedis()){
    		byte[] bytes = jedis.hget(key.getBytes(), field.getBytes());
            if (bytes != null){
            	value = SerializationUtils.deserialize(bytes);
            }
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
        return value;
    }
    
    /**
     * **** 用于获取序列化对象
     * 获取指定key的值,如果key不存在返回null，如果该Key存储的不是字符串，会抛出一个错误
     *
     * @param key
     * @return
     */
    public static byte[] get(byte[] key) {
    	byte[] value = null;
    	try(Jedis jedis = getJedis()){
    		value = jedis.get(key);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
        return value;
    }
    
    /**
     * 设置key的值为value
     * key已经存在会覆盖相应的值
     *
     * @param key
     * @param value
     * @return
     */
    public static String set(String key, String value) {
    	String result = null;
        
        try(Jedis jedis = getJedis()){
        	result = jedis.set(key, value);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
        return result;
    }
    
    /**
     * 设置key value并指定这个键值的有效期
     * key已经存在会覆盖相应的值
     *
     * @param key
     * @param seconds
     * @param value
     * @return
     */
    public static String setex(String key, int seconds, String value) {
        String result = null;
        try(Jedis jedis = getJedis()){
        	result = jedis.setex(key, seconds, value);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
        return result;
    }
    
    /**
     * **** 用于保存序列化对象
     * 设置key value并指定这个键值的有效期
     * key已经存在会覆盖相应的值
     *
     * @param key
     * @param seconds
     * @param value
     * @return
     */
    public static String setex(byte[] key, long seconds, byte[] value) {
        String result = null;
        try(Jedis jedis = getJedis()){
        	result = jedis.psetex(key, seconds, value);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
        return result;
    }
    
    public static Long hset(String key, String field, Serializable object) {
    	Long result = null;
    	try(Jedis jedis = getJedis()){
    		result = jedis.hset(key.getBytes(), field.getBytes(), SerializationUtils.serialize(object));
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
        return result;
    }

    /**
     * 删除指定的key,也可以传入一个包含key的数组
     *
     * @param keys
     * @return
     */
    public static Long del(String... keys) {
    	Long result = null;
        try(Jedis jedis = getJedis()){
        	result = jedis.del(keys);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
        
        return result;
    }
    
    /**
     * 删除指定的key - fields,也可以传入一个包含key的数组
     *
     * @param keys
     * @return
     */
    public static Long hdel(String key, String... fields) {
        Long result = null;
        try(Jedis jedis = getJedis()){
        	result = jedis.hdel(key, fields);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
        
        return result;
    }

    /**
     * 通过key向指定的value值追加值
     *
     * @param key
     * @param str
     * @return
     */
    public static Long append(String key, String str) {
        Long result = null;
        try(Jedis jedis = getJedis()){
        	result = jedis.append(key, str);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
        
        return result;
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    public static Boolean exists(String key) {
    	Boolean result = null;
        try(Jedis jedis = getJedis()){
        	result = jedis.exists(key);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
        return result;
    }

    /**
     * 通过key判断值的类型
     *
     * @param key
     * @return
     */
    public static String type(String key) {
        String result = null;
        try(Jedis jedis = getJedis()){
        	result = jedis.type(key);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
        return result;
    }

    

    public static void main(String[] args) {
    	/*
    	long time1 = System.currentTimeMillis();
    	for(int i=0;i< 10;i++) {
    		//Long status = RedisUtil.getInstance().del("key"+i);
        	
        	String status = RedisUtil.set("key"+i, "val_"+i);
        	
        	System.out.println("set [" + i +"]:" + status);
        	
        	String re = RedisUtil.type("aaaaaaaaa");
        	System.out.println("re [" + re +"]");
    	}
    	long time2 = System.currentTimeMillis();
    	
    	System.out.println(time2 - time1);
    	
    	//RedisUtil.getInstance().setAutoClose(false);
    	//获取所有
    	Set<String> keys = RedisUtil.keys("*");
    	
    	System.out.println(keys);
    	
    	*/
    	
    	
    	//设置键值
    	String status = RedisUtil.set("zhangsan", "张三1122334");
    	System.out.println("set status:" + status);//OK
    	String status2 = RedisUtil.setex("zhangsan", 60, "张三1122334");
    	System.out.println("set status:" + status2);//OK
    	
    	//获取指定key的值
    	String zhangsan = RedisUtil.get("zhangsan");
    	System.out.println(zhangsan);
    	
    	//删除指定key 可以是多个
    	//RedisUtil.del("zhangsan");
    	//RedisUtil.del("zhangsan","a");
    	//RedisUtil.del(new String[] {"zhangsan","a"});
    	
    	String jsonstr = RedisUtil.get("1000001");
    	
    	
	}
}