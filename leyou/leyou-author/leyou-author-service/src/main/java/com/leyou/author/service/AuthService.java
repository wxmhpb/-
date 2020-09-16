package com.leyou.author.service;

import com.leyou.author.client.UserClient;
import com.leyou.author.config.JwtConfig;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JwtUtils;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtConfig jwtConfig;
    public String accredit(String username,String password){
        //根据用户名和密码进行查询
        User user=this.userClient.queryUser(username,password);
        //判断user
        if(user==null){
            return null;
        }

        try{
            //通过jwtUtils生成jwt类型的token
            UserInfo userInfo=new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
           return  JwtUtils.generateToken(userInfo,this.jwtConfig.getPrivateKey(),this.jwtConfig.getExpire());
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
