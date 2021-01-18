package com.cqkj.activiti6demo;

import com.cqkj.activiti6demo.mapper.ActivitiMapper;
import com.cqkj.activiti6demo.pojo.ProcDef;
import com.cqkj.activiti6demo.service.ActivitiService;
import com.cqkj.activiti6demo.utils.WriteUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;

@Slf4j
@SpringBootTest
class  Activiti6demoApplicationTests {

    @Resource
    private ActivitiService activitiService;

    @Resource
    private TaskService taskService;

    @Resource
    private IdentityService identityService;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private FormService formService;

    @Resource
    private HistoryService historyService;

    /**
     * 1.测试财务申请流程的发起
     * processId = 75001
     */
    @Test
    void testcwsq(){
        identityService.setAuthenticatedUserId("KanouAkira");
        // 根据流程key发起流程
        final ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("FinanceProcess");
        log.info("发起流程成功,流程Id={}", processInstance.getId());
    }

    /**
     * 2.根据用户获取Task列表
     * 根据Task列表中的task的Id获取表单
     */
    @Test
    void testcwsqform(){
        // 根据用户获取Task列表
        final List<Task> tasks = taskService.createTaskQuery().taskAssignee("KanouAkira").list();
        tasks.forEach(task -> {
            if ("申请财务".equals(task.getName())){
//                log.info("任务的流程定义ID={}",task.getProcessDefinitionId());
                // 根据taskId获取定义的表单
                final TaskFormData taskFormData = formService.getTaskFormData(task.getId());
                // 遍历定义表单的元素及其内容，由此返回给前端页面生成表单填写
                taskFormData.getFormProperties().forEach(formProperty -> {
                    log.info("id:{},name:{},type:{},isRequired:{},isReadable:{},isWritable:{}",formProperty.getId(),formProperty.getName(),formProperty.getType(),formProperty.isRequired(),formProperty.isReadable(),formProperty.isWritable());
                });
                // 根据填写的内容返回一个结果map集合
                Map<String, String> formResultMap = new HashMap<>();
                formResultMap.put("money", "19999");
                formResultMap.put("reason", "谢谢财务主管");

                // 根据InputStream或者url在任务中添加附件
                try {
                    File file = ResourceUtils.getFile("classpath:static/Detail.xlsx");
                    InputStream inputStream = new FileInputStream(file);
                    taskService.createAttachment("file", task.getId(), task.getProcessInstanceId(), "Detail.xlsx", "这是针对IEC104规约解析的细则excel文件", inputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                try {
                    // 根据填写的map集合提交任务到下一节点
                    formService.submitTaskFormData(task.getId(), formResultMap);
                }catch (ActivitiException e){
                    log.error("提交表单错误:{}",e.getMessage());
                }
            }
        });
    }

    /**
     * 财务组进行审核
     */
    @Test
    void testcwsqverify() throws IOException {
        // 根据用户获取Task列表
        final List<Task> tasks = taskService.createTaskQuery().taskAssignee("KA").list();
        for (Task task : tasks) {
            if ("财务主管审批".equals(task.getName())){
//            if ("财务人员审批".equals(task.getName())){
                final Map<String, Object> variables = runtimeService.getVariables(task.getExecutionId());
                log.info("申请人:{},申请金额:{},申请原因:{}", variables.get("applyUserId"), variables.get("money"), variables.get("reason"));
                // 根据taskId获取定义的表单
                final TaskFormData taskFormData = formService.getTaskFormData(task.getId());
                // 遍历定义表单的元素及其内容，由此返回给前端页面生成表单填写
                taskFormData.getFormProperties().forEach(formProperty -> {
                    log.info("id:{},name:{},type:{},isRequired:{},isReadable:{},isWritable:{}",formProperty.getId(),formProperty.getName(),formProperty.getType(),formProperty.isRequired(),formProperty.isReadable(),formProperty.isWritable());
                });

                // 根据当前流程实例获取附件信息
                List<Attachment> attachments = taskService.getProcessInstanceAttachments(task.getProcessInstanceId());
                for (Attachment attachment : attachments) {
                    log.info("附件名:{},附件描述:{}",attachment.getName(), attachment.getDescription());
                    // 根据附件Id获取附件文件流
                    final InputStream attachmentContent = taskService.getAttachmentContent(attachment.getId());
                    WriteUtil.writeToLocal(String.format("D:\\%s", attachment.getName()), attachmentContent);
                }
                // 根据填写的内容返回一个结果map集合
                Map<String, String> formResultMap = new HashMap<>();
//                formResultMap.put("result", "false");
                formResultMap.put("result", "true");
                try{
                    formService.submitTaskFormData(task.getId(), formResultMap);
                }catch (ActivitiException e){
                    log.error("提交表单错误:{}",e.getMessage());
                }
            }
        }
    }

    @Test
    void vocationAssign(){
//        taskService.setVariable("25005","val","kan");
        final List<Task> list = taskService.createTaskQuery().taskId("25005").list();
        String nextId = "";
        for (Task task : list) {
            // 根据任务获取流程定义
            final ProcessDefinition processDefinition = repositoryService.getProcessDefinition(task.getProcessDefinitionId());
            System.out.println();
        }
    }

    @Test
    void vocationtask(){
        final List<Task> kermit = taskService.createTaskQuery().taskAssignee("kermit").list();
        kermit.forEach(task -> {
            System.out.println(task.getId() + " " + task.getName());
            activitiService.getNextFlowElement(task.getProcessInstanceId());
//            taskService.complete(task.getId());
        });

    }

    @Test
    void deployResource(){
        final DeploymentBuilder deployment = repositoryService.createDeployment();
        try {
            File file = ResourceUtils.getFile("classpath:static/Detail.xlsx");
            InputStream inputStream = new FileInputStream(file);
            System.out.println();
//            deployment.addInputStream("财务申请流程模型",inputStream).deploy();
//            System.out.println();
//            deployment.addInputStream(inputStream);
//            deployment.addClasspathResource("classpath:processes/cwsqModel.bpmn20.xml").deploy();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据流程实例Id获取附件内容
     */
    @Test
    void findAttachmentByProcessInstanceId(){
        // 根据当前流程实例获取附件信息
        List<Attachment> attachments = taskService.getProcessInstanceAttachments("145001");
        for (Attachment attachment : attachments) {
            log.info("附件名:{},附件描述:{}",attachment.getName(), attachment.getDescription());
        }
    }

    /**
     * 获取已定义的流程
     */
    @Test
    void findDefinitions(){
//        repositoryService.deployment
        final List<String> deploymentResourceNames = repositoryService.getDeploymentResourceNames("105001");
        System.out.println();
    }

    /**
     * 新增用户
     */
    @Test
    void createUser() throws IOException {
        String PROC_INST_ID = "2501";
        Task task = taskService.createTaskQuery().processInstanceId(PROC_INST_ID).singleResult();
        final List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(PROC_INST_ID).list();

                                                                                                                                                                                                                                                                                                                             ProcessDefinition processDefinition = repositoryService.getProcessDefinition(task.getProcessDefinitionId());
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        // ID 为 流程定义Key
        Process process = bpmnModel.getProcessById(processDefinition.getKey());

//        UserTask userTask = (UserTask) process.getFlowElement(task.getTaskDefinitionKey());
        // 流程节点ID
        FlowElement flowElement = process.getFlowElement(task.getTaskDefinitionKey());

        DefaultProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
        List<String> highLightedActivities = new ArrayList<>();
        highLightedActivities.add(flowElement.getId());
        List<String> highLightedFlows = new ArrayList<>();

        list.forEach(historicTaskInstance -> highLightedActivities.add(process.getFlowElement(historicTaskInstance.getTaskDefinitionKey()).getId()));

//     生成流程图
//        InputStream inputStream = generator.generateJpgDiagram(bpmnModel);
//        InputStream inputStream = generator.generatePngDiagram(bpmnModel);
//        InputStream inputStream = generator.generateDiagram(bpmnModel, "jpg", highLightedActivities);

// 生成图片
        InputStream inputStream = generator.generateDiagram(bpmnModel, "png", highLightedActivities, highLightedFlows, "宋体", "宋体", "宋体", null, 2.0);

        WriteUtil.writeToLocal("D:\\test.png", inputStream);
    }

    @Test
    void getProcess(){
//        taskService.complete();
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionVersion().asc().list();
        System.out.println();
    }
}
