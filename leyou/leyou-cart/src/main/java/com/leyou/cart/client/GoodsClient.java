package com.leyou.cart.client;
import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @desc：
 * @auther Liangqi
 * @date 2020/4/9 20:55
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi{
}
