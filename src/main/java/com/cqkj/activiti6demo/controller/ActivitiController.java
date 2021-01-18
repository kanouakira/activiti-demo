package com.cqkj.activiti6demo.controller;


import com.cqkj.activiti6demo.common.HttpResult;
import com.cqkj.activiti6demo.common.ResultCodeEnum;
import com.cqkj.activiti6demo.pojo.SubmitTask;
import com.cqkj.activiti6demo.pojo.TaskInfo;
import com.cqkj.activiti6demo.pojo.TaskRepresentation;
import com.cqkj.activiti6demo.service.ActivitiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Activiti6.0测试流程控制器
 */
@Api(tags = "Activiti6.0相关接口")
@RestController
@RequestMapping("/activiti")
public class ActivitiController {

    @Autowired
    private ActivitiService activitiService;

    @Resource
    private ProcessEngine processEngine;


    /**************
     * 待办相关服务 *
     **************/

    /**
     * 根据taskId获取待办信息
     * @param taskId
     * @return
     */
    @ApiOperation("根据taskId获取待办信息的接口")
    @GetMapping("/tasks/{taskId}")
    public HttpResult getTaskInfo(@PathVariable("taskId")String taskId){
        final TaskInfo taskInfo = activitiService.getTaskInfo(taskId);
        return HttpResult.success(taskInfo);
    }

