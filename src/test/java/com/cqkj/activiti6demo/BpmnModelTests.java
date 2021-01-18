package com.cqkj.activiti6demo;

import com.alibaba.fastjson.JSON;
import com.cqkj.activiti6demo.pojo.CustomFlowInfo;
import com.cqkj.activiti6demo.pojo.CustomGraphicInfo;
import com.cqkj.activiti6demo.pojo.FlowElementPojo;
import com.cqkj.activiti6demo.utils.ActivitiUtils;
import com.cqkj.activiti6demo.utils.CommonUtil;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.activiti.image.impl.DefaultProcessDiagramCanvas;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.activiti.validation.ProcessValidator;
import org.activiti.validation.ProcessValidatorFactory;
import org.activiti.validation.ValidationError;
import org.apache.commons.lang3.ObjectUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.*;

/**
 * 自定义代码生成流程model实例
 */
@Slf4j
@SpringBootTest
public class BpmnModelTests {

    @Autowired
    private ProcessEngine processEngine;

    /**
     * 创建开始节点信息
     * @param id
     * @param name
     * @return
     */
    public FlowElement createStartFlowElement(String id, String name){
        StartEvent startEvent = new StartEvent();
        startEvent.setId(id);
        startEvent.setName(name);
        return startEvent;
    }

    /**
     * 创建结束节点信息
     * @param id
     * @param name
     * @return
     */
    public FlowElement createEndFlowElement(String id, String name){
        EndEvent endEvent = new EndEvent();
        endEvent.setId(id);
        endEvent.setName(name);
        return endEvent;
    }

    /**
     * 创建普通任务节点信息
     * @param id
     * @param name
     * @param assignee
     * @return
     */
    public FlowElement createCommonUserTask(String id,String name,String assignee){
        UserTask userTask=new UserTask();
        userTask.setId(id);
        userTask.setName(name);
        userTask.setAssignee(assignee);
        return userTask;
    }

    /**
     * 查询各节点关联流转信息,即流转线
     *FlowElementPojo 是自定义类
     */
    public List<FlowElementPojo> createCirculationSequence(){


        List<FlowElementPojo> list=new ArrayList<>();
        FlowElementPojo flowElementPojo_start=new FlowElementPojo();
        flowElementPojo_start.setId("sequence_id_1");
        flowElementPojo_start.setTargetFlowElementId("USER_TASK_1"); // 结束
        flowElementPojo_start.setSourceFlowElementId("START_EVENT_ID"); // 起始
        flowElementPojo_start.setFlowElementType("sequence");

        FlowElementPojo flowElementPojo_user_0=new FlowElementPojo();
        flowElementPojo_user_0.setId("sequence_id_4");
        flowElementPojo_user_0.setTargetFlowElementId("END_EVENT_ID");
        flowElementPojo_user_0.setSourceFlowElementId("USER_TASK_1");
        flowElementPojo_user_0.setFlowElementType("sequence");

        list.add(flowElementPojo_start);
        list.add(flowElementPojo_user_0);

        return list;

    }

    /**
     * 绘制节点流转顺序
     * @param id 流转id
     * @param name 流转名称
     * @param targetId 目标节点id
     * @param sourceId 起始节点id
     * @param conditionExpression 流转el表达式
     * @return
     */
    public SequenceFlow createSequeneFlow(String id,String name,String sourceId,String targetId,String conditionExpression){
        SequenceFlow sequenceFlow=new SequenceFlow();
        sequenceFlow.setId(id);
        sequenceFlow.setName(name);
        if (ObjectUtils.isNotEmpty(targetId)){
            sequenceFlow.setTargetRef(targetId);
        }
        if (ObjectUtils.isNotEmpty(sourceId)){
            sequenceFlow.setSourceRef(sourceId);
        }
        if (ObjectUtils.isNotEmpty(conditionExpression)){
            sequenceFlow.setConditionExpression(conditionExpression);
        }
        return sequenceFlow;
    }

    @Test
    void CreateModel(){
        // 创建模型
        BpmnModel bpmnModel = new BpmnModel();
        // 流程信息
        Process process = new Process();
        process.setId("testModel");
        process.setName("测试流程图");
        process.setDocumentation("测试流程图");
        // 添加流程节点信息
        String startId = "START_EVENT_ID";
        String startName = "开始流程";
        String endId = "END_EVENT_ID";
        String endName = "结束流程";
        // 创建数据存储所有流程节点信息
        List<FlowElement> elementList = new ArrayList<>();
        // 创建开始节点
        FlowElement startFlowElement = createStartFlowElement(startId, startName);
        FlowElement endFlowElement = createEndFlowElement(endId, endName);
        elementList.add(startFlowElement);
        elementList.add(endFlowElement);
        
        FlowElement commonUserTask = createCommonUserTask("USER_TASK_1", "普通任务节点1", "testUser");
        elementList.add(commonUserTask);
        // 节点存入流程
        elementList.stream().forEach(flowElement -> process.addFlowElement(flowElement));

        List<FlowElementPojo> circulationSequence = createCirculationSequence();
        for (FlowElementPojo flowElementPojo : circulationSequence) {
            SequenceFlow sequenceFlow = createSequeneFlow(flowElementPojo.getId(), "流转", flowElementPojo.getSourceFlowElementId(), flowElementPojo.getTargetFlowElementId(), "");
            process.addFlowElement(sequenceFlow);
        }
        // 流程存入模型
        bpmnModel.addProcess(process);
        // 校验模型
        ProcessValidator defaultProcessValidator = new ProcessValidatorFactory().createDefaultProcessValidator();
        List<ValidationError> validationErrors = defaultProcessValidator.validate(bpmnModel);
        if (validationErrors.size()>0){
            throw new RuntimeException("流程有误，请检查后重试");
        }

        String fileName = "model_custom.bpmn20.xml";
        new BpmnAutoLayout(bpmnModel).execute();


        //bpmnModel 转换为标准的bpmn20 xml文件
        BpmnXMLConverter bpmnXMLConverter=new BpmnXMLConverter();
        byte[] convertToXML = bpmnXMLConverter.convertToXML(bpmnModel);
        String bytes=new String(convertToXML);
        System.out.println(bytes);

        // 部署流程
        Deployment deploy = processEngine.getRepositoryService().createDeployment().addBpmnModel(fileName, bpmnModel)
                .name("测试流程图")
                .key("testModel")
                .deploy();
    }

    /**
     * 根据流程id 获取当前流程信息
     */
    @Test
    void getBpmnDiagram(){

        Map<String, Object> result = new HashMap<>();
        List<CustomGraphicInfo> elements = new ArrayList<>(); // 保存节点对象
        List<CustomFlowInfo> flows = new ArrayList<>(); // 保存流对象
        String proc_inst_id = "22501";
        // 根据流程id获取流程实例
        HistoricProcessInstance processInstance = processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(proc_inst_id).singleResult();
        // 根据流程实例获取到流程模型
        BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(processInstance.getProcessDefinitionId());

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
        List<HistoricActivityInstance> historicActivityInstances = processEngine.getHistoryService().createHistoricActivityInstanceQuery().processInstanceId(proc_inst_id)
                .orderByHistoricActivityInstanceId().asc().list();

        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            String activityId = historicActivityInstance.getActivityId(); // 流程文件里定义的id 同 taskDefinitionKey
            completedTask.put(activityId, historicActivityInstance);
        }
        // 获取当前节点信息
        Task current_task = processEngine.getTaskService().createTaskQuery().processInstanceId(proc_inst_id).active().singleResult();

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

        System.out.println(JSON.toJSONString(result));
    }

}
