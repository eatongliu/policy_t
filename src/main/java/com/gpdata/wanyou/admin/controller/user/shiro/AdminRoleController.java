package com.gpdata.wanyou.admin.controller.user.shiro;

import com.alibaba.fastjson.JSON;
import com.gpdata.wanyou.admin.entity.AdminResource;
import com.gpdata.wanyou.admin.entity.AdminRole;
import com.gpdata.wanyou.admin.service.AdminResourceService;
import com.gpdata.wanyou.admin.service.AdminRoleService;
import com.gpdata.wanyou.base.controller.AdminBaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色
 */
@Controller
@RequestMapping("/role")
public class AdminRoleController extends AdminBaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminRoleController.class);
    @Autowired
    private AdminRoleService roleService;

    @Autowired
    private AdminResourceService resourceService;

    /**
     * 分页
     * <p>
     *
     * @param model
     * @param offset
     * @param limit
     * @author guoxy 2016年7月5日下午3:44:52
     */
    @RequiresPermissions("role:view")
    @RequestMapping(method = RequestMethod.GET)
    public void list(Model model, Long offset, Long limit, HttpServletRequest request,
                     HttpServletResponse response) {
        // model.addAttribute("roleList", roleService.findAll(offset, limit));
        try {
            Map<String, Object> maps = new HashMap<String, Object>();
            List<AdminRole> roles = roleService.findAll(offset, limit);
            int count = roleService.findAllCount();
            maps.put("rows", roles);
            maps.put("total", count);
            response.getWriter().write(JSON.toJSONString(maps));
        } catch (Exception e) {
            LOGGER.error("Exception:{}", e);
        }
    }

    /**
     * 权限列表菜单
     * <p>
     *
     * @param model
     * @return
     * @author guoxy
     * </p>
     * <p>
     * 2016年7月5日下午3:45:10
     * </p>
     */
    @RequiresPermissions("role:view")
    @RequestMapping(value = "/rolemenus")
    public void listAllRolesMenu(HttpServletRequest request, HttpServletResponse response) {
        try {
            /**
             * 权限列表
             */
            List<AdminResource> menuschild = resourceService.findAll();
            Map<Object, Object> maps = new HashMap<Object, Object>();
            for (AdminResource root : menuschild) {
                if (root.getParentId() != 0 && root.getId() != 1) {
                    List mls = new ArrayList();
                    for (AdminResource child : menuschild) {
                        if (child.getParentId() == root.getId()
                                || child.getParentId().equals(root.getId())) {
                            mls.add(child);
                        }
                    }
                    // 菜单
                    if (mls.size() != 0) {
                        maps.put(root.getId() + ":" + root.getName(), mls);
                    }

                }

            }
            LOGGER.debug("maps{}", maps);
            response.getWriter().write(JSON.toJSONString(maps));
        } catch (Exception e) {
            LOGGER.error("Exception:{}", e);
        }

    }

    /**
     * 页面跳转
     */
    @RequiresPermissions("role:create")
    @RequestMapping(value = "/torole", method = RequestMethod.GET)
    public String toRole() {
        return "role/create";
    }

    /**
     * 增加和修改
     *
     * @param role
     * @param request
     * @param response
     * @author guoxy 2016年7月6日下午2:22:00
     */
    @RequiresPermissions("role:create")
    @RequestMapping(value = "/create")
    public void create(AdminRole role, HttpServletRequest request, HttpServletResponse response) {
        try {
            AdminRole newrole = null;
            if (role.getId() != null) {
                newrole = roleService.updateRole(role);

            } else {
                newrole = roleService.createAdminRole(role);
            }
            response.getWriter().write(JSON.toJSONString(newrole));
        } catch (Exception e) {
            LOGGER.error("Exception:{}", e);
        }

    }

    /**
     * 得到详情
     * <p>
     *
     * @param id
     * @param request
     * @param response
     * @author guoxy 2016年7月6日下午2:22:34
     */
    @RequiresPermissions("role:update")
    @RequestMapping(value = "/{id}/update")
    public void showUpdateForm(@PathVariable("id") Long id, HttpServletRequest request,
                               HttpServletResponse response) {

        try {
            AdminRole role = roleService.findOne(id);
            response.getWriter().write(JSON.toJSONString(role));
        } catch (Exception e) {
            LOGGER.error("Exception:{}", e);
        }
    }

    /**
     * 删除权限
     *
     * @param id
     * @param request
     * @param response
     * @author guoxy 2016年7月6日下午2:23:40
     */
    @RequiresPermissions("role:delete")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public void delete(@PathVariable("id") Long id, HttpServletRequest request,
                       HttpServletResponse response) {
        try {
            int flag = roleService.deleteRole(id);
            if (flag == 1) {
                response.getWriter().write("success");
            } else {
                response.getWriter().write("error");
            }

        } catch (Exception e) {
            LOGGER.error("Exception:{}", e);
        }

    }

    private void setCommonData(Model model) {
        model.addAttribute("resourceList", resourceService.findAll());
    }

}
