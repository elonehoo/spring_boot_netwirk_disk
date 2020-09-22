package com.inet.codebase.controller;


import com.inet.codebase.entity.Document;
import com.inet.codebase.entity.User;
import com.inet.codebase.service.FileService;
import com.inet.codebase.utlis.Result;
import com.inet.codebase.utlis.UUIDUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author HCY
 * @since 2020-09-21
 */
@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileController {
    //设置文件的存储路径
    private String fileRootPath = "C:\\Program Files\\Apache Software Foundation\\Tomcat 8.5\\webapps\\files";

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private FileService fileService;

    @PostMapping("/uploads")
    public Result PostUploads(@RequestParam(value = "file") MultipartFile[] files
                              ,@RequestParam(value = "Token") String token
                              ,@RequestParam(value = "TypeId") String typeId){

        //判断token是否失效
        Result result = decideToken(token, "上传文件请求");
        if (result.getCode() != 100){
            return result;
        }
        //判断文件类别是否为空
        if (typeId.equals("")){
            return new Result("上传失败,文件没有类别","上传文件请求",101);
        }
        //创建集合
        List<Document> documents = new ArrayList<>();
        //进行文件的读取
        for (MultipartFile file : files ) {
            //创建实体类
            Document rootFile = new Document();
            //设置文件得所属人
            rootFile.setFileAffiliation(token);
            //设置文件得创建时间和测试时间
            Date date = new Date();
            rootFile.setFileCreation(date);
            rootFile.setFileModification(date);
            //设置文件得类别
            rootFile.setFileType(typeId);
            //判断文件是否存在
            if (file.isEmpty()){
                return new Result("上传失败,文件为空","上传文件请求",101);
            }
            //获取文件名字
            String fileName = file.getOriginalFilename();
            //文件得原本得名称
            rootFile.setFileName( fileName.substring(0, fileName.lastIndexOf(".")) );
            //文件得后缀
            String postfix = fileName.substring(fileName.lastIndexOf("."));
            rootFile.setFilePostfix( postfix );
            //设置文件得序号
            rootFile.setFileId(UUIDUtils.getId());
            //修改文件得名字
            String uuid = UUIDUtils.getId();
            rootFile.setFileUuname(uuid);
            //设置文件的大小
            Long fileSize = file.getSize() / 1024;
            rootFile.setFileSize(fileSize);
            //更换名字
            fileName = uuid + postfix;
            //存放图片
            File dest = new File(fileRootPath + "/" + fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            //判断是否上传成功
            try {
                file.transferTo(dest);
                String url="http://47.104.249.85:8080/files/"+fileName;
                rootFile.setFileSite(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //进行集合的添加
            documents.add(rootFile);
        }
        boolean judgment = fileService.saveBatch(documents);
        if (judgment){
            return new Result("上传成功","上传文件请求",100);
        }else {
            return new Result("上传失败","上传文件请求",104);
        }
    }

    /**
     * 判断是否登录已经失效
     * @author HCY
     * @since 2020-9-21
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
