package com.hjq.demo.session;

import com.google.gson.Gson;
import com.hjq.demo.model.UserInfoModel;

public class UserManager {

    private static UserInfoModel user;

    /**
     * 清除缓存
     */
    public static void clear() {
        UserManager.user = null;
        ConfigData.removeKey(UserConfig.user);
    }

    /**
     * 获取用户信息
     */
    public static UserInfoModel getUser() {
        if (user == null) {
            if (ConfigData.contains(UserConfig.user)) {
                user = new Gson().fromJson(ConfigData.readString(UserConfig.user), UserInfoModel.class);
            }
        }
        return user;
    }

    /**
     * 登录
     */
    public static void save(UserInfoModel model) {
        ConfigData.save(UserConfig.user, new Gson().toJson(model));
        UserManager.user = model;
    }

}
