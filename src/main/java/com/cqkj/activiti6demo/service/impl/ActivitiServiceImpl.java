package com.cqkj.activiti6demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.cqkj.activiti6demo.mapper.ActivitiMapper;
import com.cqkj.activiti6demo.pojo.*;
import com.cqkj.activiti6demo.service.ActivitiService;
import com.cqkj.activiti6demo.utils.ActivitiUtils;
import com.cqkj.activiti6demo.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Slf4j
@Service
public class ActivitiServiceImpl implements ActivitiService {

    @Autowired
    private IdentityService identityService; // Activiti八大服务之一，主要作用身份认证方面的服务

    @Autowired
    private RuntimeService runtimeService; // Activiti八大服务之一，主要作用流程运行时的服务

    @Autowired
    private TaskService taskService; // Activiti八大服务之一，主要作用任务方面的服务

    @Autowired
    private HistoryService historyService; // Activiti八大服务之一，主要作用流程历史方面的服务

    @Autowired
    private RepositoryService repositoryService; // Activiti八大服务之一，主要作用流程仓库方面的服务

    @Autowired
    private FormService formService; // Activiti八大服务之一，主要作用流程表单方面的服务

    @Autowired
    private ActivitiMapper activitiMapper;


    /**
     * 查询所有已定义的流程信息
     * @return
     */
    @Override
    public Map<String,ProcDef> findAllProcDef() {
        List<ProcDef> list = activitiMapper.findAllProcDef();
        //定义有序map，相同的key,添加map值后，后面的会覆盖前面的值
        Map<String,ProcDef> map=new LinkedHashMap();
        //遍历相同的key，替换最新的值
        for(ProcDef pd:list){
            map.put(pd.getKEY_(), pd);
        }
        return map;
    }

    /**
     * 根据key发起一个流程，并且根据userId设置发起人
     * @param userId 发起人的Id
     * @param procKey 流程的key
     * @return
     */
    @Override
    public String start(String userId, String procKey) {
        // 设置流程发起人，先后顺序不能修改
        identityService.setAuthenticatedUserId(userId);
        // 根据流程key发起流程
        final ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(procKey);
        log.info("发起流程成功,流程Id={},发起人:{}", processInstance.getId(), userId);
        return processInstance.getId();
    }

    /**
     * 根据流程实例的Id获取当前审批节点的下一级审批节点的信息
     * @param processInstanceId 流程实例的Id
     * @return
     */
    @Override
    public FlowElement getNextFlowElement(String processInstanceId) {
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        Optional.ofNullable(task).orElseThrow(() -> new RuntimeException("流程未启动或已执行完成"));
        // 获取已提交的任务
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(task.getId()).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(historicTaskInstance.getProcessDefinitionId());
        // 获取节点
        FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(historicTaskInstance.getTaskDefinitionKey());
        // 获取节点输出连线
        List<SequenceFlow> outgoingFlows = flowNode.getOutgoingFlows();
        for (SequenceFlow outgoingFlow : outgoingFlows) {
            // 获取节点输出连线对象节点
//            final FlowElement targetFlowElement = outgoingFlow.getTargetFlowElement();
            // 排除非用户任务的节点
//            if (targetFlowElement instanceof UserTask){
                // TODO: 2020/11/30 还需要多分支的情况下获取下一个审批节点的信息 
//                System.out.println();
//            }
        }
        return null;
    }

    /**
     * 根据流程实例Id获取绘制流程图所需要的信息
     * @param processInstanceId 实例Id
     * @return
     */
    @Override
    public Map<String, Object> getBpmnDiagramInfo(String processInstanceId) {
        Map<String, Object> result = new HashMap<>();
        List<CustomGraphicInfo> elements = new ArrayList<>(); // 保存节点对象
        List<CustomFlowInfo> flows = new ArrayList<>(); // 保存流对象
        // 根据流程id获取流程实例
        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        // 根据流程实例获取到流程模型
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());

