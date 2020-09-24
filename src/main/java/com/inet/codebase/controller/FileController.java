package com.inet.codebase.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inet.codebase.entity.Document;
import com.inet.codebase.entity.User;
import com.inet.codebase.service.FileService;
import com.inet.codebase.service.UserService;
import com.inet.codebase.utlis.Result;
import com.inet.codebase.utlis.UUIDUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
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

    @Resource
    private UserService userService;

    /**
     * 上传文件
     * @author HCY
     * @since 2020-9-22
     * @param files 文件列表
     * @param token 用户令牌
     * @param typeId 文件的列别
     * @return Result风格的JSON集合对象
     */
    @PostMapping("/uploads")
    public Result PostUploads(@RequestParam(value = "file") MultipartFile[] files
                              ,@RequestParam(value = "Token") String token
                              ,@RequestParam(value = "TypeId",defaultValue = "") String typeId){

        //判断token是否失效
        Result result = decideToken(token, "上传文件请求");
        if (result.getCode() != 100){
            return result;
        }
        //判断文件类别是否为空
        if (typeId.equals("")){
            typeId = token;
        }
        Result uploadIng = uploadIng(files, token, typeId);
        return uploadIng;
    }

    /**
     * 使用Kid上传文件
     * @author HCY
     * @since 2020-9-23
     * @param files 文件列表
     * @param kid 通过API上传的序号
     * @return Result风格的JSON集合对象
     */
    @PostMapping("/KID")
    public Result PostKID(@RequestParam(value = "file") MultipartFile[] files
                         ,@RequestParam(value = "KID") String kid){
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
     * 下载操作
     * @author HCY
     * @since 2020-9-23
     * @param response response操作
     * @param token 令牌
     * @param fileId 文件的序号
     * @return Result风格的JSON集合对象
     */
    @GetMapping("/down")
    public void GetDown(HttpServletResponse response
                         ,@RequestParam(value = "Token",defaultValue = "") String token
                         ,@RequestParam(value = "FileId",defaultValue = "") String fileId){
        //判断token是否为空
        Result result = decideToken(token, "下载请求");
        if (result.getCode() != 100){
        }
        //获取文件的名字
        Document document = fileService.getById(fileId);
        //判断文件是否是用户的
        if ( ! document.getFileAffiliation().equals(token)){
            return;
        }
        //获取文件现在的名字
        String fileName = document.getFileUuname() + document.getFilePostfix();
        //判断文件名字是否为空
        if (fileName.equals("")){
            return;
        }
        // 设置信息给客户端不解析
        String type = new MimetypesFileTypeMap().getContentType(fileName);
        // 设置contenttype，即告诉客户端所发送的数据属于什么类型
        response.setHeader("Content-type",type);
        // 设置编码
        String hehe = null;
        try {
            hehe = new String(fileName.getBytes("utf-8"), "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 设置扩展头，当Content-Type 的类型为要下载的类型时 , 这个信息头会告诉浏览器这个文件的名字和类型。
        response.setHeader("Content-Disposition", "attachment;filename=" + hehe);
        //下载操作
        Result downloadFile = null;
        try {
            downloadFile = downloadFile(response, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 展示文件,可以进行分类查询
     * @author HCY
     * @since 2020-09-23
     * @param token 用户令牌
     * @param typeId 类别序号
     * @param currentPage 页数
     * @param pageSize 每页显示条目个数
     * @return Result风格的JSON集合对象
     */
    @GetMapping("/list")
    public Result GetList( @RequestParam(value = "Token",defaultValue = "") String token
                          ,@RequestParam(value = "TypeId",defaultValue = "") String typeId
                          ,@RequestParam(value = "FileName",defaultValue = "") String fileName
                          ,@RequestParam(value = "CurrentPage",defaultValue = "1") int currentPage
                          ,@RequestParam(value = "PageSize",defaultValue = "10") int pageSize){
        //判断Token是否失效
        Result result = decideToken(token, "列表请求");
        //是否需要输出
        if (result.getCode() != 100){
            return result;
        }
        //进行查询条件的部署
        Map<String , Object> condition = new HashMap<>();
        QueryWrapper<Document> queryWrapper = new QueryWrapper<>();
        //设置查询文件的所属人
        condition.put("file_affiliation",token);
        //判断是否进行了类别分类
        if ( ! typeId.equals("")){
            condition.put("file_type",typeId);
        }
        //判断是否需要进行名字搜索
        if ( ! fileName.equals("")){
            queryWrapper.like("file_name",fileName);
        }
        //进行查询条件的部署
        queryWrapper.allEq(condition);
        //设置分页操作
        Page<Document> page = new Page<>(currentPage,pageSize);
        //进行查询
        IPage<Document> documentIPage = fileService.page(page, queryWrapper);
        return new Result(documentIPage,"列表请求",100);
    }

    /**
     * 删除操作
     * @author HCY
     * @since 2020-09-23
     * @param token 用户令牌
     * @param fileId 文件序号
     * @return Result风格的JSON集合对象
     */
    @DeleteMapping("/delete")
    public Result Delete( @RequestParam(value = "Token",defaultValue = "") String token
                         ,@RequestParam(value = "FileId",defaultValue = "") String fileId){
        //判断Token
        Result result = decideToken(token, "删除请求");
        if (result.getCode() != 100){
            return result;
        }
        //判断删除文件的序号是否为空
        if (fileId.equals("")){
            return new Result("删除失败,未知的删除请求","删除请求",101);
        }
        //进行查询条件的设置
        Map<String , Object> condition = new HashMap<>();
        condition.put("file_id",fileId);
        QueryWrapper<Document> queryWrapper = new QueryWrapper<>();
        queryWrapper.allEq(condition);
        //进行查询
        Document document = fileService.getOne(queryWrapper);
        //判断文件的所属人是否正确
        if ( ! document.getFileAffiliation().equals(token)){
            return new Result("删除失败,这不是您的文件","删除请求",101);
        }
        //获取文件的名字
        String fileName = document.getFileUuname() + document.getFilePostfix();
        //获取文件的地址
        File file = new File(fileRootPath + "\\" + fileName);
        //判断文件是否存在
        if (file != null){
            fileService.removeById(document.getFileId());
            file.delete();
            return new Result("删除成功","删除请求",100);
        }else {
            return new Result("删除失败","删除请求",104);
        }
    }

    /**
     * 下载操作工具类
     * @author HCY
     * @since 2020-9-23
     * @param response 后端文件设置
     * @param fileName 文件名字
     * @return Result风格的JSON集合对象
     * @throws IOException 异常
     */
    public Result downloadFile(HttpServletResponse response, String fileName) throws IOException {
        // 发送给客户端的数据
        OutputStream outputStream = response.getOutputStream();
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        // 读取filenameN
        bis = new BufferedInputStream(new FileInputStream(new File(fileRootPath + "\\" + fileName)));
        int i = bis.read(buff);
        while (i != -1) {
            outputStream.write(buff, 0, buff.length);
            outputStream.flush();
            i = bis.read(buff);
        }
        return new Result("下载成功","下载请求",100);
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
