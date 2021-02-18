package org.tak.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.tak.entity.Order;
import org.tak.entity.Stock;
import org.tak.entity.User;
import org.tak.mapper.OrderMapper;
import org.tak.mapper.StockMapper;
import org.tak.mapper.UserMapper;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@Slf4j
public class OrderService {

    @Autowired
    StockMapper stockMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    UserMapper userMapper;
    //根据商品id校验库存，扣除库存，创建订单

    public int killL(Integer id){
        Stock stock=selectStock(id);
        updateStock(stock);
        return createOrder(stock);
    }

    public  int kill(Integer id) {
        //判断商品是否还在redis中
        if(!stringRedisTemplate.hasKey("kill"+id)){
            throw new RuntimeException("当前抢购活动已经结束");
        }
        Stock stock=selectStock(id);
        updateStock(stock);
        return createOrder(stock);
    }

    //校验库存
    private Stock selectStock(Integer id){
        Stock stock=stockMapper.selectStockById(id);
        if(stock.getSale().equals(stock.getCount())){
            throw new RuntimeException("库存不足");
        }
        return stock;
    }
    //扣除库存，悲观锁方式
    private void updateStockB(Stock stock){
        stockMapper.updateStockB(stock);
    }
    //扣除库存
    private void updateStock(Stock stock){
        //在数据库层面上利用version和数据库事务使用乐观锁
//        stock.setSale(stock.getSale()+1);悲观锁
        int res=stockMapper.updateStock(stock);
        if(res==0){
            throw new RuntimeException("抢购失败");
        }
    }
    //创建订单
    private Integer createOrder(Stock stock){
        Order order=new Order();
        order.setSid(stock.getId()).setName(stock.getName()).setCreateTime(new Date());
        orderMapper.createOrder(order);
        return order.getId();
    }

    public String getMD5(Integer id, Integer userid) {
        //验证用户合法性
        //生成md5存入redis
        User user=userMapper.findUserById(userid);
        if(user==null){
            throw  new RuntimeException("用户不存在");
        }
        log.info("用户信息:"+user.toString());
        //验证商品合法性
        Stock stock=stockMapper.selectStockById(id);
        if(stock==null){
            throw new RuntimeException("商品不存在");
        }
        log.info("商品信息:"+stock.toString());
        //hashkey
        String Key="key_"+userid+"_"+id;
        String value= DigestUtils.md5DigestAsHex((userid+id+"!q*js3").getBytes());
        stringRedisTemplate.opsForValue().set(Key,value,3600, TimeUnit.SECONDS);
        log.info("写入redis"+"hashkey:"+Key  +"key:"+value);
        return value;

    }

    public int killmd5(Integer id, Integer userid, String md5) {
        //判断商品是否还在redis中
        if(!stringRedisTemplate.hasKey("kill"+id)){
            throw new RuntimeException("当前抢购活动已经结束");
        }
        //验证签名
        String key="key_"+userid+"_"+id;
        String s=stringRedisTemplate.opsForValue().get(key);
        if(s==null)
            throw new RuntimeException("没有签名");
        if(!stringRedisTemplate.opsForValue().get(key).equals(md5))
            throw new RuntimeException("当前请求数据不合法");
        Stock stock=selectStock(id);
        updateStock(stock);
        return createOrder(stock);
    }
}
