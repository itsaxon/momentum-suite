package com.momentum.suite.infrastructure.adapter;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.momentum.suite.client.common.ResultCode;
import com.momentum.suite.client.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * OpenIM 外部服务适配器
 * <p>
 * 封装了所有与 OpenIM 系统进行交互的逻辑 ，包括认证和 API 调用。
 * 作为一个基础设施组件，它为上层业务(Service)提供清晰、稳定的接口。
 * </p>
 */
@Slf4j
@Component
public class OpenImAdapter {

    private static final String OPEN_IM_API_BASE_URL = "http://127.0.0.1:10002";
    private static final String ADMIN_TOKEN_URL = OPEN_IM_API_BASE_URL + "/auth/get_admin_token";
    private static final String GET_USER_TOKEN_URL = OPEN_IM_API_BASE_URL + "/auth/get_user_token";
    private static final String GET_USERS_ONLINE_TOKEN_URL = OPEN_IM_API_BASE_URL + "/user/get_users_online_token_detail";
    private static final String USER_REGISTER_URL = OPEN_IM_API_BASE_URL + "/user/user_register";
    private static final String ADMIN_SECRET = "openIM123";
    private static final String ADMIN_USER_ID = "imAdmin";

    /**
     * 获取 OpenIM 管理员令牌
     *
     * @return 管理员令牌
     * @throws BusinessException 如果获取失败
     */
    public String getAdminToken( ) {
        String response;
        try {
            response = HttpRequest.post(ADMIN_TOKEN_URL)
                    .header("operationID", String.valueOf(System.currentTimeMillis()))
                    .body(JSONUtil.createObj()
                            .set("secret", ADMIN_SECRET)
                            .set("userID", ADMIN_USER_ID)
                            .toString())
                    .execute()
                    .body();
        } catch (Exception e) {
            log.error("请求 OpenIM 管理员令牌时发生网络异常", e);
            throw new BusinessException(ResultCode.SERVICE_UNAVAILABLE, "外部IM服务网络异常");
        }


        JSONObject jsonObject = JSONUtil.parseObj(response);
        if (jsonObject.getInt("errCode") == 0) {
            String token = jsonObject.getJSONObject("data").getStr("token");
            log.info("成功获取 OpenIM 管理员令牌"+token);
            return token;
        } else {
            log.error("获取 OpenIM 管理员令牌失败，响应：{}", response);
            throw new BusinessException(ResultCode.SERVICE_UNAVAILABLE, "获取外部IM服务管理员令牌失败");
        }
    }


    /**
     * 注册用户到 OpenIM 系统（账号导入）
     *
     * @param adminToken 管理员令牌
     * @param userID     用户ID
     * @param nickname   用户昵称
     * @param faceURL    用户头像URL
     * @return 是否注册成功
     */
    public boolean userRegister(String adminToken, String userID, String nickname, String faceURL) {
        // 构建请求参数
        Map<String, Object> requestBody = new HashMap<>();
        List<Map<String, String>> users = new ArrayList<>();

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userID", userID);
        userInfo.put("nickname", nickname);
        userInfo.put("faceURL", faceURL);

        users.add(userInfo);
        requestBody.put("users", users);

        String response;
        try {
            response = HttpRequest.post(USER_REGISTER_URL)
                    .header("operationID", String.valueOf(System.currentTimeMillis()))
                    .header("token", adminToken)
                    .body(JSONUtil.toJsonStr(requestBody))
                    .execute()
                    .body();
        } catch (Exception e) {
            log.error("请求 OpenIM 用户注册接口时发生网络异常，用户ID: {}", userID, e);
            throw new BusinessException(ResultCode.SERVICE_UNAVAILABLE, "外部IM服务网络异常");
        }

        JSONObject jsonObject = JSONUtil.parseObj(response);
        if (jsonObject.getInt("errCode") == 0) {
            log.info("成功注册用户到 OpenIM 系统，用户ID: {}", userID);
            return true;
        } else {
            log.error("注册用户到 OpenIM 系统失败，用户ID: {}，错误码: {}，错误信息: {}",
                    userID, jsonObject.getInt("errCode"), jsonObject.getStr("errMsg"));
            // 可以根据具体的错误码进行更精细的错误处理
            throw new BusinessException(ResultCode.SERVICE_UNAVAILABLE,
                    "注册外部IM用户失败: " + jsonObject.getStr("errMsg"));
        }
    }


    /**
     * 获取指定用户的 OpenIM 用户令牌
     *
     * @param adminToken 管理员令牌
     * @param platformId 平台ID
     * @param userId     用户ID
     * @return 用户令牌
     */
    public String getUserToken(String adminToken, String platformId, String userId) {
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
            String token = jsonObject.getJSONObject("data").getStr("token");
            log.info("成功为用户 {} 获取 OpenIM 用户令牌", userId);
            return token;
        } else {
            log.error("获取用户 {} 的 OpenIM token失败，响应: {}", userId, response);
            // 在适配器层，可以选择返回 null 或直接抛出异常，这里选择返回 null，让调用方处理
            return null;
        }
    }

    /**
     * 查询用户的在线状态和令牌详情
     *
     * @param adminToken 管理员令牌
     * @param userId     用户ID
     * @return 包含在线信息的 JSONObject，如果失败则返回 null
     */
    public JSONObject getUsersOnlineTokenDetail(String adminToken, String userId) {
        Map<String, Object> body = new HashMap<>();
        body.put("userIDs", Collections.singletonList(userId));

        String response = HttpRequest.post(GET_USERS_ONLINE_TOKEN_URL)
                .header("operationID", String.valueOf(System.currentTimeMillis()))
                .header("token", adminToken)
                .body(JSONUtil.toJsonStr(body))
                .execute()
                .body();

        JSONObject jsonResult = JSONUtil.parseObj(response);
        if (jsonResult.getInt("errCode") != 0) {
            log.error("查询用户在线状态失败，响应: {}", response);
            return null;
        }
        return jsonResult;
    }
}
