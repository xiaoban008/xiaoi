package com.xiaoban.xiaoi.service;

import com.xiaoban.xiaoi.model.TentcentChatResult;

/**
 * 机器人消息处理服务
 *
 * @author xiaoban
 * @version 1.0.0
 * @create 2021/04/13 - 14:03
 */
public interface RobotMsgHandleService {
    /**
     * 处理
     *
     * @param apiResponse api的响应
     * @return {@link String}
     */
    String handle(String apiResponse);

    /**
     * 处理
     *
     * @param result api返回的结果
     * @param tClass t类
     * @return {@link T}
     */
    <T>  T handle(TentcentChatResult<T> result,Class<T> tClass);
}
