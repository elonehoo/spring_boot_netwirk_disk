package com.inet.codebase.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inet.codebase.entity.Register;
import com.inet.codebase.entity.User;
import com.inet.codebase.service.RegisterService;
import com.inet.codebase.service.TypeService;
import com.inet.codebase.service.UserService;
import com.inet.codebase.utlis.Result;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.Adler32;

/**
 * 管理员的操作
 * @author HCY
 * @since 2020-09-25
 */

@RestController
@RequestMapping("/register")
@CrossOrigin
public class RegisterController {

    @Resource
    private RegisterService registerService;

    @Resource
    private UserService userService;

    @Resource
    private TypeService typeService;

    @Resource
    private RedisTemplate redisTemplate;

    //设置管理员的序号标签
    private String ADMIN = "7F181E5C9EC94BBA88F8F4683D6AE27D";



    /**
     * 进行登录操作
     * @author HCY
     * @since 2020-09-25
     * @param map 前端传来的数据集合
     * @return Result风格的JSON集合对象
     */
    @PostMapping("/login")
    public Result PostLogin(@RequestBody HashMap<String, Object> map){
        //获取登录的账号
        String account = (String) map.get("Account");
        //判断账号是否为空
        if (account.equals("")){
            return new Result("登录失败,账号为空","管理员的登录请求",101);
        }
        //获取登录的密码
        String cipher = (String) map.get("Cipher");
        if(cipher.equals("")){
            return new Result("登录失败,密码为空","管理员的登录请求",101);
        }
        //进行身份的验证
        Map<String , Object> condition = new HashMap<>();
        //进行查询条件的记录
        condition.put("register_account",account);
        condition.put("register_password",cipher);
        //进行查询条件的设置
        QueryWrapper<Register> queryWrapper = new QueryWrapper<>();
        queryWrapper.allEq(condition);
        //查询
        Register register = registerService.getOne(queryWrapper);
        //判断是否查询成功,并且是否是管理员账号
        if (register == null){
            return new Result("登录失败,账号或者密码错误","管理员的登录请求",101);
        }else if ( ! register.getRegisterId().equals(ADMIN)){
            return new Result("登录失败,不是管理员哦","管理员的登录请求",101);
        }
        //是管理员的账号
        //获取User对象
        User user = userService.getById(ADMIN);
        //将user存入Redis
        redisTemplate.opsForValue().set(ADMIN,user);
        //设置返回值 map集合
        Map<String , Object> backtrack = new HashMap<>();
        backtrack.put("msg","登录成功");
        backtrack.put("token",ADMIN);
        return new Result(backtrack,"管理员的登录请求",100);
    }

    /**
     * 进行展示所有的用户参数
     * @author HCY
     * @since 2020-09-25
     * @param token 管理员令牌
     * @param currentPage 页数
     * @param pageSize 每一页显示的条目
     * @param search 搜索的用户名
     * @return Result风格的JSON集合对象
     */
    @GetMapping("/index")
    public Result GetIndex( @RequestParam(value = "Token",defaultValue = "") String token
                           ,@RequestParam(value = "CurrentPage",defaultValue = "1") int currentPage
                           ,@RequestParam(value = "PageSize",defaultValue = "10") int pageSize
                           ,@RequestParam(value = "Search",defaultValue = "") String search){
        //判断token是否是管理员的
        if ( ! token.equals(ADMIN)){
            return new Result("展示失败,登录的并不是管理员账号","管理员展示请求",101);
        }
        Result result = decideToken(token, "管理员登录请求");
        if (result.getCode() != 100){
            return result;
        }
        //设置页数
        Page<User> userPage = new Page<>(currentPage,pageSize);
        //设置查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("user_id",ADMIN);
        //设置是否需要搜索
        if ( ! search.equals("")){
            queryWrapper.like("user_name",search);
        }
        IPage<User> page = userService.page(userPage, queryWrapper);
        return new Result(page,"管理员分页请求",100);
    }

    /**
     * 重置用户密码
     * @author HCY
     * @since 2020-09-25
     * @param map 前端传来的数据集合
     * @return Result风格的JSON集合对象
     */
    @PutMapping("/reset")
    public Result PutUpdate(@RequestBody HashMap<String, Object> map){
        //获取token
        String token = (String) map.get("Token");
        //判断token是否失效
        Result result = decideToken(token, "管理员修改操作");
        if (result.getCode() != 100){
            return result;
        }
        //获取需要重置的密码的序号
        String userId = (String) map.get("UserId");
        //判断修改用户是否为空
        if (userId.equals("")){
            return new Result("修改失败,未选择需要修改的用户","管理员修改操作",101);
        }
        //重置密码
        String password = "123456";
        String newPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        //通过用户需要找到用户
        Register register = registerService.getById(userId);
        register.setRegisterPassword(newPassword);
        //进行修改操作
        boolean jump = registerService.updateById(register);
        if (jump){
            return new Result("修改成功","管理员修改操作",100);
        }else {
            return new Result("修改失败","管理员修改操作",104);
        }
    }


    /**
     * 判断是否登录已经失效
     * @author HCY
     * @since 2020-9-22
     * @param token 用户登录之后的令牌
     * @param message 什么请求信息
     * @return Result风格的对象
     */
    public Result decideToken(String token,String message){
        //判断token是否存在
        if (token.equals("")){
            return new Result("未登录,请先去登录",message,103);
        }
        //通过token获取对象
        User user = (User) redisTemplate.opsForValue().get(token);
        //判断token是否过时
        if (user == null){
            return new Result("登录超时,请重新登录",message,103);
        }
        //设置存储的时间为30分钟
        redisTemplate.expire(token,30L, TimeUnit.MINUTES);
        return new Result("登录成功",message,100);
    }

}
