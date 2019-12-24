package com.jonas.service;

import com.jonas.dto.UserDTO;
import com.jonas.pojo.UserInfo;
import com.jonas.pojo.UserReport;
import com.jonas.pojo.UsersFans;
import com.jonas.utils.PagedResult;
import org.springframework.stereotype.Service;

/**
 * @author cmj
 * @date 2019-02-11 09:44
 */
@Service
public interface UserService {
    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    boolean queryUsernameIsExist(String username);

    /**
     * 保存用户信息
     * @param userInfo
     */
    void saveUser(UserInfo userInfo);

    /**
     * 用户登录的实现类，校验用户名密码是否对应
     * @param userInfo
     * @return
     */
    UserInfo canLogin(UserInfo userInfo);

    /**
     * 更新用户信息
     * @param userInfo
     * @return
     */
    void updateUserInfo(UserInfo userInfo);

    /**
     * 查询用户信息
     * @param userInfo
     * @return
     */
    UserInfo searchUserInfoById(UserInfo userInfo);

    /**
     * 增加用户的已关注数，和被关注人的粉丝数
     * @param userId 当前登录用户的Id
     * @param fansId 被关注人的Id
     */
    void saveUserFansRelation(String userId,String fansId);

    /**
     * 减少用户的已关注数，和被关注人的粉丝数
     * @param userId 当前登录用户的Id
     * @param fansId 被取消关注人的Id
     */
    void deleteUserFansRelation(String userId,String fansId);

    /**
     * 查询用户粉丝的关系
     * @param usersFans
     * @return
     */
    UsersFans selectUserFansRelation(UsersFans usersFans);

    /**
     * 举报用户违规
     * @param userReport
     */
    void reportUser(UserReport userReport);

    PagedResult getUserInfoByCondition(UserDTO userDTO);


}
