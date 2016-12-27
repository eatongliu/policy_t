package com.gpdata.wanyou.tk.entity;

import com.gpdata.wanyou.utils.PinyinUtil;

import javax.persistence.*;

/**
 * 任务信息表 task_job
 * Created by guoxy on 2016/10/25.
 */
@Entity
@Table(name = "task_job")
public class TaskJob {
    /**
     * 任务id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer taskId;
    /**
     * 任务名称,英文，作为文件名使用
     */
    private String taskName;
    /**
     * 重试次数,自动尝试失败的次数 默认1次
     */
    private Integer retries;
    /**
     * 重试间隔,每次重试尝试之间的毫秒时间，默认无
     */
    private Long retryBackoff;
    /**
     * 类型,如 command
     */
    private String type;
    /**
     * 命令，根据命令类型选择的类型编写具体的命令
     */
    private String command;
    /**
     * 依赖，第一个job不可选依赖关系，第二个job可以依赖第一个
     */
    private String dependencies;
    /**
     * 模板id
     */
    private Integer templateId;

    /**
     * 备注说明
     *
     * @return
     */
    private String remark;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Long getRetryBackoff() {
        return retryBackoff;
    }

    public void setRetryBackoff(Long retryBackoff) {
        this.retryBackoff = retryBackoff;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getDependencies() {
        return dependencies;
    }

    public void setDependencies(String dependencies) {
        this.dependencies = dependencies;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "TaskJob{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", retries=" + retries +
                ", retryBackoff=" + retryBackoff +
                ", type='" + type + '\'' +
                ", command='" + command + '\'' +
                ", dependencies='" + dependencies + '\'' +
                ", templateId=" + templateId +
                ", remark='" + remark + '\'' +
                '}';
    }

    public String toJobTemp() {
        String j_tep = "";
        if (dependencies != null && dependencies.length() != 0) {
            j_tep += "\ndependencies=" + PinyinUtil.getPin(dependencies);
        }
        if (retries != null) {
            j_tep += "\nretries=" + retries;
        }
        if (retries != null) {
            j_tep += "\nretry.backoff=" + retries;
        }
        return "#说明： " + remark +
                j_tep +
                "\ntype=" + type +
                "\ncommand=" + command;
    }
}
