package com.fpush.plat.network.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpush.common.api.protocol.JsonPacket;
import com.fyqz.tools.utils.HttpUtil;

import java.util.HashMap;

public class DemoTest {
    public static void main(String[] args) {

    }
    static  void post(SendMsgDto msgDto) throws Exception{
        ObjectMapper objectMapper=new ObjectMapper();
        String socketurl="http://192.168.0.79:7777/route";
        HashMap map=new HashMap(1);
        map.put("socketData",msgDto);
        JsonPacket jsonPacket=new JsonPacket();
        jsonPacket.getBody().put("sessionId","abc");
        jsonPacket.getBody().put("clientType","2");
        jsonPacket.getBody().put("pushIds","abc");
        jsonPacket.getBody().put("data",objectMapper.writeValueAsBytes(map));
        HttpUtil.httpPostJson(socketurl,objectMapper.writeValueAsString(jsonPacket));
    }
}