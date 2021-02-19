# 秒杀系统的设计

 ## 1.1秒杀的场景

*    电商抢购商品
*    火车12306抢票
*    ......

## 1.2为什么做秒杀

### 防止超卖，100件商品库存结果订单下了120个

### 防止黑产：非法分子使用工具将秒杀福利收入囊中

### 用户体验:高并发下，别的网页打不开

## 1.3保护措施

### 乐观锁防止超卖（核心基础）

### 令牌桶限流

### redis缓存

### 消息队列异步处理订单

## 2本系统使用的技术

### 悲观锁防止超卖(不推荐)

### 乐观锁防止超卖（version加数据库事务，推荐）

### 令牌桶(guava)限流

### 防止非法技技生成md5token存入redis和校验前端传过来的token对比

### 单个用户接口限流（redis计数）

## 3测试结果使用jmeter压力测试，实现在redis中生成kill_1 键，生成md5token



![image](https://github.com/tak123456/miaosha/blob/master/public/images/%E6%88%AA%E5%B1%8F2021-02-18%20%E4%B8%8B%E5%8D%8810.20.18.png)

![image](https://github.com/tak123456/miaosha/blob/master/public/images/%E6%88%AA%E5%B1%8F2021-02-18%20%E4%B8%8B%E5%8D%8810.20.35.png)

![image](https://github.com/tak123456/miaosha/blob/master/public/images/%E6%88%AA%E5%B1%8F2021-02-18%20%E4%B8%8B%E5%8D%8810.19.51.png)

![image](https://github.com/tak123456/miaosha/blob/master/public/images/%E6%88%AA%E5%B1%8F2021-02-18%20%E4%B8%8B%E5%8D%8810.20.55.png)

![image](https://github.com/tak123456/miaosha/blob/master/public/images/%E6%88%AA%E5%B1%8F2021-02-18%20%E4%B8%8B%E5%8D%8810.21.15.png)

![image](https://github.com/tak123456/miaosha/blob/master/public/images/%E6%88%AA%E5%B1%8F2021-02-18%20%E4%B8%8B%E5%8D%8810.22.01.png)

![image](https://github.com/tak123456/miaosha/blob/master/public/images/%E6%88%AA%E5%B1%8F2021-02-18%20%E4%B8%8B%E5%8D%8810.22.07.png)





