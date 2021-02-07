package nl.novi.stuivenberg.springboot.example.security.bvs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;

public class LogUtil {
    public static void logMappedObject(Object obj, Logger logger){
        logMappedObject( obj, logger,false);
    }

    public static void logMappedObject(Object obj, Logger logger,boolean indent){
        ObjectMapper objectMapper = new ObjectMapper();
        if (indent ) objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            logger.info(obj.getClass().getCanonicalName()+ "\n" + objectMapper.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
