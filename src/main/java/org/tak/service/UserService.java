package org.tak.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    public int saveUserConut(Integer userid){
        //根据不同用户id生成调用次数key
        String limitKey="limit_"+userid;
        //获取redis中key的调用次数
        String limitNum=stringRedisTemplate.opsForValue().get(limitKey);
        int limit=-1;
        if(limitNum==null){
            //第一次调用redis中设置0
            stringRedisTemplate.opsForValue().set(limitKey,"0",3600, TimeUnit.SECONDS);
        }else {
            //不是第一次加1
            limit=Integer.parseInt(limitNum)+1;
            stringRedisTemplate.opsForValue().set(limitKey,String.valueOf(limit),3600,TimeUnit.SECONDS);
        }
        return limit;
    }

    public boolean getUsercount(Integer userid){
        String limitKey="limit_"+userid;
        String limitNum=stringRedisTemplate.opsForValue().get(limitKey);
        if(limitNum==null){
           log.error("没有记录异常");
           return true;
        }
        return Integer.parseInt(limitNum)>10;//false代表没有超过
    }
}
