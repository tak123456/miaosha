package org.tak.controller;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tak.service.OrderService;
import org.tak.service.UserService;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/stock")
@Slf4j
public class StockController {

    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;
    //创建令牌桶
    private RateLimiter rateLimiter=RateLimiter.create(20);


    @GetMapping("/killB/{id}")//悲观锁
    public String killB(@PathVariable("id") Integer id){
        //秒杀的商品id
        System.out.println(id);
        try {
            synchronized (this){
                int orderId=orderService.kill(id);
                return "商品秒杀成功，订单id为"+String.valueOf(orderId);
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }



    @GetMapping("/killL/{id}")//乐观锁
    public String killL(@PathVariable("id") Integer id){
        //秒杀的商品id
        System.out.println(id);
        try {
                int orderId=orderService.kill(id);
                return "商品秒杀成功，订单id为"+String.valueOf(orderId);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/killtoken/{id}")//乐观锁+令牌桶实现限流
    public String killtoken(@PathVariable("id") Integer id){
        System.out.println(id);
        if(!rateLimiter.tryAcquire(2,TimeUnit.SECONDS)){
            return "抢购失败";
        }
        try {
            int orderId=orderService.kill(id);
            return "商品秒杀成功，订单id为"+String.valueOf(orderId);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    //生成md5算法,实现接口隐藏，防止用户使用工具进行秒杀
    @GetMapping ("/md5")
    public String getMD5(Integer id,Integer userid){
        String md5;
        try {
            md5=orderService.getMD5(id,userid);
        }catch (Exception e){
            e.printStackTrace();
            return "获取md5失败"+e.getMessage();
        }
        return "获取md5："+md5;
    }

    //乐观锁+令牌桶+md5接口隐藏
    @GetMapping("/killtokenmd5")
    public String killtokenmd5(Integer id,Integer userid,String md5){
        if(!rateLimiter.tryAcquire(2,TimeUnit.SECONDS)){
            return "抢购失败";
        }
        try {
            int orderId=orderService.killmd5(id,userid,md5);
            return "商品秒杀成功，订单id为"+String.valueOf(orderId);
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    //乐观锁防止超卖+令牌桶限流+md5签名（接口隐藏）+单用户访问频率限制
    @GetMapping("/killtokenlimit")
    public String killtokenlimit(Integer id,Integer userid,String md5){
        //令牌桶限流
        if(!rateLimiter.tryAcquire(2,TimeUnit.SECONDS)){
            return "抢购失败,活动过于火爆，请重试";
        }
        try {
            //单用户调用接口频率限制
            int count=userService.saveUserConut(userid);
            log.info("用户访问次数"+count);
            //进行
            boolean isBanned=userService.getUsercount(userid);
            if(isBanned){
                log.info("用户超过频率限制");
                return "购买失败,超过频率限制";
            }
            int orderId=orderService.killmd5(id,userid,md5);
            return "商品秒杀成功，订单id为"+String.valueOf(orderId);
        } catch (Exception e) {
            return e.getMessage();
        }

    }
}
