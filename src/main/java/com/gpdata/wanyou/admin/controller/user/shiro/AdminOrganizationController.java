package com.gpdata.wanyou.admin.controller.user.shiro;

import com.alibaba.fastjson.JSON;
import com.gpdata.wanyou.admin.entity.AdminOrganization;
import com.gpdata.wanyou.admin.service.AdminOrganizationService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 组织
 */
@Controller
@RequestMapping("/organization")
public class AdminOrganizationController extends AdminBaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminOrganizationController.class);

    @Autowired
    private AdminOrganizationService organizationService;

    @RequiresPermissions("organization:view")
    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        return "role/index";
    }

    @RequiresPermissions("organization:view")
    @RequestMapping(value = "/tree")
    public void showTree(HttpServletRequest request, HttpServletResponse response) {

        try {
            List<AdminOrganization> organizationList = organizationService.findAll();
            response.getWriter().write(JSON.toJSONString(organizationList));
        } catch (Exception e) {
            LOGGER.error("Exception:{}", e);
        }
    }

    @RequiresPermissions("organization:create")
    @RequestMapping(value = "/{parentId}/appendChild", method = RequestMethod.GET)
    public String showAppendChildForm(@PathVariable("parentId") Long parentId, Model model) {
        AdminOrganization parent = organizationService.findOne(parentId);
        model.addAttribute("parent", parent);
        AdminOrganization child = new AdminOrganization();
        child.setParentId(parentId);
        child.setParentIds(parent.makeSelfAsParentIds());
        model.addAttribute("child", child);
        model.addAttribute("op", "新增");
        return "role/appendChild";
    }

    @RequiresPermissions("organization:create")
    @RequestMapping(value = "/{parentId}/appendChild", method = RequestMethod.POST)
    public String create(AdminOrganization organization) {
        organizationService.createOrganization(organization);
        return "redirect:/admin/organization/success";
    }

    @RequiresPermissions("organization:update")
    @RequestMapping(value = "/{id}/maintain", method = RequestMethod.GET)
    public String showMaintainForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("organization", organizationService.findOne(id));
        return "role/maintain";
    }

    @RequiresPermissions("organization:update")
    @RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
    public String update(AdminOrganization organization, RedirectAttributes redirectAttributes) {
        organizationService.updateOrganization(organization);
        redirectAttributes.addFlashAttribute("msg", "修改成功");
        return "redirect:/admin/organization/success";
    }

    @RequiresPermissions("organization:delete")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        organizationService.deleteOrganization(id);
        redirectAttributes.addFlashAttribute("msg", "删除成功");
        return "redirect:/admin/organization/success";
    }

    @RequiresPermissions("organization:update")
    @RequestMapping(value = "/{sourceId}/move", method = RequestMethod.GET)
    public String showMoveForm(@PathVariable("sourceId") Long sourceId, Model model) {
        AdminOrganization source = organizationService.findOne(sourceId);
        model.addAttribute("source", source);
        model.addAttribute("targetList", organizationService.findAllWithExclude(source));
        return "role/move";
    }

    @RequiresPermissions("organization:update")
    @RequestMapping(value = "/{sourceId}/move", method = RequestMethod.POST)
    public String move(@PathVariable("sourceId") Long sourceId,
                       @RequestParam("targetId") Long targetId) {
        AdminOrganization source = organizationService.findOne(sourceId);
        AdminOrganization target = organizationService.findOne(targetId);
        organizationService.move(source, target);
        return "redirect:/admin/organization/success";
    }

    @RequiresPermissions("organization:view")
    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public String success() {
        return "role/success";
    }

}
