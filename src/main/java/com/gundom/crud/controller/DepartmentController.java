package com.gundom.crud.controller;

import com.gundom.crud.bean.Department;
import com.gundom.crud.bean.Msg;
import com.gundom.crud.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @RequestMapping("/depts")
    @ResponseBody
    public Msg getDepts(){
        List<Department> list =departmentService.getDepts();

        return Msg.success().add("depts",list);
    }

}
