package ru.org.sevn.entity;

public class DbEntity {
    private Long createTime;
    private Long updateTime;

    public Long getCreateTime () {
        return createTime;
    }

    public void setCreateTime (Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime () {
        return updateTime;
    }

    public void setUpdateTime (Long updateTime) {
        this.updateTime = updateTime;
    }

}
