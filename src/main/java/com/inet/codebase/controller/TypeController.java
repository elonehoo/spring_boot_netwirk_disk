package com.inet.codebase.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inet.codebase.entity.File;
import com.inet.codebase.entity.Type;
import com.inet.codebase.entity.User;
import com.inet.codebase.service.FileService;
import com.inet.codebase.service.TypeService;
import com.inet.codebase.utlis.Result;
import com.inet.codebase.utlis.UUIDUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author HCY
 * @since 2020-09-20
 */
@RestController
@RequestMapping("/type")
@CrossOrigin
public class TypeController {

    @Resource
    private TypeService typeService;

    @Resource
    private FileService fileService;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 添加类别
     * @author HCY
     * @since 2020-9-20
     * @param map 前端传递的数据map集合
     * @return Result风格的JSON集合对象
     */
    @PostMapping("/newly")
    public Result PostNewly(@RequestBody HashMap<String, Object> map){
        //获取token
        String token = (String) map.get("Token");
        //判断token是否存在
        if (token.equals("")){
            return new Result("未登录,请先去登录","添加类别请求",103);
        }
        //通过token获取对象
        User user = (User) redisTemplate.opsForValue().get(token);
        //判断token是否过时
        if (user == null){
            return new Result("登录超时,请重新登录","添加类别请求",103);
        }
        //设置存储的时间为30分钟
        redisTemplate.expire(token,30L, TimeUnit.MINUTES);
        //获取类别名称
        String typeName = (String) map.get("Name");
        //判断类别是否为空
        if (typeName.equals("")){
            return new Result("添加失败,类别名称为空","添加类别",101);
        }
        //创建类别的序号
        String typeId = UUIDUtils.getId();
        //创建事件(进行创建事件和修改事件的复用)
        Date date = new Date();
        //创建实体类对象
        Type type = new Type(typeId,typeName,date,date,token,0);
        //进行添加操作
        boolean judgment = typeService.save(type);
        //判断是否添加成功
        if (judgment){
            return new Result(typeName + "添加成功","添加类别请求",100);
        }else {
            return new Result(typeName + "添加失败","添加类别请求",104);
        }
    }

    /**
     * 修改类别的名称
     * @author HCY
     * @since 2020-9-20
     * @param map 前端传递的数据map集合
     * @return Result风格的JSON集合对象
     */
    @PutMapping("/renewal")
    public Result PutRenewal(@RequestBody HashMap<String, Object> map){
        //获取token
        String token = (String) map.get("Token");
        //判断token是否存在
        if (token.equals("")){
            return new Result("未登录,请先去登录","修改类别请求",103);
        }
        //通过token获取对象
        User user = (User) redisTemplate.opsForValue().get(token);
        //判断token是否过时
        if (user == null){
            return new Result("登录超时,请重新登录","修改类别请求",103);
        }
        //设置存储的时间为30分钟
        redisTemplate.expire(token,30L, TimeUnit.MINUTES);
        //获取新的类别名称
        String typeName = (String) map.get("Name");
        //判断类别是否为空
        if (typeName.equals("")){
            return new Result("修改失败,修改的类别名称为空","修改类别请求",101);
        }
        //获取需要修改的类别序号
        String typeId = (String) map.get("TypeId");
        //判断序号是否为空
        if (typeId.equals("")){
            return new Result("修改失败,未知的修改序号","修改类别请求",101);
        }
        //创建修改事件
        Date date = new Date();
        //通过序号查询博客
        Type type = typeService.getById(typeId);
        //判断所属人是否正确
        if (! type.getTypeAffiliation().equals(token) ){
            return new Result("修改失败,可能该类别不属于您","修改类别请求",101);
        }
        //进行修改数据的添加
        type.setTypeModification(date);
        type.setTypeName(typeName);
        //进行修改操作
        boolean judgment = typeService.updateById(type);
        //判断是否修改成功
        if (judgment){
            return new Result("修改成功","修改类别请求",100);
        }else {
            return new Result("修改失败","修改类别请求",104);
        }
    }

    /**
     * 获取所有的类别+总数
     * @author HCY
     * @since 2020-9-20
     * @param token 令牌进行退出的必要操作
     * @return Result风格的JSON集合对象
     */
    @GetMapping("/index")
    public Result GetIndex(@RequestParam String token){
        //判断token是否存在
        if (token.equals("")){
            return new Result("未登录,请先去登录","类别请求",103);
        }
        //通过token获取对象
        User user = (User) redisTemplate.opsForValue().get(token);
        //判断token是否过时
        if (user == null){
            return new Result("登录超时,请重新登录","类别请求",103);
        }
        //设置存储的时间为30分钟
        redisTemplate.expire(token,30L, TimeUnit.MINUTES);
        //设置查询条件
        Map<String , Object> condition = new HashMap<>();
        //进行条件的设置
        condition.put("type_affiliation",token);
        //创建条件构造器
        QueryWrapper<Type> queryWrapper = new QueryWrapper<>();
        queryWrapper.allEq(condition);
        //进行查询
        List<Type> typeList = typeService.list(queryWrapper);
        //进行查询所有类别的总数
        for (int i = 0 ; i < typeList.size() ; i++){
            //进行Map集合的创建
            Map<String,Object> map = new HashMap<>();
            map.put("file_type", typeList.get(i).getTypeId());
            //进行查询条件的设定
            QueryWrapper<File> parameter = new QueryWrapper<>();
            //进行条件的设置
            parameter.allEq(map);
            //进行总数的查询
            int count = fileService.count(parameter);
            typeList.get(i).setCount(count);
        }
        return new Result(typeList,"类别的请求",100);
    }

    @DeleteMapping("/delete")
    public Result Delete(@RequestParam String token,
                         @RequestParam String typeId){
        //判断token是否存在
        if (token.equals("")){
            return new Result("未登录,请先去登录","删除类别请求",103);
        }
        //通过token获取对象
        User user = (User) redisTemplate.opsForValue().get(token);
        //判断token是否过时
        if (user == null){
            return new Result("登录超时,请重新登录","删除类别请求",103);
        }
        //设置存储的时间为30分钟
        redisTemplate.expire(token,30L, TimeUnit.MINUTES);
        //判断需要删除的类别序号是否为空
        if (typeId.equals("")){
            return new Result("删除失败,删除序号为空","删除类别请求",101);
        }
        //进行删除操作
        boolean judgment = typeService.removeById(typeId);
        if (judgment){
            return new Result("删除成功","删除类别请求",100);
        }else {
            return new Result("删除失败","删除类别请求",104);
        }
    }
}
