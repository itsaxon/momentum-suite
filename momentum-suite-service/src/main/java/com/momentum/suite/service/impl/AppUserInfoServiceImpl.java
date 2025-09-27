package com.momentum.suite.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.momentum.suite.infrastructure.persistent.mapper.AppUserInfoMapper;
import com.momentum.suite.infrastructure.persistent.po.AppUserInfoPO;
import com.momentum.suite.service.AppUserInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * app用户信息表 服务实现类
 * </p>
 *
 * @author itsaxon
 * @since 2025-09-27
 */
@Service
public class AppUserInfoServiceImpl extends ServiceImpl<AppUserInfoMapper, AppUserInfoPO> implements AppUserInfoService {

}
