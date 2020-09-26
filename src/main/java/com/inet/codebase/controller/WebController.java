package com.inet.codebase.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inet.codebase.entity.Document;
import com.inet.codebase.entity.User;
import com.inet.codebase.service.FileService;
import com.inet.codebase.service.UserService;
import com.inet.codebase.utlis.Result;
import com.inet.codebase.utlis.UUIDUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author HCY
 * @since 2020-09-18
 */
@RestController
@RequestMapping("/web")
@CrossOrigin
public class WebController {

    //设置文件的存储路径
    private String fileRootPath = "C:\\Program Files\\Apache Software Foundation\\Tomcat 8.5\\webapps\\files";

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private FileService fileService;

    @Resource
    private UserService userService;


    /**
     * 使用Kid上传文件
     * @author HCY
     * @since 2020-9-23
     * @param files 文件列表
     * @param kid 通过API上传的序号
     * @return Result风格的JSON集合对象
     */
    @PostMapping("/kidUpload")
    public Result PostKID( @RequestParam(value = "file") MultipartFile[] files
                         , @RequestParam(value = "KID") String kid){
        //判断Kid是否为空
        if (kid.equals("")){
            return new Result("上传失败,KID为空","上传文件请求",101);
        }
        //查询上传的条件
        Map<String , Object> condition = new HashMap<>();
        //为 user_KID = kid
        condition.put("user_KID" , kid);
        //进行条件的插入
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.allEq(condition);
        //进行查询
        User user = userService.getOne(queryWrapper);
        String fileTypeId = user.getUserId();
        String fileAffiliation = user.getUserId();
        Result result = uploadIng(files, fileAffiliation, fileTypeId);
        return result;
    }



    /**
     * 上传文件的方法
     * @author HCY
     * @since 2020-9-23
     * @param files 需要上传的文件集合
     * @param fileAffiliation 文件的所属人
     * @param fileTypeId 文件的列别
     * @return Result风格的JSON集合对象
     */
    public Result uploadIng(MultipartFile[] files , String fileAffiliation, String fileTypeId){
        //创建集合
        List<Document> documents = new ArrayList<>();
        //进行文件的读取
        for (MultipartFile file : files ) {
            //创建实体类
            Document rootFile = new Document();
            //设置文件得所属人
            rootFile.setFileAffiliation(fileAffiliation);
            //设置文件得创建时间和测试时间
            Date date = new Date();
            rootFile.setFileCreation(date);
            rootFile.setFileModification(date);
            //设置文件得类别
            rootFile.setFileType(fileTypeId);
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
}
