package com.fh.util;

import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class RedisUtil {


    private static JedisPool jedisPool;

    private RedisUtil(){

    }

    static {

        //创建redis的配置
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(20);
        jedisPoolConfig.setMaxIdle(5);
        jedisPoolConfig.setMinIdle(2);
        jedisPoolConfig.setMaxWaitMillis(30000);
        jedisPool = new JedisPool("192.168.204.136",6379);

    }

    //从池中拿连接
    public static Jedis getJedis(){
        return jedisPool.getResource();
    }

    //链接用完还给池中
    public static void returnJedis(Jedis jedis){
        jedisPool.returnResource(jedis);
    }


    public static void setRedis(String key,String value){
        Jedis jedis = getJedis();
        jedis.set(key,value);
        returnJedis(jedis);
    }

    public static String getRedis(String key){
        Jedis jedis = getJedis();
        String value = jedis.get(key);
        returnJedis(jedis);
        return value;
    }

    public static String getexRedis(String key){
        Jedis jedis = getJedis();
        String value = jedis.get(key);
        returnJedis(jedis);
        return value;
    }

    public static void setexRedis(String key,int seconds,String value){
        Jedis jedis = getJedis();
        jedis.setex(key,seconds,value);
        returnJedis(jedis);
    }

    public static String hgetRedis(String key,String filed){
        Jedis jedis = getJedis();
        String value = jedis.hget(key, filed);
        returnJedis(jedis);
        return value;
    }

    public static void hsetRedis(String key,String filed,Object value){
        String values = JSONObject.toJSONString(value);
        Jedis jedis = getJedis();
        jedis.hset(key,filed,values);
        returnJedis(jedis);
    }

}
