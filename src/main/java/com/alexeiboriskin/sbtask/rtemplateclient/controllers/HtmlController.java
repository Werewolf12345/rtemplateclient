package com.alexeiboriskin.sbtask.rtemplateclient.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HtmlController {
    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public ModelAndView loginPage(@RequestParam(value = "error",required = false) String error,
                                  @RequestParam(value = "logout",	required = false) String logout) {

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", "Invalid Credentials provided.");
            model.setViewName("signin");
        }

        if (logout != null) {
            model.addObject("message", "Logged out successfully.");
            model.setViewName("signin");
        }

        model.setViewName("signin");
        return model;
    }

    @RequestMapping("adminpanel")
    public String adminPanel() {
        return "adminpanel";
    }

    @RequestMapping("userpanel")
    public String userPanel() {
        return "userpanel";
    }
}
