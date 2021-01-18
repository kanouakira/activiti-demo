package com.cqkj.activiti6demo.pojo;

import lombok.Data;

import java.util.Map;

@Data
public class SubmitTask {
    private String taskId; // 任务ID
    private Map<String, String> results; // 表单内容
}
