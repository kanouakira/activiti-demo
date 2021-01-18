package com.cqkj.activiti6demo.pojo;

import lombok.Data;
import org.activiti.bpmn.model.GraphicInfo;

/**
 * 自定义图形布局信息实体类
 * 用于返回前端绘制当前流程图
 */
@Data
public class CustomGraphicInfo extends GraphicInfo {
    private String name; // 节点名称
    private String type; // 节点类型
    private Boolean completed; // 是否已完成
    private Boolean current; // 是否当前节点
}
