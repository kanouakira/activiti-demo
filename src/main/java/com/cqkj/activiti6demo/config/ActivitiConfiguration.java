package com.cqkj.activiti6demo.config;

import com.cqkj.activiti6demo.custom.factory.CustomGroupEntityManagerFactory;
import com.cqkj.activiti6demo.custom.factory.CustomUserEntityManagerFactory;
import com.cqkj.activiti6demo.custom.manager.CustomGroupEntityManager;
import com.cqkj.activiti6demo.custom.manager.CustomUserEntityManager;
import com.zaxxer.hikari.HikariDataSource;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableTransactionManagement // 开启事务管理
public class ActivitiConfiguration extends AbstractProcessEngineAutoConfiguration {

    @Resource
    private CustomUserEntityManager customUserEntityManager;
    @Resource
    private CustomUserEntityManagerFactory customUserEntityManagerFactory;
    @Resource
    private CustomGroupEntityManager customGroupEntityManager;
    @Resource
    private CustomGroupEntityManagerFactory customGroupEntityManagerFactory;

//    @Primary
    @Bean("activitiDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.activiti.datasource") // 根据自定义的数据源配置返回数据源参数
    public DataSourceProperties activitiDataSourceProperties(){
        return new DataSourceProperties();
    }

    @Bean("activitiDataSource")
    public DataSource activitiDataSource(){
        DataSourceProperties activiti = activitiDataSourceProperties();
//        DataSourceProperties activiti = new DataSourceProperties();
        return activiti.initializeDataSourceBuilder().build();
    }

    @Primary // 这个注解在出现类型重复的时候优先选择
    @Bean("insuranceActivitiConfig")
    public SpringProcessEngineConfiguration insuranceActivitiConfig(PlatformTransactionManager transactionManager,
                                                                    SpringAsyncExecutor springAsyncExecutor) throws IOException {
        SpringProcessEngineConfiguration springProcessEngineConfiguration = baseSpringProcessEngineConfiguration(
                activitiDataSource(),
                transactionManager,
                springAsyncExecutor);

        // 配置自定义的用户和组管理
        springProcessEngineConfiguration.setUserEntityManager(customUserEntityManager);
        springProcessEngineConfiguration.setGroupEntityManager(customGroupEntityManager);

        List<SessionFactory> customSessionFactories = new ArrayList<>();
        customSessionFactories.add(customUserEntityManagerFactory);
        customSessionFactories.add(customGroupEntityManagerFactory);
        springProcessEngineConfiguration.setCustomSessionFactories(customSessionFactories);

        return springProcessEngineConfiguration;
    }

//    @Bean
//    public DataSource database(){
//        return DataSourceBuilder.create()
//                .url("jdbc:mysql://192.168.83.139:3306/activiti_cstm?useUnicode=true&characterEncoding=utf8")
//                .username("root")
//                .password("root")
//                .driverClassName("com.mysql.cj.jdbc.Driver")
//                .build();
//    }

}