    /**
     * 登录
     * @param userId 用户Id
     * @param password 密码
     * @return
     */
    @ApiOperation("登录的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", defaultValue = "testUser", required = true),
            @ApiImplicitParam(name = "password", value = "密码", defaultValue = "123456", required = true)
    })
    @PostMapping("/login")
    public HttpResult login(@RequestParam("userId")String userId,
                            @RequestParam("password")String password){

        return activitiService.verifyPassword(userId, password) == true ? HttpResult.success() : HttpResult.failure(ResultCodeEnum.VERIFY_ERROR);
    }

    /**
     * 新增用户
     * @param user
     * @return
     */
    @ApiOperation("新增用户")
    @PostMapping("/users")
    public HttpResult createUser(@RequestBody User user){
        activitiService.createUser(user);
        return HttpResult.success();
    }

    /**
     * 根据userId获取待办
     * @param userId userId
     * @return
     */
    @ApiOperation("根据userId获取待办的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", defaultValue = "testUser", required = true)
    })
    @GetMapping("/users/{userId}/tasks")
    public HttpResult task(@PathVariable(value = "userId")String userId){
        List<Task> tasks = activitiService.getTask(userId);
        List<TaskRepresentation> dtos = new ArrayList<>();
        for (Task task : tasks) {
            dtos.add(new TaskRepresentation(task.getId(),task.getName()));
        }
        return HttpResult.success(dtos);
    }

    /**
     * 根据taskId获取当前流程任务的表单
     * @param taskId
     * @return
     */
    @ApiOperation("根据taskId获取当前流程任务的表单")
    @GetMapping("/taskform")
    public HttpResult getTaskFormData(@RequestParam(value = "taskId")String taskId){
        return HttpResult.success(activitiService.getTaskFormData(taskId).getFormProperties());
    }

    /**
     * 根据表单提交完成待做任务
     */
    @ApiOperation("根据表单提交完成待做任务")
    @PostMapping("/completeTaskForm")
    public HttpResult completeTaskForm(@RequestBody SubmitTask submitTask){
        activitiService.completeFormTask(submitTask.getTaskId(), submitTask.getResults());
        return HttpResult.success();
    }

    /**************
     * 流程相关服务 *
     **************/

    /**
     * 根据key发起一个流程，并且根据userId设置发起人
     * @param userId 发起人的Id
     * @param procKey 流程的key
     * @return
     */
    @ApiOperation("发起流程的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "发起人的Id", defaultValue = "testUser", required = true),
            @ApiImplicitParam(name = "procKey", value = "流程的key", defaultValue = "FinanceProcess", required = true)
    })
    @GetMapping("/start")
    public HttpResult start(@RequestParam("userId")String userId,
                            @RequestParam("procKey")String procKey){
        return HttpResult.success(activitiService.start(userId, procKey));
    }


    /**
     * 根据流程实例id获取绘制流程图的信息
     * @param procInstId 流程实例id
     * @return
     */
    @ApiOperation("根据流程实例id获取绘制流程图的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "procInstId", value = "流程实例id", defaultValue = "", required = true),
    })
    @GetMapping("/procs/{procInstId}/diagram")
    public HttpResult start(@PathVariable(value = "procInstId")String procInstId){
        return HttpResult.success(activitiService.getBpmnDiagramInfo(procInstId));
    }

    /**
     * 查询所有已定义的流程信息
     * @return
     */
    @ApiOperation("查询所有已定义的流程信息的接口")
    @GetMapping("/procdefs")
    public HttpResult getProDefs(){
        return HttpResult.success(activitiService.findAllProcDef());
    }

    /**
     * 根据userId获取未结束流程
     * @param userId userId
     * @return
     */
    @ApiOperation("根据userId获取未结束流程的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", defaultValue = "testUser", required = true)
    })
    @GetMapping("/users/{userId}/unFinishedProc")
    public HttpResult unFinishedProc(@PathVariable(value = "userId")String userId){
        return HttpResult.success(activitiService.getUnFinishedProc(userId));
    }

    /**
     * 根据userId获取已结束流程
     * @param userId userId
     * @return
     */
    @ApiOperation("根据userId获取已结束流程的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", defaultValue = "testUser", required = true)
    })
    @GetMapping("/users/{userId}/finishedProc")
    public HttpResult finishedProc(@PathVariable(value = "userId")String userId){
        return HttpResult.success(activitiService.getFinishedProc(userId));
    }

    /**
     * 根据procInstId取消流程
     * @param procInstId 流程实例ID
     * @return
     */
    @ApiOperation("根据流程实例ID取消流程的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "procInstId", value = "流程实例ID", required = true)
    })
    @DeleteMapping("/procs/{procInstId}")
    public HttpResult delProcInst(@PathVariable(value = "procInstId")String procInstId){
        activitiService.delProcInst(procInstId);
        return HttpResult.success("取消成功");
    }

    /**************
     * 附件相关服务 *
     **************/

    /**
     * 根据taskId获取流程附件信息
     */
    @ApiOperation("根据taskId获取流程附件信息")
    @GetMapping("/tasks/{taskId}/attachments")
    public HttpResult getTaskAttachment(@PathVariable("taskId")String taskId){
        return HttpResult.success(activitiService.getTaskAttachments(taskId));
    }

    /**
     * 根据taskId和输入流对象添加附件到流程
     * @param taskId
     * @param files
     * @return 成功返回attachmentId，失败返回null
     */
    @ApiOperation("根据taskId添加附件到流程")
    @PostMapping(value = "/attachments" , headers = "content-type=multipart/form-data")
    public HttpResult setAttachment(@RequestParam("taskId")String taskId, @RequestParam("files") List<MultipartFile> files){
        List<String> results = new ArrayList<>();
        // 遍历上传的文件集合
        for (MultipartFile file : files) {
            try {
                // 添加结果attachmentId
                String result = null;
                // 添加附件
                Attachment attachment = activitiService.setTaskAttachment(taskId, file.getOriginalFilename(), "None", file.getInputStream());
                // 添加失败
                if (attachment != null){
                    result = attachment.getId();
                }
                results.add(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return HttpResult.success(results);
    }

    /**
     * 根据附件id删除附件
     * @param attachmentId 附件id
     */
    @ApiOperation("根据附件id删除附件")
    @DeleteMapping("/attachments/{attachmentId}")
    public HttpResult delAttachment(@PathVariable("attachmentId")String attachmentId){
        try {
            activitiService.delTaskAttachment(attachmentId);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return HttpResult.success();
    }

    /**
     * 根据附件id下载附件
     * @param attachmentId 附件id
     */
    @ApiOperation("根据附件id下载附件")
    @GetMapping("/attachments/{attachmentId}")
    public void downloadAttachment(HttpServletResponse res, @PathVariable("attachmentId")String attachmentId){
        final Attachment attachment = activitiService.getAttachment(attachmentId);
        String fileName = attachment.getName();
        res.setHeader("content-type", "application/octet-stream");
        res.setContentType("application/octet-stream");
        try {
            res.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("utf-8"),"ISO8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = res.getOutputStream();
            bis=new BufferedInputStream(processEngine.getTaskService().getAttachmentContent(attachmentId));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**************
     * 部署相关服务 *
     **************/

    /**
     * 根据xml文件部署新的流程
     * @param file
     * @return
     */
    @PostMapping("/deploy")
    public HttpResult deploy(@RequestParam("files") MultipartFile file){
        try {
            String name = file.getOriginalFilename();
            if (!name.endsWith(".bpmn20.xml") && !name.endsWith(".bpmn")) {
                return HttpResult.failure(ResultCodeEnum.DEPLOY_ERROR);
            }
            return HttpResult.success(activitiService.deploy(file.getOriginalFilename(), file.getInputStream()));
        } catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException("upload failed");
        }
    }

    /**
     * 删除已部署的流程信息
     * @param deploymentId 部署id
     * @return
     */
    @DeleteMapping("/deploy/{deploymentId}")
    public HttpResult delDeployment(@PathVariable("deploymentId")String deploymentId){
        // 第二个参数代表级联操作
        processEngine.getRepositoryService().deleteDeployment(deploymentId, true);
        return HttpResult.success();
    }
}
