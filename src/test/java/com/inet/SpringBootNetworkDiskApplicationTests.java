package com.inet;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inet.codebase.entity.Register;
import com.inet.codebase.entity.Type;
import com.inet.codebase.entity.User;
import com.inet.codebase.service.RegisterService;
import com.inet.codebase.service.TypeService;
import com.inet.codebase.service.UserService;
import com.inet.codebase.utlis.RegesUtils;
import com.inet.codebase.utlis.Result;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class SpringBootNetworkDiskApplicationTests {

    @Resource
    private RegisterService registerService;

    @Resource
    private UserService userService;

    @Resource
    private TypeService typeService;

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        String account = "2414776185@qq.com";
        String password = "1";
        Map params = new HashMap();
        QueryWrapper<Register> queryWrapper = new QueryWrapper<>();
        params.put("register_account",account);
        params.put("register_password",password);
        queryWrapper.allEq(params);
        Register register = registerService.getOne(queryWrapper);
        //如果没有
        if (register == null){
//            return new Result("登录失败,账号或者密码错误","登录请求",104);
            System.out.println("登录失败,账号或者密码错误");
        }else {
            //获取用户的具体信息
            User user = userService.getById(register.getRegisterId());
            //将数据存储NoSQL
            redisTemplate.opsForValue().set(register.getRegisterId(),user);
            //设置存储的时间为30分钟
//            redisTemplate.expire(register.getRegisterId(),30L, TimeUnit.MINUTES);
            //设置返回模式
            Map<String,Object> returning = new HashMap<>();
            returning.put("msg","登录成功");
            returning.put("token",register.getRegisterId());
            System.out.println(user);
//            return new Result(returning,"登录请求",100);
            System.out.println(returning);
        }
    }

    @Test
    void contextLoads2() {
        String fileName = "1F9E696A2FB04235BBC0B0398C9E58D4.jpg";
        String substring = fileName.substring(fileName.lastIndexOf("."));;
        System.out.println(substring);
    }



    @Test
    void contextLoads3() {
        QueryWrapper<Type> queryWrapper = new QueryWrapper<>();
        List<Type> list = typeService.list(queryWrapper);
        System.out.println(list);
    }

}
