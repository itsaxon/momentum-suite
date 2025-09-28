package com.momentum.suite.service.impl;

import com.momentum.suite.infrastructure.persistent.po.AdminUserInfoPO;
import com.momentum.suite.infrastructure.persistent.mapper.AdminUserInfoMapper;
import com.momentum.suite.service.AdminUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台管理系统用户信息表 服务实现类
 * </p>
 *
 * @author itsaxon
 * @since 2025-09-28
 */
@Service
public class AdminUserInfoServiceImpl extends ServiceImpl<AdminUserInfoMapper, AdminUserInfoPO> implements AdminUserInfoService {

}
