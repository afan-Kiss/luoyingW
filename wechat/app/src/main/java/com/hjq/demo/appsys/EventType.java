package com.hjq.demo.appsys;

/**
 * @author Created by zhangrenwei QQ1282447073
 * @date 2019-11-18.
 * 简述: <EventBus类型>
 */
public enum EventType {
    Event_ToMessage,//跳转到消息列表
    Event_RefershMessage,//更新消息列表
    Event_MessageCode,//请求消息列表之后传递请求状态码state
    Event_Token_Update,//更新令牌信息
}
