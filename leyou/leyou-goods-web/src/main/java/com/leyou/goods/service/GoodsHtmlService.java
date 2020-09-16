package com.leyou.goods.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;

@Service
public class GoodsHtmlService {

    @Autowired
    private TemplateEngine engine;

    @Autowired
    private GoodsService goodsService;

    //创建静态页面
    public void createHtml(Long spuId){
        //初始化运行上下文
        Context context = new Context();
        context.setVariables(this.goodsService.loadData(spuId));
        PrintWriter printWriter=null;
        try{
            //把静态文件生成到本地服务器
            File file = new File("D:\\AC\\NewFile\\SSM_idea\\leyou_mm\\tools\\nginx-1.14.0\\html\\item\\" + spuId + ".html");
             printWriter=new PrintWriter(file);
            this.engine.process("item",context,printWriter);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }finally {
            if(printWriter!=null){
                printWriter.close();
            }
        }
   }

   //删除静态页面
    public void deleteHtml(Long id){
        File file = new File("D:\\AC\\NewFile\\SSM_idea\\leyou_mm\\tools\\nginx-1.14.0\\html\\item\\" + id + ".html");
        file.deleteOnExit();
    }

}
