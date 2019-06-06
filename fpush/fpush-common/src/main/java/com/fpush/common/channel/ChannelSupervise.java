package com.fpush.common.channel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.fyqz.tools.utils.Strings;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChannelSupervise {
    private   static ChannelGroup GlobalGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private  static ConcurrentMap<String, ChannelId> ChannelMap=new ConcurrentHashMap<String, ChannelId>();
    private  static ConcurrentMap<String, Channel> SessionMap=new ConcurrentHashMap<String, Channel>();//key sessionid ,value channel 
    private static ConcurrentMap<String,String> UserAndChannel=new ConcurrentHashMap<String,String>();//key channelid ,value sessionid
    public  static void addChannel(Channel channel){
        GlobalGroup.add(channel);
        ChannelMap.put(channel.id().asShortText(),channel.id());
    }
    public static void removeChannel(Channel channel){
        GlobalGroup.remove(channel);
        ChannelMap.remove(channel.id().asShortText());
        String sessionId=UserAndChannel.get(channel.id().asShortText());
        if(!Strings.isNullOrEmpty(sessionId)) {
        	SessionMap.remove(sessionId);
            UserAndChannel.remove(channel.id().asShortText());
        }
    }
    public static  Channel findChannel(String id){
        return GlobalGroup.find(ChannelMap.get(id));
    }
    public static void send2All(TextWebSocketFrame tws){
        GlobalGroup.writeAndFlush(tws);
    }
    public static int getChannelSize(){
    	return ChannelMap.size();
    }
    public static void setChannelBySessionId(String sessionId,Channel channel) {
    	SessionMap.put(sessionId,channel);
    	UserAndChannel.put(channel.id().asShortText(), sessionId);
    }
    
    public static  Channel findChannelBySessionId(String sessionId){
        return SessionMap.get(sessionId);
    }
}
