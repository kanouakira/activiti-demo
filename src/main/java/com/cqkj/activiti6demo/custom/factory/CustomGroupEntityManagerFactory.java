package com.cqkj.activiti6demo.custom.factory;

import com.cqkj.activiti6demo.custom.manager.CustomGroupEntityManager;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 自定义的activiti用户组管理工厂类
 */
@Service
public class CustomGroupEntityManagerFactory implements SessionFactory {
    @Resource
    private CustomGroupEntityManager customGroupEntityManager;

    @Override
    public Class<?> getSessionType() {
        return GroupEntityManager.class;
    }

    @Override
    public Session openSession(CommandContext commandContext) {
        return customGroupEntityManager;
    }
}
