package com.cqkj.activiti6demo.custom.factory;

import com.cqkj.activiti6demo.custom.manager.CustomUserEntityManager;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.springframework.stereotype.Service;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;

import javax.annotation.Resource;

/**
 * 自定义的activiti用户管理工厂类
 */
@Service
public class CustomUserEntityManagerFactory implements SessionFactory {
    // 使用自定义的用户管理类
    @Resource
    private CustomUserEntityManager customUserEntityManager;

    @Override
    public Class<?> getSessionType() {
        // activiti原生类
        return UserEntityManager.class;
    }

    @Override
    public Session openSession(CommandContext commandContext) {
        return customUserEntityManager;
    }
}
