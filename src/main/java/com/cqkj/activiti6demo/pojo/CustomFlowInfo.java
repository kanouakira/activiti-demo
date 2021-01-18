package com.cqkj.activiti6demo.pojo;

import lombok.Data;
import org.activiti.bpmn.model.GraphicInfo;

import java.util.List;

/**
 * 自定义流线布局信息实体类
 * 用于返回前端绘制当前流程图
 */
@Data
public class CustomFlowInfo {
    private String type; // 节点类型
    private Boolean completed; // 是否已完成
    private Boolean current; // 是否当前节点
    private List<GraphicInfo> waypoints; // 路径
}
