package com.fyqz.http_clients;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		Msg msg=new Msg();
		msg.getMap().put("ddddd", "ddd");
		Map m=new HashMap();
		m.put("fdsfsd", "fdsfds");
		m.put("曾超", "zengchao");
		JsonPacket jsonpacket=new JsonPacket();
		jsonpacket.getBody().put("sessionId", "abc");
		jsonpacket.getBody().put("clientType", "2");
		jsonpacket.getBody().put("pushIds", "abc");
		jsonpacket.getBody().put("data",objectMapper.writeValueAsString(m));
		jsonpacket.setCmd(Command.FAST_CONNECT.cmd);
		
		HttpUtil.httpPostJson("http://127.0.0.1:8081/route",objectMapper.writeValueAsString(jsonpacket));
	}
	
	
	public static Map<String, Object> objectToMap(Object obj) throws Exception {    
        if(obj == null)  
            return null;      
  
        Map<String, Object> map = new HashMap<String, Object>();   
  
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());    
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();    
        for (PropertyDescriptor property : propertyDescriptors) {    
            String key = property.getName();    
            if (key.compareToIgnoreCase("class") == 0) {   
                continue;  
            }  
            Method getter = property.getReadMethod();  
            Object value = getter!=null ? getter.invoke(obj) : null;  
            map.put(key, value);  
        }    
  
        return map;  
    }    
	
	public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {    
        if (map == null)  
            return null;  
  
        Object obj = beanClass.newInstance();  
  
        org.apache.commons.beanutils.BeanUtils.populate(obj, map);  
  
        return obj;  
    }    
}
