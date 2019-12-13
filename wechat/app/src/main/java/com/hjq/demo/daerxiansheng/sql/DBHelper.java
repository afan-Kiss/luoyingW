package com.hjq.demo.daerxiansheng.sql;


import android.util.Log;
import android.widget.Toast;

import com.hjq.demo.common.MyApplication;
import com.hjq.demo.greenDao.FrendsEntityDao;
import com.hjq.demo.greenDao.FrendsMessageEntityDao;
import com.hjq.demo.greenDao.GroupEntityDao;
import com.hjq.demo.greenDao.GroupMessageEntityDao;
import com.hjq.demo.greenDao.GroupUserEntityDao;
import com.hjq.demo.greenDao.MessageListEntityDao;
import com.hjq.demo.greenDao.TokenEntityDao;
import com.hjq.demo.greenDao.VirtualAppEntityDao;
import com.hjq.demo.session.UserManager;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by zhangrenwei QQ1282447073
 * @date 2019-11-19.
 * 简述: <数据库操作增删改查工具类>
 */
public class DBHelper {

    //查询好友消息表数据
    //查询好友消息表数据
    //查询好友消息表数据
    //查询好友消息表数据
    //查询好友消息表数据

    /**
     * 添加数据，如果有重复则覆盖
     *
     * @param entity
     */
    public static void insertMessageReplace(FrendsMessageEntity entity) {
        MyApplication.getDaoSession().getFrendsMessageEntityDao().insertOrReplace(entity);
    }

