package com.cqkj.activiti6demo.pojo;

import lombok.Data;

/**
 * 任务表达式
 */
@Data
public class TaskRepresentation {
    private String id;
    private String name;

    public TaskRepresentation(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
