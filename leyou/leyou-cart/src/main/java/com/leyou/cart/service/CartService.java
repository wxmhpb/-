package com.leyou.cart.service;

import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.KeyBoundCursor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @desc：
 * @auther Liangqi
 * @date 2020/4/9 19:53
 */
@Service
public class CartService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private GoodsClient goodsClient;

    static final String KEY_PREFIX = "user:cart:";

    static final Logger logger = LoggerFactory.getLogger(CartService.class);

    public void addCart(Cart cart) {
        // 获取登录用户
        UserInfo user = LoginInterceptor.getUser();
        // Redis的key
        String key = KEY_PREFIX + user.getId();
        // 获取hash操作对象
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        // 查询是否存在
        Long skuId = cart.getSkuId();
        Integer num = cart.getNum();
        String k=cart.getSkuId().toString();
        if (hashOps.hasKey(k)) {
            // 存在，获取购物车数据
            String cartJson = hashOps.get(k).toString();
            cart = JsonUtils.parse(cartJson, Cart.class);
            // 修改购物车数量
            cart.setNum(cart.getNum() + num);
            hashOps.put(key,JsonUtils.serialize(cart));
        } else {
            // 不存在，新增购物车数据
            cart.setUserId(user.getId());
            // 其它商品信息，需要查询商品服务
            Sku sku = this.goodsClient.querySkuBySkuId(cart.getSkuId());
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
        }
        // 将购物车数据写入redis
        hashOps.put(k, JsonUtils.serialize(cart));
    }
    public List<Cart> queryCarts() {
        // 获取登录用户
        UserInfo user = LoginInterceptor.getUser();

        // 判断是否存在购物车
        String key = KEY_PREFIX + user.getId();
        if (!this.redisTemplate.hasKey(key)) {
            // 不存在，直接返回
            return null;
        }

        //获取购物车记录--cart集合
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        List<Object> carts = hashOps.values();
        // 判断是否有数据--购物车集合为空
        if (CollectionUtils.isEmpty(carts)) {
            return null;
        }
        // 查询购物车数据
        return carts.stream().map(o -> JsonUtils.parse(o.toString(), Cart.class)).collect(Collectors.toList());
    }

        //更新购物车
        public void updateNum(Cart cart) {
            // 获取登陆信息
            UserInfo user = LoginInterceptor.getUser();
           if(!this.redisTemplate.hasKey(KEY_PREFIX+user.getId())){
               return ;
           }
            Integer num=cart.getNum();
            // 获取hash操作对象
            BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(KEY_PREFIX+user.getId());
            System.out.println("\n\n\n\n\n\n\n"+cart.toString());
            // 获取购物车信息
            String cartJson = hashOperations.get(cart.getSkuId().toString()).toString();
            Cart cart1 = JsonUtils.parse(cartJson, Cart.class);
            // 更新数量
            cart1.setNum(cart.getNum());
            // 写入购物车
            hashOperations.put(cart.getSkuId().toString(), JsonUtils.serialize(cart1));
        }

        //删除
        public void deleteCart(String skuId) {
            // 获取登录用户
            UserInfo user = LoginInterceptor.getUser();
            String key = KEY_PREFIX + user.getId();
            BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
            hashOps.delete(skuId);
        }


}