    /**
     * 添加数据
     *
     * @param entity
     */
    public static void insertMessage(FrendsMessageEntity entity) {
        MyApplication.getDaoSession().getFrendsMessageEntityDao().insert(entity);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public static void deleteMessage(long id) {
        MyApplication.getDaoSession().getFrendsMessageEntityDao().deleteByKey(id);
    }


    /**
     * 更新数据
     *
     * @param entity
     */
    public static void updateMessage(FrendsMessageEntity entity) {
        MyApplication.getDaoSession().getFrendsMessageEntityDao().update(entity);
    }

    /**
     * 查询全部数据
     */
    public static List<FrendsMessageEntity> queryFrendMessage() {
        return MyApplication.getDaoSession().getFrendsMessageEntityDao().loadAll();
    }


    /**
     * 分页查询条件为和目标用户的聊天记录
     * DESC: 代表降序
     * ASC: 代表升序
     *
     * @param card 代表好友card
     * @return
     */
    public static List<FrendsMessageEntity> queryMessageAsc(int page, String card) {
        return MyApplication.getDaoSession().getFrendsMessageEntityDao().queryBuilder().where(FrendsMessageEntityDao.Properties.ToUid.eq(card), FrendsMessageEntityDao.Properties.Card.eq(UserManager.getUser().getCard())).offset(page * 20).limit(20).orderAsc().list();
    }

    /**
     * 分页查询条件为目标用户的聊天记录
     * DESC: 代表降序
     *
     * @param card 代表好友card
     * @return
     */
    public static List<FrendsMessageEntity> queryMessageDesc(int page, String card) {
        return MyApplication.getDaoSession().getFrendsMessageEntityDao().queryBuilder().where(FrendsMessageEntityDao.Properties.ToUid.eq(card), FrendsMessageEntityDao.Properties.Card.eq(UserManager.getUser().getCard())).offset(page * 20).limit(20).orderDesc().list();
    }


    /**
     * 目标用户的所有聊天记录
     * DESC: 代表降序
     * ASC: 代表升序
     *
     * @param card 代表好友card
     * @return
     */
    public static List<FrendsMessageEntity> queryMessageAllAsc(String card) {
        return MyApplication.getDaoSession().getFrendsMessageEntityDao().queryBuilder().where(FrendsMessageEntityDao.Properties.ToUid.eq(card), FrendsMessageEntityDao.Properties.Card.eq(UserManager.getUser().getCard())).orderAsc().list();
    }

    /**
     * 目标用户的所有聊天记录总条数
     *
     * @param card 代表好友card
     * @return
     */
    public static long queryMessageAll(String card) {
        return MyApplication.getDaoSession().getFrendsMessageEntityDao().queryBuilder().where(FrendsMessageEntityDao.Properties.ToUid.eq(card), FrendsMessageEntityDao.Properties.Card.eq(UserManager.getUser().getCard())).count();
    }


    /**
     * 目标用户的所有聊天记录
     * DESC: 代表降序
     *
     * @param card 代表好友card
     * @return
     */
    public static List<FrendsMessageEntity> queryMessageAllDesc(String card) {
        return MyApplication.getDaoSession().getFrendsMessageEntityDao().queryBuilder().where(FrendsMessageEntityDao.Properties.ToUid.eq(card), FrendsMessageEntityDao.Properties.Card.eq(UserManager.getUser().getCard())).orderDesc().list();
    }


    /**
     * 查询条件为文本类型的聊天内容时候包含用户输入的搜索内容
     * DESC: 代表降序
     * ASC: 代表升序
     * card 发送用户的标识
     *
     * @return
     */
    public static List<FrendsMessageEntity> frendMessageContentAsc(String card, String content) {
        return MyApplication.getDaoSession().getFrendsMessageEntityDao().queryBuilder().where(FrendsMessageEntityDao.Properties.ToUid.eq(card), FrendsMessageEntityDao.Properties.Content.like("%" + content + "%"), FrendsMessageEntityDao.Properties.ContentType.eq(1), FrendsMessageEntityDao.Properties.Card.eq(UserManager.getUser().getCard())).orderAsc().list();
    }

    /**
     * 查询条件为文本类型的聊天内容时候包含用户输入的搜索内容
     * DESC: 代表降序
     * ASC: 代表升序
     *
     * @return
     */
    public static List<FrendsMessageEntity> frendMessageContentDesc(String card, String content) {
        return MyApplication.getDaoSession().getFrendsMessageEntityDao().queryBuilder().where(FrendsMessageEntityDao.Properties.Card.eq(card), FrendsMessageEntityDao.Properties.Content.like("%" + content + "%"), FrendsMessageEntityDao.Properties.ContentType.eq(1), FrendsMessageEntityDao.Properties.Card.eq(UserManager.getUser().getCard())).orderDesc().list();
    }

    /**
     * 分页查询当前用户和好友聊天记录20条每页
     * Desc 降序
     *
     * @param card 发送消息用户的唯一标识
     * @param page
     * @return
     */
    public static List<FrendsMessageEntity> getFrendMessagePageDesc(int page, String card) {
        return MyApplication.getDaoSession().getFrendsMessageEntityDao().queryBuilder()
                .where(FrendsMessageEntityDao.Properties.Card.eq(UserManager.getUser().getCard())).offset(page * 20).limit(20).orderDesc().list();
    }

    /**
     * 分页查询当前用户和好友聊天记录20条每页
     * Asc 降序
     *
     * @param card 发送消息用户的唯一标识
     * @param page
     * @return
     */
    public static List<FrendsMessageEntity> getFrendMessagePageAsc(int page, String card) {
        return MyApplication.getDaoSession().getFrendsMessageEntityDao().queryBuilder()
                .where(FrendsMessageEntityDao.Properties.Card.eq(UserManager.getUser().getCard()))
                .whereOr(FrendsMessageEntityDao.Properties.ToUid.eq(card), FrendsMessageEntityDao.Properties.ToUid.eq(UserManager.getUser().getCard()))
                .offset(page * 20)
                .limit(20)
                .orderAsc()
                .list();
    }

//    public static List<FrendsMessageEntity> getFrendMessagePageIdDesc(int id, int page, String card) {
//        return MyApplication.getDaoSession().getFrendsMessageEntityDao().queryBuilder().where(FrendsMessageEntityDao.Properties.Card.eq(UserManager.getUser().getCard()))
//                .whereOr(FrendsMessageEntityDao.Properties.ToUid.eq(card), FrendsMessageEntityDao.Properties.ToUid.eq(UserManager.getUser().getCard()))
//                .offset(page * 20)
//                .limit(20)
//                .orderAsc().list();
//    }

    //查询好友表数据
    //查询好友表数据
    //查询好友表数据
    //查询好友表数据

    /**
     * 添加数据，如果有重复则覆盖 （根据主键来检测是否已经存在）
     *
     * @param entity
     */
    public static void insertFrendReplace(FrendsEntity entity) {
        MyApplication.getDaoSession().getFrendsEntityDao().insertOrReplace(entity);
    }

    /**
     * 添加数据
     *
     * @param entity
     */
    public static void insertFrend(FrendsEntity entity) {
        MyApplication.getDaoSession().getFrendsEntityDao().insert(entity);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public static void deleteFrend(long id) {
        MyApplication.getDaoSession().getFrendsEntityDao().deleteByKey(id);
    }

    /**
     * 更新数据
     *
     * @param entity
     */
    public static void updateFrend(FrendsEntity entity) {
        MyApplication.getDaoSession().getFrendsEntityDao().update(entity);
    }

    /**
     * 查询全部数据
     */
    public static List<FrendsEntity> queryFrend() {
        return MyApplication.getDaoSession().getFrendsEntityDao().loadAll();
    }

    /**
     * 查询当前用户所有的好友信息
     *
     * @return
     */
    public static List<FrendsEntity> queryFrendCard() {
        return MyApplication.getDaoSession().getFrendsEntityDao().queryBuilder().where(FrendsEntityDao.Properties.UserCard.eq(UserManager.getUser().getCard())).list();
    }

    /**
     * 模糊查询名称中包含某一个文字的用户
     *
     * @param name
     * @return
     */
    public static List<FrendsEntity> getUserEntityName(String name) {
        return MyApplication.getDaoSession().getFrendsEntityDao().queryBuilder().where(FrendsEntityDao.Properties.Nickname.like("%" + name + "%"), FrendsEntityDao.Properties.UserCard.eq(UserManager.getUser().getCard())).list();
    }

    /**
     * 查询某一个用户
     *
     * @param card 用户唯一标识
     * @return
     */
    public static List<FrendsEntity> getUserEntityCard(String card) {
        return MyApplication.getDaoSession().getFrendsEntityDao().queryBuilder().where(FrendsEntityDao.Properties.Card.eq(card), FrendsEntityDao.Properties.UserCard.eq(UserManager.getUser().getCard())).list();
    }

    /**
     * 根据用户标识去查询数据库中是否有用户
     *
     * @param card
     * @return
     */
    public static boolean getUser(String card) {
        List<FrendsEntity> entityList = MyApplication.getDaoSession().getFrendsEntityDao().queryBuilder().where(FrendsEntityDao.Properties.Card.eq(card)).list();
        if (entityList != null && entityList.size() > 0) {
            return true;
        }
        return false;
    }

//    /**
//     * 查询当前用户下的所有的消息列表
//     *
//     * @return
//     */
//    public static List<List<FrendsMessageEntity>> getUserMessage() {
//        List<FrendsEntity> userEntity = queryFrendCard();
//        List<List<FrendsMessageEntity>> userMessageList = new ArrayList<>();
//        if (userEntity != null && userEntity.size() > 0) {
//            for (FrendsEntity frendsEntity : userEntity) {
//                List<FrendsMessageEntity> frendsMessageEntities = queryMessageAsc(frendsEntity.card);
//                if (frendsMessageEntities != null && frendsMessageEntities.size() > 0) {
//                    userMessageList.add(frendsMessageEntities);
//                }
//            }
//        }
//        return userMessageList;
//    }

    /**
     * 查询当前用户好友聊天消息中包含某个字段的聊天内容有多少条
     * 消息按照升序返回
     *
     * @param content 用户搜索内容
     * @return 返回包含搜索内容的用户集合
     */
    public static List<List<FrendsMessageEntity>> getFrendMessageListAsc(String content) {
        List<FrendsEntity> frendsEntities = queryFrendCard();//查询所有好友
        List<List<FrendsMessageEntity>> frendMessage = new ArrayList<>();
        if (frendsEntities != null && frendsEntities.size() > 0) {
            for (FrendsEntity entity : frendsEntities) {
                List<FrendsMessageEntity> list = frendMessageContentAsc(entity.card, content);
                if (list != null && list.size() > 0) {
                    frendMessage.add(list);//查询消息列表中发送给用户的内容包含某个值
                }
            }
        }
        return frendMessage;
    }

    /**
     * 查询当前用户下好友聊天消息中包含某个字段的聊天内容有多少条
     * 消息按照降序返回
     *
     * @param content 用户搜索内容
     * @return 返回包含搜索内容的用户集合
     */
    public static List<List<FrendsMessageEntity>> getFrendMessageListDesc(String content) {
        List<FrendsEntity> frendsEntities = queryFrendCard();//查询所有好友
        List<List<FrendsMessageEntity>> frendMessage = new ArrayList<>();
        if (frendsEntities != null && frendsEntities.size() > 0) {
            for (FrendsEntity entity : frendsEntities) {
                List<FrendsMessageEntity> list = frendMessageContentDesc(entity.card, content);
                if (list != null && list.size() > 0) {
                    frendMessage.add(list);//查询消息列表中发送给用户的内容包含某个值
                }
            }
        }
        return frendMessage;
    }

    /**
     * 查询当前用户下群组聊天记录中是否包含用户搜索的内容返回群组的集合
     * Asc升序
     *
     * @param content 用户搜索内容
     * @return 包含用户搜索内容的群组的集合
     */
    public static List<List<GroupMessageEntity>> getGroupMessageListAsc(String content) {
        List<GroupEntity> groupEntityList = queryGroupCard();//获取所有列表
        List<List<GroupMessageEntity>> groupMessageEntities = new ArrayList<>();
        if (groupEntityList != null && groupEntityList.size() > 0) {
            for (GroupEntity item : groupEntityList) {
                List<GroupMessageEntity> list = groupMessageContentAsc(item.card, content);
                if (list != null && list.size() > 0) {
                    groupMessageEntities.add(list);
                }
            }
        }
        return groupMessageEntities;

    }

    /**
     * 查询当前用户下群组聊天记录中是否包含用户搜索的内容返回群组的集合
     * Desc降序
     *
     * @param content 用户搜索内容
     * @return 包含用户搜索内容的群组的集合
     */
    public static List<List<GroupMessageEntity>> getGroupMessageListDesc(String content) {
        List<GroupEntity> groupEntityList = queryGroupCard();//获取所有列表
        List<List<GroupMessageEntity>> groupMessageEntities = new ArrayList<>();
        if (groupEntityList != null && groupEntityList.size() > 0) {
            for (GroupEntity item : groupEntityList) {
                //查询某一个群组中的文本类型的聊天内容包含用户搜索的内容
                List<GroupMessageEntity> list = groupMessageContentDesc(item.card, content);
                if (list != null && list.size() > 0) {
                    groupMessageEntities.add(list);
                }
            }
        }
        return groupMessageEntities;

    }


    //查询群组消息表数据
    //查询群组消息表数据
    //查询群组消息表数据
    //查询群组消息表数据
    //查询群组消息表数据

    /**
     * 添加数据，如果有重复则覆盖
     *
     * @param entity
     */
    public static void insertMessageGroupReplace(GroupMessageEntity entity) {
        MyApplication.getDaoSession().getGroupMessageEntityDao().insertOrReplace(entity);
    }

    /**
     * 添加数据
     *
     * @param entity
     */
    public static void insertMessageGroup(GroupMessageEntity entity) {
        MyApplication.getDaoSession().getGroupMessageEntityDao().insert(entity);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public static void deleteMessageGroup(long id) {
        MyApplication.getDaoSession().getGroupMessageEntityDao().deleteByKey(id);
    }

    /**
     * 更新数据
     *
     * @param entity
     */
    public static void updateMessageGroup(GroupMessageEntity entity) {
        MyApplication.getDaoSession().getGroupMessageEntityDao().update(entity);
    }

    /**
     * 查询全部数据
     */
    public static List<GroupMessageEntity> queryMessageGroup() {
        return MyApplication.getDaoSession().getGroupMessageEntityDao().loadAll();
    }

//    /**
//     * 查询本地数据库中是否存在该群组消息
//     * @param groupCard
//     * @return
//     */
//    public static List<GroupMessageEntity> isGroupMessage(String groupCard){
//        return MyApplication.getDaoSession().getGroupMessageEntityDao().queryBuilder().where(GroupMessageEntityDao.Properties.GroupCard.eq(groupCard)).list();
//    }

    /**
     * 查询某一个群组文本信息包含用户搜索的字段的聊天内容
     * Asc升序
     *
     * @param groupCard
     * @param content
     * @return
     */
    public static List<GroupMessageEntity> groupMessageContentAsc(String groupCard, String content) {
        return MyApplication.getDaoSession().getGroupMessageEntityDao().queryBuilder().where(GroupMessageEntityDao.Properties.GroupCard.eq(groupCard), GroupMessageEntityDao.Properties.Content.like("%" + content + "%"), GroupMessageEntityDao.Properties.ContentType.eq(1), GroupMessageEntityDao.Properties.UserCard.eq(UserManager.getUser().getCard())).orderAsc().list();
    }

    /**
     * 查询某一个群组文本信息包含用户搜索的字段的聊天内容
     * Desc降序
     *
     * @param groupCard
     * @param content
     * @return
     */
    public static List<GroupMessageEntity> groupMessageContentDesc(String groupCard, String content) {
        return MyApplication.getDaoSession().getGroupMessageEntityDao().queryBuilder().where(GroupMessageEntityDao.Properties.GroupCard.eq(groupCard), GroupMessageEntityDao.Properties.Content.like("%" + content + "%"), GroupMessageEntityDao.Properties.ContentType.eq(1), GroupMessageEntityDao.Properties.UserCard.eq(UserManager.getUser().getCard())).orderDesc().list();
    }

    /**
     * 分页获取群聊消息记录
     * Asc 升序
     *
     * @param page
     * @param card 群聊card
     * @return
     */
    public static List<GroupMessageEntity> groupMessageAsc(int page, String card) {
        return MyApplication.getDaoSession().getGroupMessageEntityDao().queryBuilder().where(GroupMessageEntityDao.Properties.UserCard.eq(UserManager.getUser().getCard()), GroupMessageEntityDao.Properties.GroupCard.eq(card)).offset(page * 20).limit(20).orderAsc().list();
    }

    /**
     * 分页获取群聊消息记录
     * Desc 升序
     *
     * @param page
     * @param card 群聊card
     * @return
     */
    public static List<GroupMessageEntity> groupMessageDesc(int page, String card) {
        return MyApplication.getDaoSession().getGroupMessageEntityDao().queryBuilder().where(GroupMessageEntityDao.Properties.UserCard.eq(UserManager.getUser().getCard()), GroupMessageEntityDao.Properties.GroupCard.eq(card)).offset(page * 20).limit(20).orderDesc().list();
    }


    /**
     * 获取指定群聊所有消息记录
     * Asc 升序
     *
     * @param card 群聊card
     * @return
     */
    public static List<GroupMessageEntity> groupMessageAllAsc(String card) {
        return MyApplication.getDaoSession().getGroupMessageEntityDao().queryBuilder().where(GroupMessageEntityDao.Properties.UserCard.eq(UserManager.getUser().getCard()), GroupMessageEntityDao.Properties.GroupCard.eq(card)).orderAsc().list();
    }

    /**
     * 获取指定群聊所有消息记录
     * Desc 升序
     *
     * @param card 群聊card
     * @return
     */
    public static List<GroupMessageEntity> groupMessageAllDesc(String card) {
        return MyApplication.getDaoSession().getGroupMessageEntityDao().queryBuilder().where(GroupMessageEntityDao.Properties.UserCard.eq(UserManager.getUser().getCard()), GroupMessageEntityDao.Properties.GroupCard.eq(card)).orderDesc().list();
    }

    /**
     * 查询当前用户下指定群组中 id前后30条聊天记录
     * Asc 升序
     *
     * @param id 聊天记录id
     * @return
     */
    public static List<GroupMessageEntity> groupMessageContentAsc(int id, String groupCard) {
        return MyApplication.getDaoSession().getGroupMessageEntityDao().queryBuilder().where(GroupMessageEntityDao.Properties.UserCard.eq(UserManager.getUser().getCard()), GroupMessageEntityDao.Properties.GroupCard.eq(groupCard), GroupMessageEntityDao.Properties.Id.ge(id), GroupMessageEntityDao.Properties.Id.lt(id)).limit(30).orderAsc().list();
    }

    /**
     * 查询当前用户下指定群组中 id前后30条聊天记录
     * Desc 升序
     *
     * @param id 聊天记录id
     * @return
     */
    public static List<GroupMessageEntity> groupMessageContentDesc(int id, String groupCard) {
        return MyApplication.getDaoSession().getGroupMessageEntityDao().queryBuilder().where(GroupMessageEntityDao.Properties.UserCard.eq(UserManager.getUser().getCard()), GroupMessageEntityDao.Properties.GroupCard.eq(groupCard), GroupMessageEntityDao.Properties.Id.ge(id), GroupMessageEntityDao.Properties.Id.lt(id)).limit(30).orderDesc().list();
    }


    //查询群组表数据
    //查询群组表数据
    //查询群组表数据
    //查询群组表数据

    /**
     * 添加数据，如果有重复则覆盖
     *
     * @param entity
     */
    public static void insertGroupReplace(GroupEntity entity) {
        MyApplication.getDaoSession().getGroupEntityDao().insertOrReplace(entity);
    }

    /**
     * 添加数据
     *
     * @param entity
     */
    public static void insertGroup(GroupEntity entity) {
        MyApplication.getDaoSession().getGroupEntityDao().insert(entity);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public static void deleteGroup(long id) {
        MyApplication.getDaoSession().getGroupEntityDao().deleteByKey(id);
    }

    /**
     * 更新数据
     *
     * @param entity
     */
    public static void updateGroup(GroupEntity entity) {
        MyApplication.getDaoSession().getGroupEntityDao().update(entity);
    }

    /**
     * 查询全部数据
     */
    public static List<GroupEntity> queryGroup() {
        return MyApplication.getDaoSession().getGroupEntityDao().loadAll();
    }

    /**
     * 查询群组中是否有这个群组
     *
     * @param card
     * @return
     */
    public static List<GroupEntity> isGroup(String card) {
        return MyApplication.getDaoSession().getGroupEntityDao().queryBuilder().where(GroupEntityDao.Properties.Card.eq(card), GroupEntityDao.Properties.UserCard.eq(UserManager.getUser().getCard())).list();
    }

    /**
     * 查询当前用户的所有群组列表
     *
     * @return
     */
    public static List<GroupEntity> queryGroupCard() {
        return MyApplication.getDaoSession().getGroupEntityDao().queryBuilder().where(GroupEntityDao.Properties.UserCard.eq(UserManager.getUser().getCard())).list();
    }

    /**
     * 模糊查询当前用户群组名称中包含某一个文字的群组集合
     *
     * @param name
     * @return
     */
    public static List<GroupEntity> getGroupEntity(String name) {
        return MyApplication.getDaoSession().getGroupEntityDao().queryBuilder().where(GroupEntityDao.Properties.GroupName.like("%" + name + "%"), GroupEntityDao.Properties.UserCard.eq(UserManager.getUser().getCard())).list();
    }


    //查询群组成员表数据
    //查询群组成员表数据
    //查询群组成员表数据
    //查询群组成员表数据
    //查询群组成员表数据

    /**
     * 添加数据，如果有重复则覆盖
     *
     * @param entity
     */
    public static void insertGroupUserReplace(GroupUserEntity entity) {
        MyApplication.getDaoSession().getGroupUserEntityDao().insertOrReplace(entity);
    }

    /**
     * 添加数据
     *
     * @param entity
     */
    public static void insertGroupUser(GroupUserEntity entity) {
        MyApplication.getDaoSession().getGroupUserEntityDao().insert(entity);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public static void deleteGroupUser(long id) {
        MyApplication.getDaoSession().getGroupUserEntityDao().deleteByKey(id);
    }

    /**
     * 更新数据
     *
     * @param entity
     */
    public static void updateGroupUser(GroupUserEntity entity) {
        MyApplication.getDaoSession().getGroupUserEntityDao().update(entity);
    }

    /**
     * 查询全部数据
     */
    public static List<GroupUserEntity> queryGroupUser() {
        return MyApplication.getDaoSession().getGroupUserEntityDao().loadAll();
    }

    /**
     * 查询当前用户是否在数据库群组用户表中存在
     *
     * @param card
     * @return
     */
    public static List<GroupUserEntity> isGroupUser(String card) {
        return MyApplication.getDaoSession().getGroupUserEntityDao().queryBuilder().where(GroupUserEntityDao.Properties.Card.eq(card)).list();
    }

    //查询虚拟App表数据
    //查询虚拟App表数据
    //查询虚拟App表数据
    //查询虚拟App表数据

    /**
     * 添加数据，如果有重复则覆盖
     *
     * @param entity
     */
    public static void insertVirtualAppReplace(VirtualAppEntity entity) {
        MyApplication.getDaoSession().getVirtualAppEntityDao().insertOrReplace(entity);
    }

    /**
     * 添加数据
     *
     * @param entity
     */
    public static void insertVirtualApp(VirtualAppEntity entity) {
        MyApplication.getDaoSession().getVirtualAppEntityDao().insert(entity);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public static void deleteVirtualApp(long id) {
        MyApplication.getDaoSession().getVirtualAppEntityDao().deleteByKey(id);
    }

    /**
     * 更新数据
     *
     * @param entity
     */
    public static void updateVirtualApp(VirtualAppEntity entity) {
        MyApplication.getDaoSession().getVirtualAppEntityDao().update(entity);
    }

    /**
     * 查询全部数据
     */
    public static List<VirtualAppEntity> queryVirtualApp() {
        return MyApplication.getDaoSession().getVirtualAppEntityDao().loadAll();
    }

    /**
     * 查询当前用户有没有这个虚拟app
     *
     * @param virtualNumber
     * @return
     */
    public static List<VirtualAppEntity> isVirtual(String virtualNumber) {
        return MyApplication.getDaoSession().getVirtualAppEntityDao().queryBuilder().where(VirtualAppEntityDao.Properties.App_number.eq(virtualNumber), VirtualAppEntityDao.Properties.UserCard.eq(UserManager.getUser().getCard())).list();
    }

    /**
     * 获取当前用户下的app
     *
     * @return
     */
    public static List<VirtualAppEntity> getVirtualAppList() {
        return MyApplication.getDaoSession().getVirtualAppEntityDao().queryBuilder().where(VirtualAppEntityDao.Properties.UserCard.eq(UserManager.getUser().getCard())).list();
    }


    //查询令牌数据
    //查询令牌数据
    //查询令牌数据
    //查询令牌数据
    //查询令牌数据

    /**
     * 添加数据，如果有重复则覆盖
     *
     * @param entity
     */
    public static void insertTokenReplace(TokenEntity entity) {
        MyApplication.getDaoSession().getTokenEntityDao().insertOrReplace(entity);
    }

    /**
     * 添加数据
     *
     * @param entity
     */
    public static void insertToken(TokenEntity entity) {
        TokenEntityDao dao = MyApplication.getDaoSession().getTokenEntityDao();

        //这里应该加上是否存在判断.?
        for (TokenEntity entity1:
             dao.loadAll()) {
            if(entity.getKey().equals(entity1.getKey())){
                ToastUtils.show("当前令牌已存在!");
                return;
            }
        }
        dao.insert(entity);

    }

    /**
     * 删除数据
     *
     * @param id
     */
    public static void deleteToken(long id) {
        MyApplication.getDaoSession().getTokenEntityDao().deleteByKey(id);
    }

    /**
     * 更新数据
     *
     * @param entity
     */
    public static void updateToken(TokenEntity entity) {
        MyApplication.getDaoSession().getTokenEntityDao().update(entity);
    }

    /**
     * 查询全部数据
     */
    public static List<TokenEntity> queryToken() {
        return MyApplication.getDaoSession().getTokenEntityDao().loadAll();
    }

    /**
     * 删除全部数据
     */
    public static void deleteToken() {
        Log.i("删除数据", "删除了数据");
        MyApplication.getDaoSession().getTokenEntityDao().deleteAll();
    }

    //查询消息列表数据
    //查询消息列表数据
    //查询消息列表数据
    //查询消息列表数据
    //查询消息列表数据

    /**
     * 添加数据，如果有重复则覆盖
     *
     * @param entity
     */
    public static void insertMessageListReplace(MessageListEntity entity) {
        MyApplication.getDaoSession().getMessageListEntityDao().insertOrReplace(entity);
    }

    /**
     * 添加数据
     *
     * @param entity
     */
    public static void insertMessageList(MessageListEntity entity) {
        MyApplication.getDaoSession().getMessageListEntityDao().insert(entity);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public static void deleteMessageList(long id) {
        MyApplication.getDaoSession().getMessageListEntityDao().deleteByKey(id);
    }

    /**
     * 更新数据
     *
     * @param entity
     */
    public static void updateMessageList(MessageListEntity entity) {
        MyApplication.getDaoSession().getMessageListEntityDao().update(entity);
    }

    /**
     * 查询全部数据
     */
    public static List<MessageListEntity> queryMessageList() {
        return MyApplication.getDaoSession().getMessageListEntityDao().loadAll();
    }

    /**
     * 获取当前用户消息列表
     *
     * @return
     */
    public static List<MessageListEntity> getUserMessageList() {
        if (UserManager.getUser() == null || UserManager.getUser().getCard() == null) return null;
        return MyApplication.getDaoSession().getMessageListEntityDao().queryBuilder().where(MessageListEntityDao.Properties.CurrentuserCard.eq(UserManager.getUser().getCard())).list();
    }

}
