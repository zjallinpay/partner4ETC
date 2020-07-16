package com.allinpay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manage")
/**
 * @description: 页面转发controller
 * @author: tanguang
 * @date: 2019-07-02 19:18
 */
public class RedirectController {
    @RequestMapping("/orgEnter")
    public String orgEnter() {
        return "orgmanage/orgEnter";
    }

    @RequestMapping("/orgModify")
    public String orgModify() {
        return "orgmanage/orgModify";
    }

    @RequestMapping("/orgAudit")
    public String orgAudit() {
        return "orgmanage/orgAudit";
    }

    @RequestMapping("/orgquery")
    public String orgQuery() {
        return "dataquery/orgQuery";
    }

    @RequestMapping("/orgLogout")
    public String orgLogout() {
        return "orgmanage/orgLogout";
    }

    @RequestMapping("/orgBlocking")
    public String orgBlocking() {
        return "orgmanage/orgBlocking";
    }

    @RequestMapping("/userHairpin")
    public String userHairpin() {
        return "dataquery/userhairpin";
    }

    @RequestMapping("/passageMoney")
    public String passageMoney() {
        return "dataquery/passagemoney";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/403")
    public String forbidden() {
        return "common/403";
    }

    @RequestMapping("/index")
    public String index() {
        return "common/index";
    }

    @RequestMapping("/bank")
    public String bank() {
        return "bank/bankinfo";
    }

    @RequestMapping("/orgbank")
    public String orgbank() {
        return "bank/orgbankmap";
    }

    @RequestMapping("/orgdeposit")
    public String orgdeposit() {
        return "bank/orgdeposit";
    }

    @RequestMapping("/notify")
    public String notiry() {
        return "bank/notifyinfo";
    }

    @RequestMapping("/stall")
    public String stall() {
        return "gwj/stallmanage";
    }

    @RequestMapping("/orderquery")
    public String order() {
        return "gwj/orderquery";
    }

    @RequestMapping("/hzcc/employee")
    public String employee() {
        return "hzcc/employee";
    }
}
