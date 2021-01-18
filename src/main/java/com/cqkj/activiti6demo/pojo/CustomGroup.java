package com.cqkj.activiti6demo.pojo;

import lombok.Data;
import org.activiti.engine.impl.persistence.entity.GroupEntity;

/**
 * 自定义activiti用户组
 */
@Data
public class CustomGroup implements GroupEntity {
    private String ID_;
    private String NAME_;
    private String DESCRIPTION_;
    private Integer STATUS_;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String s) {

    }

    @Override
    public boolean isInserted() {
        return false;
    }

    @Override
    public void setInserted(boolean b) {

    }

    @Override
    public boolean isUpdated() {
        return false;
    }

    @Override
    public void setUpdated(boolean b) {

    }

    @Override
    public boolean isDeleted() {
        return false;
    }

    @Override
    public void setDeleted(boolean b) {

    }

    @Override
    public Object getPersistentState() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String s) {

    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public void setType(String s) {

    }

    @Override
    public void setRevision(int i) {

    }

    @Override
    public int getRevision() {
        return 0;
    }

    @Override
    public int getRevisionNext() {
        return 0;
    }
}
