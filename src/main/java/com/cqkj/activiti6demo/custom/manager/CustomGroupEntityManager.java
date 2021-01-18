package com.cqkj.activiti6demo.custom.manager;

import com.cqkj.activiti6demo.common.ResultCodeEnum;
import com.cqkj.activiti6demo.custom.exception.NoImplementException;
import com.cqkj.activiti6demo.pojo.CustomUser;
import org.activiti.engine.identity.*;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 自定义activiti用户组实体管理类
 */
@Component
public class CustomGroupEntityManager implements GroupEntityManager, Session {

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
    public Group createNewGroup(String s) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public GroupQuery createNewGroupQuery() {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public List<Group> findGroupByQueryCriteria(GroupQueryImpl groupQuery, Page page) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public long findGroupCountByQueryCriteria(GroupQueryImpl groupQuery) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public List<Group> findGroupsByUser(String s) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public List<Group> findGroupsByNativeQuery(Map<String, Object> map, int i, int i1) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public long findGroupCountByNativeQuery(Map<String, Object> map) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public boolean isNewGroup(Group group) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public GroupEntity create() {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public GroupEntity findById(String s) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public void insert(GroupEntity entity) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public void insert(GroupEntity entity, boolean b) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public GroupEntity update(GroupEntity entity) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public GroupEntity update(GroupEntity entity, boolean b) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public void delete(String s) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public void delete(GroupEntity entity) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }

    @Override
    public void delete(GroupEntity entity, boolean b) {
        throw new NoImplementException(ResultCodeEnum.IMPLEMENT_ERROR);
    }


}
