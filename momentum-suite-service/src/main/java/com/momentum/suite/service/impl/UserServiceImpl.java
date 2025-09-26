package com.momentum.suite.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.momentum.suite.client.common.ResultCode;
import com.momentum.suite.client.dto.LoginSuccessVO;
import com.momentum.suite.client.dto.MobileLoginRequest;
import com.momentum.suite.client.exception.BusinessException;
import com.momentum.suite.client.facade.UserFacade;
import com.momentum.suite.infrastructure.persistent.mapper.UserMapper;
import com.momentum.suite.infrastructure.persistent.po.UserPO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 用户服务门面接口的实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPO> implements UserFacade {

    // 外部服务地址常量
    private static final String OPEN_IM_API_BASE_URL = "http://127.0.0.1:10002";
    private static final String ADMIN_TOKEN_URL = OPEN_IM_API_BASE_URL + "/auth/get_admin_token";
    private static final String GET_USER_TOKEN_URL = OPEN_IM_API_BASE_URL + "/auth/get_user_token";
    private static final String GET_USERS_ONLINE_TOKEN_URL = OPEN_IM_API_BASE_URL + "/user/get_users_online_token_detail";

    // 外部服务认证常量
    private static final String ADMIN_SECRET = "openIM123";
    private static final String ADMIN_USER_ID = "imAdmin";

    // ServiceImpl 会自动注入 UserMapper ，我们无需再声明
    // private final UserMapper userMapper;

    @Override
    public LoginSuccessVO loginByMobile(MobileLoginRequest request) {
        log.info("开始处理手机号登录请求，手机号: {}", request.getMobile());

        // 遵照您的要求，在业务逻辑开始前，先查询并打印 t_user 表的数据行数
        Long userCount = this.baseMapper.selectCount(null);
        log.info("当前 t_user 表中的数据行数: {}", userCount);

        // =================================================================
        //                  ↓↓↓ 开始移植您之前的登录逻辑 ↓↓↓
        // =================================================================

        // 第一步：获取管理员token
        String adminToken = getAdminToken();
        if (StrUtil.isBlank(adminToken)) {
            // 如果获取失败，抛出业务异常，由 GlobalExceptionHandler 统一处理
            throw new BusinessException(ResultCode.SERVICE_UNAVAILABLE, "获取外部服务管理员令牌失败");
        }

        // 第二步：使用管理员token查询用户在线状态
        // 注意：您之前的逻辑是写死查询 "2507518963"，这里暂时保留
        String onlineStatusResult = getUsersOnlineTokenDetail(adminToken, "2507518963");

        // 第三步：解析响应，并根据情况获取最终的用户token
        JSONObject jsonResult = JSONUtil.parseObj(onlineStatusResult);
        if (jsonResult.getInt("errCode") != 0) {
            log.error("查询用户在线状态失败，响应: {}", onlineStatusResult);
            throw new BusinessException(ResultCode.FAILURE, "查询用户在线状态失败");
        }

        JSONArray dataArray = jsonResult.getJSONArray("data");

        // 场景一：在线状态接口未返回任何数据，直接为该用户获取新token
        if (dataArray == null || dataArray.isEmpty()) {
            log.warn("用户在线状态为空，直接获取新token...");
            String userToken = getUserToken(adminToken, request.getPlatformId(), "2507518963");
            if (userToken != null) {
                return new LoginSuccessVO().setToken(userToken).setUserID("2507518963");
            }
        } else {
            // 场景二：在线状态接口返回了数据，需要遍历查找匹配平台的token
            JSONObject userData = dataArray.getJSONObject(0); // 假设只查了一个用户，取第一个
            String userID = userData.getStr("userID");
            JSONArray singlePlatformTokenArray = userData.getJSONArray("singlePlatformToken");

            if (singlePlatformTokenArray != null) {
                for (Object platformTokenObj : singlePlatformTokenArray) {
                    JSONObject tokenObj = (JSONObject) platformTokenObj;
                    // 检查平台ID是否与请求中的平台ID匹配
                    if (Objects.equals(tokenObj.getInt("platformID").toString(), request.getPlatformId())) {
                        JSONArray tokenArray = tokenObj.getJSONArray("token");
                        if (tokenArray != null && !tokenArray.isEmpty()) {
                            String token = tokenArray.getStr(0); // 获取第一个token
                            log.info("成功找到匹配平台的在线token，用户ID: {}, 平台ID: {}", userID, request.getPlatformId());
                            return new LoginSuccessVO().setToken(token).setUserID(userID);
                        }
                    }
                }
            }

            // 如果遍历完所有在线平台都没有找到匹配的，或者 singlePlatformTokenArray 为空，则获取新token
            log.warn("未找到匹配平台的在线token，为用户获取新token...");
            String userToken = getUserToken(adminToken, request.getPlatformId(), userID);
            if (userToken != null) {
                return new LoginSuccessVO().setToken(userToken).setUserID(userID);
            }
        }

        // 如果所有尝试都失败，则抛出最终的失败异常
        throw new BusinessException(ResultCode.FAILURE, "登录失败，无法获取有效的用户令牌");
    }

    /**
     * 获取管理员令牌 (辅助方法)
     */
    @SneakyThrows
    private String getAdminToken() {
        String response = HttpRequest.post(ADMIN_TOKEN_URL)
                .header("operationID", String.valueOf(System.currentTimeMillis()))
                .body(JSONUtil.createObj()
                        .set("secret", ADMIN_SECRET)
                        .set("userID", ADMIN_USER_ID)
                        .toString())
                .execute()
                .body();

        JSONObject jsonObject = JSONUtil.parseObj(response);
        if (jsonObject.getInt("errCode") == 0) {
            return jsonObject.getJSONObject("data").getStr("token");
        } else {
            log.error("获取管理员令牌失败，响应：{}", response);
            return null;
        }
    }

    /**
     * 获取用户token (辅助方法)
     */
    private String getUserToken(String adminToken, String platformId, String userId) {
        Map<String, Object> body = new HashMap<>();
        body.put("platformID", Integer.parseInt(platformId));
        body.put("userID", userId);

        String response = HttpRequest.post(GET_USER_TOKEN_URL)
                .header("operationID", String.valueOf(System.currentTimeMillis()))
                .header("token", adminToken)
                .body(JSONUtil.toJsonStr(body))
                .execute()
                .body();

        JSONObject jsonObject = JSONUtil.parseObj(response);
        if (jsonObject.getInt("errCode") == 0) {
            return jsonObject.getJSONObject("data").getStr("token");
        } else {
            log.error("获取用户 {} 的token失败，响应: {}", userId, response);
            return null;
        }
    }

    /**
     * 查询用户在线token详情 (辅助方法)
     */
    private String getUsersOnlineTokenDetail(String adminToken, String userId) {
        Map<String, Object> body = new HashMap<>();
        body.put("userIDs", Collections.singletonList(userId));

        return HttpRequest.post(GET_USERS_ONLINE_TOKEN_URL)
                .header("operationID", String.valueOf(System.currentTimeMillis()))
                .header("token", adminToken)
                .body(JSONUtil.toJsonStr(body))
                .execute()
                .body();
    }
}
