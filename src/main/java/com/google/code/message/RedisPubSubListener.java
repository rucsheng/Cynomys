package com.google.code.message;


import redis.clients.jedis.JedisPubSub;

//发布订阅 消息监听
public class RedisPubSubListener extends JedisPubSub {

	// 取得订阅的消息后的处理
	@Override
	public void onMessage(String channel, String message) {
		System.out.println("onMessage: channel[" + channel + "], message[" + message + "]");
	}

	 // 取得按表达式的方式订阅的消息后的处理  
	@Override
	public void onPMessage(String pattern, String channel, String message) {
		System.out.println("onPMessage: pattern[" + pattern + "],channel[" + channel + "], message[" + message + "]");
	}

	 // 初始化按表达式的方式订阅时候的处理 
	@Override
	public void onPSubscribe(String channel, int subscribedChannels) {
		System.out.println("onSubscribe: channel[" + channel + "]," + "subscribedChannels[" + subscribedChannels + "]");
	}

	// 取消按表达式的方式订阅时候的处理 
	@Override
	public void onPUnsubscribe(String channel, int subscribedChannels) {
		System.out.println("onUnsubscribe: channel[" + channel + "], " + "subscribedChannels[" + subscribedChannels + "]");
	}

	// 初始化订阅时候的处理
	@Override
	public void onSubscribe(String pattern, int subscribedChannels) {
		System.out.println("onPUnsubscribe: pattern[" + pattern + "]," +
		"subscribedChannels[" + subscribedChannels + "]");
	}

	// 取消订阅时候的处理
	@Override
	public void onUnsubscribe(String pattern, int subscribedChannels) {
		System.out.println("onPSubscribe: pattern[" + pattern + "], " +
		"subscribedChannels[" + subscribedChannels + "]");
	}
}
