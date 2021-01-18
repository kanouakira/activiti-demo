package com.cqkj.activiti6demo.pojo;

import lombok.Data;

import java.sql.Timestamp;

/**
 * 待办任务信息实体类
 */
@Data
public class TaskInfo {
    private String ID_; // 待办任务ID
    private Integer REV_; // 待办任务版本
    private String EXECUTION_ID_; // 执行ID
    private String PROC_INST_ID_; // 流程实例ID
    private String PROC_DEF_ID_; // 流程定义ID
    private String NAME_; // 待办任务名
    private String PARENT_TASK_ID_; // 父级待办任务ID
    private String DESCRIPTION_; // 待办任务描述
    private String TASK_DEF_KEY_; // 待办任务KEY
    private String OWNER_; // 待办任务持有者
    private String ASSIGNEE_; // 待办任务签发者
    private String DELEGATION_;
    private Integer PRIORITY_;
    private Timestamp CREATE_TIME_; // 待办任务生成时间
    private Timestamp DUE_DATE_; // 待办任务逾期时间
    private String CATEGORY_;
    private Integer SUSPENSION_STATE_;
    private String TENANT_ID_;
    private String FORM_KEY_; // 待办任务表单KEY
    private Timestamp CLAIM_TIME_; // 待办任务签发时间
}
