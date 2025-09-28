package com.momentum.suite.service.biz;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.momentum.suite.client.exception.BusinessException;
import com.momentum.suite.client.view.request.LoginAppMobileRequest;
import com.momentum.suite.client.view.vo.LoginAppMobileVo;
import com.momentum.suite.infrastructure.adapter.OpenImAdapter;
import com.momentum.suite.infrastructure.persistent.po.AppUserInfoPO;
import com.momentum.suite.infrastructure.security.JwtTokenProvider;
import com.momentum.suite.service.AppUserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 登录应用程序移动业务服务
 *
 * @author itsaxon
 * @version v1.0 2025/09/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginAppMobileBizService {

    private final AppUserInfoService appUserInfoService;

    private final OpenImAdapter openImAdapter;

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 登录
     *
     * @param request 请求
     * @return {@link LoginAppMobileVo }
     */
    public LoginAppMobileVo login(LoginAppMobileRequest request) {
        log.info("开始处理移动端登录请求，手机号：{}，平台ID：{}", request.getMobile(), request.getPlatformId());
        LoginAppMobileVo vo = new LoginAppMobileVo();

        // 验证码校验
        log.debug("验证码校验，输入验证码：{}", request.getCode());
        if (!"666666".equals(request.getCode())) {
            log.warn("验证码校验失败，手机号：{}，输入验证码：{}", request.getMobile(), request.getCode());
            throw new BusinessException("验证码错误");
        }
        log.info("验证码校验通过");

        String mobile = request.getMobile();

        // 根据手机号获取用户信息
        log.info("根据手机号查询用户信息：{}", mobile);
        AppUserInfoPO appUserInfoPO = appUserInfoService.lambdaQuery()
                .eq(AppUserInfoPO::getMobile, mobile)
                .one();

        log.info("获取OpenIM管理员Token");
        String adminToken = openImAdapter.getAdminToken();
        log.debug("成功获取管理员Token");

        // 新用户
        if (ObjectUtil.isNull(appUserInfoPO)) {
            log.info("用户不存在，开始新用户注册流程，手机号：{}", mobile);

            String chatUserId = Convert.toStr(IdUtil.getSnowflakeNextId());
            String nickName = "昵称" + Convert.toStr(IdUtil.getSnowflakeNextId());

            log.info("生成用户信息，ChatUserId：{}，昵称：{}", chatUserId, nickName);

            // 注册im用户
            log.info("开始注册OpenIM用户，ChatUserId：{}", chatUserId);
            openImAdapter.userRegister(adminToken, chatUserId, nickName,
                    "http://e.hiphotos.baidu.com/image/pic/item/a1ec08fa513d2697e542494057fbb2fb4316d81e.jpg");
            log.info("OpenIM用户注册成功");

            // 保存用户信息
            log.info("开始保存用户信息到数据库");
            AppUserInfoPO appUserInfoPOSave = new AppUserInfoPO();
            appUserInfoPOSave
                    .setChatUserId(chatUserId)
                    .setMobile(request.getMobile())
                    .setNickname(nickName)
                    .setAvatar("http://e.hiphotos.baidu.com/image/pic/item/a1ec08fa513d2697e542494057fbb2fb4316d81e.jpg")
                    .setIntro("1")
                    .setEnable(false)
                    .setDelFlag(false)
                    .setCreateId(0L)
                    .setCreateName("")
                    .setCreateTime(LocalDateTime.now())
                    .setUpdateId(0L)
                    .setUpdateName("")
                    .setUpdateTime(LocalDateTime.now());

            appUserInfoService.save(appUserInfoPOSave);
            log.info("用户信息保存成功，用户ID：{}", chatUserId);

            // 获取im用户token返回
            log.info("获取用户Token，平台ID：{}，ChatUserId：{}", request.getPlatformId(), chatUserId);
            String userToken = openImAdapter.getUserToken(adminToken, request.getPlatformId(), chatUserId);
            log.info("用户Token获取成功");

            Long userId = appUserInfoPOSave.getId();

            String appToken = jwtTokenProvider.generateToken(userId, nickName, "app");

            vo.setToken(userToken);
            vo.setUserID(chatUserId);
            // TODO: 获取appToken
            vo.setAppToken(appToken);

            log.info("新用户登录流程完成，手机号：{}，用户ID：{}", mobile, chatUserId);
            return vo;

        } else {
            // 老用户
            log.info("用户已存在，开始老用户登录流程，手机号：{}，用户ID：{}", mobile, appUserInfoPO.getChatUserId());

            String chatUserId = appUserInfoPO.getChatUserId();
            // 获取im用户token返回
            log.info("获取老用户Token，平台ID：{}，ChatUserId：{}", request.getPlatformId(), chatUserId);
            String userToken = openImAdapter.getUserToken(adminToken, request.getPlatformId(), chatUserId);
            log.info("老用户Token获取成功");

            String appToken = jwtTokenProvider.generateToken(appUserInfoPO.getId(), appUserInfoPO.getNickname(), "app");

            vo.setToken(userToken);
            vo.setUserID(chatUserId);
            // TODO: 获取appToken
            vo.setAppToken(appToken);

            log.info("老用户登录流程完成，手机号：{}，用户ID：{}", mobile, chatUserId);
            return vo;
        }
    }
}