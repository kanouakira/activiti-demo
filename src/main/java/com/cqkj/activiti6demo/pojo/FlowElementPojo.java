package com.cqkj.activiti6demo.pojo;

import lombok.Data;

/**
 * 自定义流转线信息
 */
@Data
public class FlowElementPojo {
    private String Id;
    private String TargetFlowElementId;
    private String SourceFlowElementId;
    private String FlowElementType;

}
