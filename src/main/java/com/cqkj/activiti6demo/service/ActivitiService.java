package com.cqkj.activiti6demo.service;

import com.cqkj.activiti6demo.pojo.ProcDef;
import com.cqkj.activiti6demo.pojo.TaskInfo;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface ActivitiService {

    /**
     * 查询所有已定义的流程信息
     * @return
     */
    Map<String,ProcDef> findAllProcDef();

    /**
     * 根据key发起一个流程，并且根据userId设置发起人
     * @param userId 发起人的Id
     * @param procKey 流程的key
     * @return
     */
    String start(String userId, String procKey);

    /**
     * 根据流程实例的Id获取当前审批节点的下一级审批节点的信息
     * @param processInstanceId 流程实例的Id
     * @return
     */
    FlowElement getNextFlowElement(String processInstanceId);

    /**
     * 根据流程实例Id获取绘制流程图所需要的信息
     * @param processInstanceId 实例Id
     * @return
     */
    Map<String, Object> getBpmnDiagramInfo(String processInstanceId);

    /**
     * 根据userId获取待办
     * @param userId userId
     * @return
     */
    List<Task> getTask(String userId);

    /**
     * 根据userId获取未完成流程
     * @param userId userId
     * @return
     */
    List<HistoricProcessInstance> getUnFinishedProc(String userId);

    /**
     * 根据userId获取已完成流程
     * @param userId userId
     * @return
     */
    List<HistoricProcessInstance> getFinishedProc(String userId);

    /**
     * 取消流程实例
     * @param procInstId
     */
    void delProcInst(String procInstId);

    /**
     * 根据taskId获取当前流程任务的表单
     * @param taskId 任务id
     * @return
     */
    TaskFormData getTaskFormData(String taskId);


    /**
     * 根据taskId填写当前任务表单
     * @param taskId 任务id
     * @return
     */
    void completeFormTask(String taskId, Map<String, String> results);

    /**
     * 根据taskId和输入流对象添加附件到流程
     * @param taskId 任务id
     * @param FileName 附件文件名
     * @param FileDescription 附件描述
     * @param inputStream 输入流
     * @return
     */
    Attachment setTaskAttachment(String taskId, String FileName, String FileDescription, InputStream inputStream);

    /**
     * 根据附件Id删除附件
     * @param attachmentId 附件Id
     */
    void delTaskAttachment(String attachmentId);

    /**
     * 根据附件Id获取附件实例
     * @param attachmentId 附件Id
     * @return
     */
    Attachment getAttachment(String attachmentId);

    /**
     * 新增用户
     * @param user 用户对象
     */
    void createUser(User user);

    /**
     * 核验密码
     * @param userId 用户Id
     * @param password 密码
     * @return
     */
    boolean verifyPassword(String userId, String password);

    /**
     * 根据taskId获取task信息
     * @param taskId
     * @return
     */
    TaskInfo getTaskInfo(String taskId);

    /**
     *
     * @param taskId 任务Id
     * @return
     */
    List<Attachment> getTaskAttachments(String taskId);

    /**
     * 部署流程资源
     * @param name
     * @param inputStream
     * @return
     */
    String deploy(String name, InputStream inputStream);
}
