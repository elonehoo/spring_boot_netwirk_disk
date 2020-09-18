package com.inet.codebase.service.impl;

import com.inet.codebase.entity.Web;
import com.inet.codebase.mapper.WebMapper;
import com.inet.codebase.service.WebService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author HCY
 * @since 2020-09-18
 */
@Service
public class WebServiceImpl extends ServiceImpl<WebMapper, Web> implements WebService {

}
