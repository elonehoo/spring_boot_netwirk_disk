package com.inet.codebase.controller;


import com.inet.codebase.utlis.Result;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

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

    @PostMapping("/uploads")
    public Result PostUploads(@RequestBody HashMap<String, Object> map){

        return null;
    }
}
