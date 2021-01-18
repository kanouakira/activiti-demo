package com.cqkj.activiti6demo.pojo;

import lombok.Data;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.impl.persistence.entity.ByteArrayRef;
import org.activiti.engine.impl.persistence.entity.UserEntity;

/**
 * 自定义Activiti用户实体类
 */
@Data
public class CustomUser implements UserEntity {
    private String ID_;
    private String PASSWORD_;
    private String EMAIL_;

    @Override
    public Picture getPicture() {
        return null;
    }

    @Override
    public void setPicture(Picture picture) {

    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String s) {

    }

    @Override
    public String getFirstName() {
        return null;
    }

    @Override
    public void setFirstName(String s) {

    }

    @Override
    public String getLastName() {
        return null;
    }

    @Override
    public void setLastName(String s) {

    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public void setEmail(String s) {

    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public void setPassword(String s) {

    }

    @Override
    public boolean isPictureSet() {
        return false;
    }

    @Override
    public ByteArrayRef getPictureByteArrayRef() {
        return null;
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
}
