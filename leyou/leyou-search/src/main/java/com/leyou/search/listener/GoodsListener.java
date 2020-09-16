package com.leyou.search.listener;

import com.leyou.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GoodsListener {

    @Autowired
    private SearchService searchService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "leyou.search.save.queue", durable = "true"),
            exchange = @Exchange(value = "ley.item.exchange", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}
    ))
    public void save(Long id) throws IOException {
        if (id == null) {
            return;
        }
        this.searchService.save(id);
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "leyou.search.delete.queue", durable = "true"),
            exchange = @Exchange(value = "ley.item.exchange", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            key = {"item.delete"}
    ))
    public void delete(Long id) throws IOException {
        if (id == null) {
            return;
        }
        this.searchService.delete(id);
    }
}
