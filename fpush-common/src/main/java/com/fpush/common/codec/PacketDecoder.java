package com.fpush.common.codec;

import java.util.Map;

import com.fpush.common.api.protocol.JsonPacket;
import com.fpush.common.api.protocol.Packet;
import com.fyqz.tools.utils.Jsons;


public class PacketDecoder {
    public static Packet decodeFrame(String frame){
        if (frame == null) return null;
        return Jsons.fromJson(frame, JsonPacket.class);
    }
    
	public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {    
        if (map == null)  
            return null;  
  
        Object obj = beanClass.newInstance();  
  
        org.apache.commons.beanutils.BeanUtils.populate(obj, map);  
  
        return obj;  
    }    
    
}
