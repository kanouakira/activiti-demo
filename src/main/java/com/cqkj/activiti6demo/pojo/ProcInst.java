package com.cqkj.activiti6demo.pojo;

import lombok.Data;

/**
 * 流程實例实体类
 */
@Data
public class ProcInst {
    private String ID_; // 唯一ID
    private Integer PROC_INST_ID; // 订正版本
    private String BUSINESS_KEY_; // 分类
    private String KEY_; // 流程KEY 唯一
    private Integer VERSION_; // 流程版本
    private String DEPLOYMENT_ID_; // 流程发布的ID
    private String RESOURCE_NAME_; // 流程资源路径
    private String DGRM_RESOURCE_NAME_; // 流程图资源路径
    private String DESCRIPTION_; // 流程描述
    private String NAME_; // 流程名称
}
