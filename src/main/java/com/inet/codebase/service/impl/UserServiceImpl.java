package com.inet.codebase.service.impl;

import com.inet.codebase.entity.User;
import com.inet.codebase.mapper.UserMapper;
import com.inet.codebase.service.UserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
