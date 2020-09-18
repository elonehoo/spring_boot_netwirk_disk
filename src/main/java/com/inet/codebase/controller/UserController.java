package com.inet.codebase.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inet.codebase.entity.Register;
import com.inet.codebase.entity.User;
import com.inet.codebase.service.RegisterService;
import com.inet.codebase.service.UserService;
import com.inet.codebase.utlis.RegesUtils;
import com.inet.codebase.utlis.Result;
import com.inet.codebase.utlis.UUIDUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author HCY
 * @since 2020-09-18
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Resource
    private RegisterService registerService;

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 登录操作
     * @author HCY
     * @date 2020-09-18
     * @param map 前端传来的map数据集合
     * @return Result风格的JSON集合对象
     */
    @PostMapping("/login")
    public Result PostLogin(@RequestBody HashMap<String, Object> map){
        //获取账号
        String account = (String) map.get("Account");
        //判断账号是否为空
        if (account.equals("")){
            return new Result("账号为空,登录失败","登录请求",104);
        }
        //获取密码
        String password = (String) map.get("Password");
        //判断密码是否为空
        if (password.equals("")){
            return new Result("密码为空,登录失败","登录请求",104);
        }
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //进行登录的状态判断
        Map params = new HashMap();
        QueryWrapper<Register> queryWrapper = new QueryWrapper<>();
        params.put("register_account",account);
        params.put("register_password",password);
        queryWrapper.allEq(params);
        //查询是否有账号和密码统一的人
        Register register = registerService.getOne(queryWrapper);
        //如果没有
        if (register == null){
            return new Result("登录失败,账号或者密码错误","登录请求",104);
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
            return new Result(returning,"登录请求",100);
        }
    }

    /**
     * 进行注册操作
     * @author HCY
     * @date 2020-09-18
     * @param map 获取前端的数据的map集合
     * @return Result 风格的JSON集合数据
     */
    @PostMapping("/register")
    public Result PostRegister(@RequestBody HashMap<String, Object> map){
        //获取账号
        String email = (String) map.get("Email");
        //判断邮箱是否正确
        boolean judgment_email = RegesUtils.isEmail(email);
        if (judgment_email == false){
            return new Result("注册失败! 邮箱错误了哦,请检查邮箱是否正确","注册请求",104);
        }
        //判断账号是否被人使用
        Map<String,Object> params = new HashMap<>();
        //进行条件查询
        params.put("register_account",email);
        QueryWrapper<Register> queryWrapper = new QueryWrapper<>();
        queryWrapper.allEq(params);
        //查询结果
        Register one = registerService.getOne(queryWrapper);
        if (one != null){
            return new Result("注册失败!邮箱已经被注册了,请检查邮箱哦!","注册请求",104);
        }
        //获取密码
        String password = (String) map.get("Password");
        //判断密码是否正确
        boolean judgment_password = RegesUtils.isPassword(password);
        if (judgment_password == false){
            return new Result("注册失败!必须是6-20位的字母、数字（这里字母、数字是指任意组合，没有必须均包含）","注册请求",104);
        }
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //获取用户名
        String name = (String) map.get("Name");
        //判断用户名是否为空
        if (name.equals("")){
            return new Result("注册失败!昵称为空了","注册请求",104);
        }
        //获取头像
        String icon = (String) map.get("Icon");
        if (icon.equals("")){
            icon = "http://47.104.249.85:8080/wwwww/123.png";
        }
        //设置序号
        String userId = UUIDUtils.getId();
        //设置KID
        String kID = UUIDUtils.getToken();
        //获取当前时间(复用,为修改时间和创建时间)
        Date date = new Date();
        //注册User实体类
        User user = new User(userId,name,icon,kID,date,date);
        //注册Register实体类
        Register register = new Register(userId,email,password,date,date);
        //进行存储
        userService.save(user);
        registerService.save(register);
        return new Result("恭喜" + name + "注册成功!","注册操作",100);
    }
}
