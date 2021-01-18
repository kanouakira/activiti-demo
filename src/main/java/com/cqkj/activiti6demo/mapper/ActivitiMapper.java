package com.cqkj.activiti6demo.mapper;

import com.cqkj.activiti6demo.pojo.CustomGroup;
import com.cqkj.activiti6demo.pojo.CustomUser;
import com.cqkj.activiti6demo.pojo.ProcDef;
import com.cqkj.activiti6demo.pojo.TaskInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Activiti6的持久层接口
 */
@Mapper
public interface ActivitiMapper {
    /**
     * 根据用户id返回用户信息
     * @param userId 用户id
     * @return
     */
    CustomUser findUserById(String userId);

    /**
     * 根据用户id返回该用户所在的所有组
     * @param userId 用户id
     * @return
     */
    List<CustomGroup> findGroupsByUserId(String userId);

    /**
     * 根据taskId获取待办任务信息
     * @param taskId
     * @return
     */
    TaskInfo findTaskInfo(String taskId);

    /**
     * 查询所有已定义的流程信息
     * @return
     */
    List<ProcDef> findAllProcDef();
}
