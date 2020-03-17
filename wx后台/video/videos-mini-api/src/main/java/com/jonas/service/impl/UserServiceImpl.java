package com.jonas.service.impl;

import com.jonas.common.idworker.Sid;
import com.jonas.mapper.UserInfoMapper;
import com.jonas.mapper.UserReportMapper;
import com.jonas.mapper.UsersFansMapper;
import com.jonas.pojo.UserInfo;
import com.jonas.pojo.UserReport;
import com.jonas.pojo.UsersFans;
import com.jonas.service.UserService;
import com.jonas.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author cmj
 * @date 2019-02-11 09:49
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UsersFansMapper usersFansMapper;
    @Autowired
    private UserReportMapper userReportMapper;
    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        UserInfo userInfoDB = userInfoMapper.selectOne(username);
        if (userInfoDB!=null) {
            return true;
        } else {
            return false;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUser(UserInfo userInfo) {
        userInfo.setId(sid.nextShort());
        userInfoMapper.insert(userInfo);
    }

    @Override
    public UserInfo canLogin(UserInfo userInfo) {
        try {
            if (!StringUtils.isBlank(userInfo.getUsername()) && !StringUtils.isBlank(userInfo.getPassword())) {
                UserInfo userInfoDB = userInfoMapper.selectOne(userInfo.getUsername());
                    //用户相同则校验密码
                    if (userInfo.getUsername().equals(userInfoDB.getUsername())) {
                        String password = MD5Utils.getMD5Str(userInfo.getPassword());
                        if (password.equals(userInfoDB.getPassword())){
                            userInfoDB.setPassword("");
                            return userInfoDB;
                        }else {
                            return null;
                        }
                    }


            }else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateUserInfo(UserInfo userInfo) {
        try {
            if (StringUtils.isNotBlank(userInfo.getId())){
                UserInfo userInfoUpdate = userInfoMapper.selectByPrimaryKey(userInfo.getId());
                //如果传入的参数不为空则进行赋值
                if (StringUtils.isNotBlank(userInfo.getUsername())){
                    userInfoUpdate.setUsername(userInfo.getUsername());
                }
                if (StringUtils.isNotBlank(userInfo.getPassword())){
                    userInfoUpdate.setPassword(MD5Utils.getMD5Str(userInfo.getPassword()));
                }
                if (StringUtils.isNotBlank(userInfo.getFaceImage())){
                    userInfoUpdate.setFaceImage(userInfo.getFaceImage());
                }
                if (StringUtils.isNotBlank(userInfo.getFansCounts())){
                    userInfoUpdate.setFansCounts(userInfo.getFansCounts());
                }
                if (StringUtils.isNotBlank(userInfo.getFollowCounts())){
                    userInfoUpdate.setFollowCounts(userInfo.getFollowCounts());
                }
                if (StringUtils.isNotBlank(userInfo.getNickname())){
                    userInfoUpdate.setNickname(userInfo.getNickname());
                }
                if (StringUtils.isNotBlank(userInfo.getReceiveLikeCounts())){
                    userInfoUpdate.setReceiveLikeCounts(userInfo.getReceiveLikeCounts());
                }
                userInfoMapper.updateByPrimaryKey(userInfoUpdate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserInfo searchUserInfoById(UserInfo userInfo) {
        UserInfo userInfoQuery=null;
        if (userInfo.getId()!=null){
            userInfoQuery = userInfoMapper.selectByPrimaryKey(userInfo.getId());
            userInfoQuery.setPassword("");
        }
        return userInfoQuery;
    }

    @Transactional
    @Override
    public void saveUserFansRelation(String userId, String fansId) {
        UsersFans usersFans = new UsersFans();
        usersFans.setId(sid.nextShort());
        usersFans.setUserId(userId);
        usersFans.setFanId(fansId);
        usersFansMapper.insert(usersFans);

        userInfoMapper.addFansCount(userId);
        userInfoMapper.addFollowCount(fansId);

    }

    @Override
    public void deleteUserFansRelation(String userId, String fansId) {
        UsersFans usersFans = new UsersFans();
        usersFans.setUserId(userId);
        usersFans.setFanId(fansId);
        usersFansMapper.deleteByUserIdAndFanId(usersFans);

        userInfoMapper.reduceFansCount(userId);
        userInfoMapper.reduceFollowCount(fansId);
    }

    @Override
    public UsersFans selectUserFansRelation(UsersFans usersFans) {
        String fanId = usersFans.getFanId();
        String userId = usersFans.getUserId();
            UsersFans selectByUserIdAndFanId=null;
        if (StringUtils.isNotBlank(fanId) && StringUtils.isNotBlank(userId)){
            selectByUserIdAndFanId = usersFansMapper.selectByUserIdAndFanId(usersFans);
        }
        return selectByUserIdAndFanId;
    }

    @Override
    public void reportUser(UserReport userReport) {
        userReport.setId(sid.nextShort());
        userReport.setCreateTime(new Date());
        userReportMapper.insert(userReport);
    }

}
