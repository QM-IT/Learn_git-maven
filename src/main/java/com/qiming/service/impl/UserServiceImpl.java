package com.qiming.service.impl;

import com.qiming.mapper.UserMapper;
import com.qiming.pojo.entity.User;
import com.qiming.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author QM
 * @since 2024-11-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
