package com.google.code.message;

import redis.clients.jedis.Jedis;  

//订阅消息  
public class Subscriber {  
  public void psub(final Jedis redisClient, final RedisPubSubListener listener) {  
      new Thread(new Runnable() {  
          @Override  
          public void run() {  
              System.out.println("订阅：log.debug");  
              // 订阅得到信息在lister的onMessage(...)方法中进行处理  
              // 订阅多个频道  
              // redisClient.subscribe(listener, "log.debug", "log.info");  
              // redisClient.subscribe(listener, new String[]{"log.debug","log.info"});  
              redisClient.psubscribe(listener, new String[] { "log.*" });//使用模式匹配的方式设置频道  
          }  
      }).start();  
  }  
}  