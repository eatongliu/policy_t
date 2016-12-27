package com.gpdata.wanyou.user.entity;

import javax.persistence.*;
import java.util.Date;


/**
 * 用户消息表
 *
 * @author MOMO
 */
@Entity
@Table(name = "user_message")
public class UserMessage {
    /*消息id 主键  自增*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /*
     * 用户ID
     */
    private String userId;


    /*
     * 用户名
     */
    private String userName;

    /*
     * 用户提问
     */
    @Column(columnDefinition = "text")
    private String userQuestion;

    /*
     * 提问时间
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date qTime;

    /*
     * 回复方
     */
    private String answerMan;

    /*
     * 回答
     */
    @Column(columnDefinition = "text")
    private String answer;

    /*
     * 回答时间
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date aTime;

    /*
     * 是否回复 0：未回复，1：回复
     */
    private String isAnswer;

    /*
     * 是否展示： 0 不展示 1 展示
     */
    private String isShow;
    /*
     *是否已读 0 未读 1 已读
     */
    private String isRead;
    /*消息类型   订阅消息   系统消息 等*/
    private String qType;
    /*备注*/
    @Column(columnDefinition = "text")
    private String remark;

    /*消息状态 0 待查阅； 1 已查看；2 待处理；3 已备注；4已处理；*/
    private String qStatus;
    /*是否重点*/
    private String isPoint;
    /*是否解决*/
    private String isResolve;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserQuestion() {
        return userQuestion;
    }

    public void setUserQuestion(String userQuestion) {
        this.userQuestion = userQuestion;
    }

    public Date getqTime() {
        return qTime;
    }

    public void setqTime(Date qTime) {
        this.qTime = qTime;
    }

    public String getAnswerMan() {
        return answerMan;
    }

    public void setAnswerMan(String answerMan) {
        this.answerMan = answerMan;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Date getaTime() {
        return aTime;
    }

    public void setaTime(Date aTime) {
        this.aTime = aTime;
    }

    public String getIsAnswer() {
        return isAnswer;
    }

    public void setIsAnswer(String isAnswer) {
        this.isAnswer = isAnswer;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getqType() {
        return qType;
    }

    public void setqType(String qType) {
        this.qType = qType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getqStatus() {
        return qStatus;
    }

    public void setqStatus(String qStatus) {
        this.qStatus = qStatus;
    }

    public String getIsPoint() {
        return isPoint;
    }

    public void setIsPoint(String isPoint) {
        this.isPoint = isPoint;
    }

    public String getIsResolve() {
        return isResolve;
    }

    public void setIsResolve(String isResolve) {
        this.isResolve = isResolve;
    }
}
