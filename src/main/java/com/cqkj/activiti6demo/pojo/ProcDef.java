package com.cqkj.activiti6demo.pojo;

import lombok.Data;

/**
 * 流程定义实体类
 */
@Data
public class ProcDef {
    private String ID_; // 唯一ID
    private Integer REV_; // 订正版本
    private String CATEGORY_; // 分类
    private String NAME_; // 流程名称
    private String KEY_; // 流程KEY 唯一
    private Integer VERSION_; // 流程版本
    private String DEPLOYMENT_ID_; // 流程发布的ID
    private String RESOURCE_NAME_; // 流程资源路径
    private String DGRM_RESOURCE_NAME_; // 流程图资源路径
    private String DESCRIPTION_; // 流程描述
}
