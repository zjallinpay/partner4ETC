package com.allinpay.controller;

import com.allinpay.entity.PartnerInfo;
import com.allinpay.entity.TEtcSysMenu;
import com.allinpay.entity.TEtcSysRole;
import com.allinpay.service.IOrgQueryService;
import com.allinpay.service.ITEtcSysMenuService;
import com.allinpay.service.ITEtcSysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 系统页面视图
 *
 * @author 吴超
 */
@Controller
@RequestMapping("/manage")
public class TEtcPageController {

    @Autowired
    private ITEtcSysRoleService sysRoleService;

    @Autowired
    private ITEtcSysMenuService sysMenuService;

    @Autowired
    private IOrgQueryService iOrgQueryService;

    @RequestMapping(value = {"/", "login.html"})
    public String login() {
        return "login";
    }
//
//    @RequestMapping(value = {"/user/index", "/index"})
//    public String index() {
//        return "common/index";
//    }

    @RequestMapping(value = "/main.html")
    public String main() {
        return "common/main";
    }

    @RequestMapping("/{module}/{url}.html")
    public String module(@PathVariable("module") String module, @PathVariable("url") String url) {
        return module + "/" + url;
    }

    @RequestMapping("/userManage")
    public String userManage() {
        return "backstage/userindex";
    }

    @GetMapping("/addUser")
    public ModelAndView userAdd() {
        List<TEtcSysRole> tEtcSysRoles = sysRoleService.list(new QueryWrapper<TEtcSysRole>().eq("status", 1).orderByDesc("ROLE_ID"));
        List<PartnerInfo> partnerInfos = iOrgQueryService.selectByNormalStatus();//合作机构信息
        ModelAndView modelAndView = new ModelAndView("backstage/operation/addUser");
        modelAndView.addObject("roles", tEtcSysRoles);
        modelAndView.addObject("partners", partnerInfos);
        return modelAndView;
    }

    @GetMapping("/editUser")
    public ModelAndView userUpdate(String userId, String roleIds, String partnerId, String opreate, String status) {
        List<TEtcSysRole> tEtcSysRoles = sysRoleService.list();
        List<PartnerInfo> partnerInfos = iOrgQueryService.selectByNormalStatus();//合作机构信息
        ModelAndView modelAndView = new ModelAndView("backstage/operation/editUser");
        modelAndView.addObject("userId", userId);
        modelAndView.addObject("roleIds", roleIds);
        modelAndView.addObject("opreate", opreate);
        modelAndView.addObject("status", status);
        modelAndView.addObject("roles", tEtcSysRoles);
        modelAndView.addObject("partners", partnerInfos);
        modelAndView.addObject("partnerIds", partnerId);
        return modelAndView;
    }


    @RequestMapping("/roleManage")
    public String roleManage() {
        return "backstage/roleManage";
    }

    @GetMapping("/addRole")
    public ModelAndView roleAdd() {
        ModelAndView modelAndView = new ModelAndView("backstage/operation/addRole");
        return modelAndView;
    }


    @GetMapping("/editRole")
    public ModelAndView editRole(Integer roleId) {
        ModelAndView modelAndView = new ModelAndView("backstage/operation/addRole");
        TEtcSysRole role = sysRoleService.getById(roleId);
        modelAndView.addObject("role", role);
        modelAndView.addObject("roleName", role.getRoleName());
        modelAndView.addObject("roleId", role.getRoleId());
        modelAndView.addObject("status", role.getStatus());
        modelAndView.addObject("opreate", "edit");
        return modelAndView;
    }


    @RequestMapping("/menuManage")
    public String menuManage() {
        return "backstage/menuManage";
    }

    @GetMapping("/addMenu")
    public ModelAndView menuAdd() {
        ModelAndView modelAndView = new ModelAndView("backstage/operation/addMenu");
        List<TEtcSysMenu> menus = sysMenuService.menuList();
        modelAndView.addObject("menus", menus);
        modelAndView.addObject("menu", new TEtcSysMenu());
        return modelAndView;

    }

    @GetMapping("/editMenu")
    public ModelAndView menuEdit(Integer menuId) {
        ModelAndView modelAndView = new ModelAndView("backstage/operation/addMenu");
        List<TEtcSysMenu> menus = sysMenuService.menuList();
        TEtcSysMenu menu = sysMenuService.getById(menuId);
        modelAndView.addObject("menus", menus);
        modelAndView.addObject("menu", menu);
        modelAndView.addObject("operate", "edit");
        return modelAndView;
    }


    //跳转页面
    @GetMapping("/activityPage")
    public ModelAndView activityPage(String proId,String proName) {
        ModelAndView modelAndView = new ModelAndView("yxpt/ActivityManager");
        modelAndView.addObject("proId", proId);
        modelAndView.addObject("proName", proName);
        return modelAndView;
    }

    //跳转页面
    @GetMapping("/acwechatdataPage")
    public ModelAndView acwechatdataPage(String acBatchId,String activityName) {
        ModelAndView modelAndView = new ModelAndView("yxpt/WechatDataManager");
        modelAndView.addObject("acBatchId", acBatchId);
        modelAndView.addObject("activityName", activityName);
        return modelAndView;
    }

    //跳转页面
    @GetMapping("/acallinpaydataPage")
    public ModelAndView acallinpaydataPage(String acBatchId,String activityName) {
        ModelAndView modelAndView = new ModelAndView("yxpt/AllinpayDataManager");
        modelAndView.addObject("acBatchId", acBatchId);
        modelAndView.addObject("activityName", activityName);
        return modelAndView;
    }


    //跳转页面
    @GetMapping("/statsRecordPage")
    public ModelAndView statsRecordPage(String acBatchId,String activityName) {
        ModelAndView modelAndView = new ModelAndView("yxpt/StatsRecordManager");
        modelAndView.addObject("acBatchId", acBatchId);
        modelAndView.addObject("activityName", activityName);
        return modelAndView;
    }


    //跳转页面
    @GetMapping("/connectAgreePage")
    public ModelAndView connectAgreePage(String proId) {
        ModelAndView modelAndView = new ModelAndView("yxpt/ConnectAgreeManager");
        modelAndView.addObject("proId", proId);
        return modelAndView;
    }
}
