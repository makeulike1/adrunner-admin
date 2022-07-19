package com.gnm.adrunner.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.gnm.adrunner.config.RedisConfig;
import com.gnm.adrunner.server.object.RedisEntity;
import com.gnm.adrunner.server.object.RedisGroup;

import org.springframework.data.redis.core.RedisCallback;

public class redisUtil {



    
    // Redis 리스트 사이즈 반환
    public static Integer getListSize(String ckListId, Integer redisGroup, Integer redisDB){
        Integer result = 0;

        RedisGroup currentRedis = RedisConfig.redisConn.get(redisGroup);

        for(int i=0;i<currentRedis.getList().size();i++){
            RedisEntity redisEntity = currentRedis.getList().get(i);

            Integer tmp = 0;
            Object clkKeyListSize       = redisEntity.getDbList().get(redisDB).opsForList().size(ckListId);
            if(clkKeyListSize == null)
                tmp = 0;
            else tmp = Integer.parseInt(clkKeyListSize.toString());
            result += tmp;
        }

        return result;
    }




    

    // Redis 클릭수 반환
    public static Integer getCkCount(String adsKey, Integer redisGroup, Integer redisDB){

        Integer totalCkCount = 0;

        RedisGroup currentRedis = RedisConfig.redisConn.get(redisGroup);

        for(int i=0;i<currentRedis.getList().size();i++){
            RedisEntity redisEntity = currentRedis.getList().get(i);

            Set<String> redisKeys = redisEntity.getDbList().get(redisDB).keys(adsKey+":*");
            List<String> keysList = new ArrayList<>(); 
            Iterator<String> it = redisKeys.iterator();
            while (it.hasNext()) {
                keysList.add(it.next());
            }

            
            Integer clickCount = 0;
            for(String key : keysList){
                Object tmp = redisEntity.getDbList().get(redisDB).opsForList().size(key);
                if(tmp != null)
                    clickCount += Integer.parseInt(tmp.toString());
            }

            totalCkCount += clickCount;
        }

        return totalCkCount;
    }






    // Redis 클릭수 반환 ( 매체사 포함 )
    public static Integer getCkCount(String adsKey, String mediaKey, Integer redisGroup, Integer redisDB){

        Integer totalCkCount = 0;
        RedisGroup currentRedis = RedisConfig.redisConn.get(redisGroup);

        for(int i=0;i<currentRedis.getList().size();i++){
            RedisEntity redisEntity = currentRedis.getList().get(i);

            Set<String> redisKeys = redisEntity.getDbList().get(redisDB).keys(adsKey+":*:"+mediaKey);
            List<String> keysList = new ArrayList<>(); 
            Iterator<String> it = redisKeys.iterator();
            while (it.hasNext()) {
                keysList.add(it.next());
            }

            
            Integer clickCount = 0;
            for(String key : keysList){
                Object tmp = redisEntity.getDbList().get(redisDB).opsForList().size(key);
                if(tmp != null)
                    clickCount += Integer.parseInt(tmp.toString());
            }

            totalCkCount += clickCount;
        }

        return totalCkCount;
    }
    






    // Redis DB 초기화 
    public static void flushDB(Integer redisGroup, Integer redisDB){
        RedisGroup currentRedis = RedisConfig.redisConn.get(redisGroup);
        
        for(int i=0;i<currentRedis.getList().size();i++){
            RedisEntity redisEntity = currentRedis.getList().get(i);

            redisEntity.getDbList().get(redisDB).execute((RedisCallback<String>) connection -> {
                connection.flushDb();
                return "ok";
            });

            redisEntity = null;
        }

        currentRedis = null;
    }
 
 
    

    // Redis에서 클릭키 검색
    public static Boolean findck(String adsKey, String ck, Integer redisGroup, Integer redisDB){

        RedisGroup currentRedis  = RedisConfig.redisConn.get(redisGroup);
         
        for(int i=0;i<currentRedis.getList().size();i++){
            
            RedisEntity redisEntity = currentRedis.getList().get(i);

            Set<String> redisKeys = redisEntity.getDbList().get(redisDB).keys(adsKey+"*");
            List<String> keysList = new ArrayList<>(); 
            Iterator<String> it = redisKeys.iterator();
            while (it.hasNext()) {
                keysList.add(it.next());
            }

            for(String listKey : keysList){
                Object tmp = redisEntity.getDbList().get(redisDB).opsForList().indexOf(listKey, ck);
                if(tmp != null)
                    return true;
            }

            redisEntity = null;
        }

        currentRedis = null;

        
        return false;
    }





    // Redis에서 금일 마지막으로 발생한 클릭키 100건 검색
    public static List<String> getLatestck(String adsKey, String[] mediaKeyList, Integer redisGroup, Integer redisDB){


        List<String> result = new ArrayList<String>();
        
        for(String e : mediaKeyList){

            // Redis에서 클릭키 조회 - 리스트 이름은 [광고키:오늘날짜:매체사키] 정규식 패턴
            RedisGroup currentRedis  = RedisConfig.redisConn.get(redisGroup); 

            for(int i=0;i<currentRedis.getList().size();i++){
                
                RedisEntity redisEntity = currentRedis.getList().get(i);

                String ck = adsKey+":"+timeBuilder.getTodayDate()+":"+e;
                List<Object> list = redisEntity.getDbList().get(redisDB).opsForList().range(ck, 0, 50);
                for(Object e1 : list)result.add(e1.toString());
                list = null;

                redisEntity = null;
            }

            currentRedis = null;
            
        }

        return result;
    }




}