        // 用于保存节点类型
        Map<String, String> typeOfTask = new HashMap<>();
        // 用于保存已完成的节点信息
        Map<String, HistoricActivityInstance> completedTask = new HashMap<>();

        // 获取流程中节点的id对应的类型
        List<Process> processes = bpmnModel.getProcesses();
        for (final Process process : processes) {
            List<FlowElement> flowElements = (List<FlowElement>) process.getFlowElements();
            for (final FlowElement flowElement : flowElements) {
                String simpleName = flowElement.getClass().getSimpleName();
                typeOfTask.put(flowElement.getId(), simpleName);
            }
        }

        // 获取已经完成的节点信息
        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceId().asc().list();

        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            String activityId = historicActivityInstance.getActivityId(); // 流程文件里定义的id 同 taskDefinitionKey
            completedTask.put(activityId, historicActivityInstance);
        }
        // 获取当前节点信息
        Task current_task = taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();

        // 获取节点元素定位信息
        Map<String, GraphicInfo> locationMap = bpmnModel.getLocationMap();
        // 遍历节点元素定位信息
        locationMap.keySet().forEach(key -> {
            CustomGraphicInfo customGraphicInfo = new CustomGraphicInfo();
            CommonUtil.parent2sub(locationMap.get(key), customGraphicInfo);
            Boolean completed = false;
            Boolean current = false;
            if (completedTask.get(key) != null){
                completed = true;
            }
            // 是否是当前节点
            if (current_task != null && key.equals(current_task.getTaskDefinitionKey())){
                current = true;
                completed = false;
            }
            customGraphicInfo.setName(bpmnModel.getFlowElement(key).getName()); // 获取该节点名称
            customGraphicInfo.setCompleted(completed); // 该节点是否已完成
            customGraphicInfo.setCurrent(current); // 该节点是否为当前节点
            customGraphicInfo.setType(typeOfTask.get(key));
            elements.add(customGraphicInfo);
        });

        // 获取已经流转的线
        List<String> highLightedFlows = ActivitiUtils.getHighLightedFlows(bpmnModel, historicActivityInstances);

        // 获取流元素定位信息
        Map<String, List<GraphicInfo>> flowLocationMap = bpmnModel.getFlowLocationMap();
        flowLocationMap.keySet().forEach(key -> {
            CustomFlowInfo customFlowInfo = new CustomFlowInfo();
            Boolean completed = false;
            if (highLightedFlows.contains(key)){
                completed = true;
            }
            customFlowInfo.setType(typeOfTask.get(key));
            customFlowInfo.setCompleted(completed);
            customFlowInfo.setWaypoints(flowLocationMap.get(key));
            flows.add(customFlowInfo);
        });

        result.put("elements", elements);
        result.put("flows", flows);
        result = ActivitiUtils.calculateBorder(result, bpmnModel); // 添加画布边界
        return result;
    }

    /**
     * 根据userId获取待办
     * @param userId userId
     * @return
     */
    @Override
    public List<Task> getTask(String userId) {
        return taskService.createTaskQuery().taskAssignee(userId).list();
    }

    /**
     * 获取未完成流程
     * @param userId userId
     * @return
     */
    @Override
    public List<HistoricProcessInstance> getUnFinishedProc(String userId) {
        return historyService.createHistoricProcessInstanceQuery().startedBy(userId).unfinished().orderByProcessInstanceStartTime().asc().list();
    }

    /**
     * 获取已完成流程
     * @return
     */
    @Override
    public List<HistoricProcessInstance> getFinishedProc(String userId) {
        return historyService.createHistoricProcessInstanceQuery().startedBy(userId).finished().orderByProcessInstanceEndTime().desc().list();
    }

    /**
     * 删除流程实例
     * @param procInstId 流程实例ID
     */
    @Override
    public void delProcInst(String procInstId) {
        final HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstId).singleResult();
        final String startUserId = historicProcessInstance.getStartUserId();
        runtimeService.deleteProcessInstance(procInstId, String.format("Cancelled by %s", startUserId));
    }

    /**
     * 根据taskId获取当前流程任务的表单
     * @param taskId 任务id
     * @return
     */
    @Override
    public TaskFormData getTaskFormData(String taskId) {
        // 根据taskId获取定义的表单
        final TaskFormData taskFormData = formService.getTaskFormData(taskId);
        // 遍历定义表单的元素及其内容，由此返回给前端页面生成表单填写
        taskFormData.getFormProperties().forEach(formProperty -> {
            log.info("id:{},name:{},type:{},isRequired:{},isReadable:{},isWritable:{}",formProperty.getId(),formProperty.getName(),formProperty.getType(),formProperty.isRequired(),formProperty.isReadable(),formProperty.isWritable());
        });
        return taskFormData;
    }

    /**
     * 根据taskId填写当前任务表单
     * @param taskId 任务id
     * @return
     */
    @Override
    public void completeFormTask(String taskId, Map<String, String> results) {
        if (results.size() > 0){
            // 根据填写的map集合提交任务到下一节点
            formService.submitTaskFormData(taskId, results);
        }else {
            taskService.complete(taskId);
        }
    }


    /**
     * 根据taskId和输入流对象添加附件到流程
     * @param taskId 任务id
     * @param FileName 附件文件名
     * @param FileDescription 附件描述
     * @param inputStream 输入流
     * @return
     */
    @Override
    public Attachment setTaskAttachment(String taskId, String FileName, String FileDescription, InputStream inputStream) {
        Attachment attachment = null;
        // 根据InputStream或者url在任务中添加附件
        // 根据taskId获取Task对象
        final Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        attachment = taskService.createAttachment("file", taskId, task.getProcessInstanceId(), FileName, FileDescription, inputStream);
        return attachment;
    }

    /**
     * 根据附件Id删除附件
     * @param attachmentId 附件Id
     */
    @Override
    public void delTaskAttachment(String attachmentId) {
        taskService.deleteAttachment(attachmentId);
    }

    /**
     * 根据附件Id获取附件
     * @param attachmentId 附件Id
     * @return
     */
    @Override
    public Attachment getAttachment(String attachmentId) {
        return taskService.getAttachment(attachmentId);
    }

    @Override
    public void createUser(User user) {
        identityService.saveUser(user);
    }

    @Override
    public boolean verifyPassword(String userId, String password) {
        boolean result = false;
        // 根据userId返回数据库中的user对象
        final CustomUser user = activitiMapper.findUserById(userId);
        if (user == null){
            return false;
        }
        if (password.equals(user.getPASSWORD_())){
            result = true;
        }
        return result;
    }

    @Override
    public TaskInfo getTaskInfo(String taskId) {
        return activitiMapper.findTaskInfo(taskId);
    }

    @Override
    public List<Attachment> getTaskAttachments(String taskId) {
        final Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return taskService.getProcessInstanceAttachments(task.getProcessInstanceId());
    }

    /**
     * 部署流程资源
     * @param name
     * @param inputStream
     * @return
     */
    @Override
    public String deploy(String name, InputStream inputStream) {
//        Deployment latestDeployment = repositoryService.createDeploymentQuery()
//                .deploymentName(name)
//                .deploymentKey(name)
//                .latest()
//                .singleResult();
//        String sbpmn = ActivitiUtil.text(inputStream);
//        if (latestDeployment != null) {
//            //检测是否内容发生变化，只重新部署有修改的流程
//            InputStream input = repositoryService.getResourceAsStream(latestDeployment.getId(), name);
//            String dbpmn = ActivitiUtil.text(input);
//            if (sbpmn.length() == dbpmn.length() && sbpmn.equals(dbpmn)) {
//                return latestDeployment.getId();
//            }
//        }
        // TODO: 2021/1/7  inputStream 比对 内容不变版本号不升

        String deploymentId = repositoryService.createDeployment()
                .addInputStream(name, inputStream)
                .name(name)
                .key(name)
                .deploy()
                .getId();
        return deploymentId;
    }
}
