package com.gpdata.wanyou.admin.entity;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 管理员用户
 *
 * @author guo
 */
public class AdminUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long adminId;

    private String adminUsername;
    private String adminLoginname;

    private String adminPassword;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date adminRegisttime;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date adminLastlogintime;
    private String adminRoleid;
    @Transient
    private List<Long> adminRoleids;// 拥有的角色列表
    private Long organizationId;

    private Boolean locked;

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAdminLoginname() {
        return adminLoginname;
    }

    public void setAdminLoginname(String adminLoginname) {
        this.adminLoginname = adminLoginname;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public Date getAdminRegisttime() {
        return adminRegisttime;
    }

    public void setAdminRegisttime(Date adminRegisttime) {
        this.adminRegisttime = adminRegisttime;
    }

    public Date getAdminLastlogintime() {
        return adminLastlogintime;
    }

    public void setAdminLastlogintime(Date adminLastlogintime) {
        this.adminLastlogintime = adminLastlogintime;
    }

    public String getAdminRoleid() {
        return adminRoleid;
    }

    public void setAdminRoleid(String adminRoleid) {
        this.adminRoleid = adminRoleid;
    }

    public List<Long> getAdminRoleids() {
        if (adminRoleids == null) {
            adminRoleids = new ArrayList<Long>();
        }
        return adminRoleids;
    }

    public void setAdminRoleids(List<Long> adminRoleids) {
        this.adminRoleids = adminRoleids;
    }

    public String getRoleIdsStr() {
        if (CollectionUtils.isEmpty(adminRoleids)) {
            return "";
        }
        StringBuilder s = new StringBuilder();
        for (Long roleId : adminRoleids) {
            s.append(roleId);
            s.append(",");
        }
        return s.toString();
    }

    public void setRoleIdsStr(String roleIdsStr) {
        if (StringUtils.isEmpty(roleIdsStr)) {
            return;
        }
        String[] roleIdStrs = roleIdsStr.split(",");
        for (String roleIdStr : roleIdStrs) {
            if (StringUtils.isEmpty(roleIdStr)) {
                continue;
            }
            getAdminRoleids().add(Long.valueOf(roleIdStr));
        }
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }
}