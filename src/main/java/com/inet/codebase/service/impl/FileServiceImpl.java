package com.inet.codebase.service.impl;

import com.inet.codebase.entity.File;
import com.inet.codebase.mapper.FileMapper;
import com.inet.codebase.service.FileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author HCY
 * @since 2020-09-20
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

}
