package com.cqkj.activiti6demo.custom.manager;

import com.cqkj.activiti6demo.common.ResultCodeEnum;
import com.cqkj.activiti6demo.custom.exception.NoImplementException;
import com.cqkj.activiti6demo.mapper.ActivitiMapper;
import com.cqkj.activiti6demo.pojo.CustomGroup;
import com.cqkj.activiti6demo.pojo.CustomUser;
import com.cqkj.activiti6demo.utils.ActivitiUtils;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 自定义activiti用户实体管理类
 */
@Component
public class CustomUserEntityManager implements UserEntityManager, Session {

    @Resource
    private ActivitiMapper activitiMapper;

    @Override
    public void flush() {
        //do something
    }

    @Override
    public void close() {
        //do something
    }

    // 根据需要重写方法

    @Override
    public User createNewUser(String userId) {
        CustomUser customUser = new CustomUser();
        customUser.setID_(userId);
        customUser.setRevision(0);
        return customUser;
    }

    @Override
    public List<User> findUserByQueryCriteria(UserQueryImpl userQuery, Page page) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public long findUserCountByQueryCriteria(UserQueryImpl userQuery) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public List<Group> findGroupsByUser(String userId) {
        List<CustomGroup> groupsByUserId = activitiMapper.findGroupsByUserId(userId);
        final List<Group> groups = ActivitiUtils.toActivitiGroups(groupsByUserId);
        return groups;
    }

    @Override
    public UserQuery createNewUserQuery() {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public Boolean checkPassword(String s, String s1) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public List<User> findUsersByNativeQuery(Map<String, Object> map, int i, int i1) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public long findUserCountByNativeQuery(Map<String, Object> map) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public boolean isNewUser(User user) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public Picture getUserPicture(String s) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public void setUserPicture(String s, Picture picture) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public void deletePicture(User user) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public UserEntity create() {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public UserEntity findById(String userId) {
        CustomUser userById = activitiMapper.findUserById(userId);
        return userById;
    }

    @Override
    public void insert(UserEntity entity) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public void insert(UserEntity entity, boolean b) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public UserEntity update(UserEntity entity) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public UserEntity update(UserEntity entity, boolean b) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public void delete(String s) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public void delete(UserEntity entity) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public void delete(UserEntity entity, boolean b) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public void updateUser(User user) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }
}
