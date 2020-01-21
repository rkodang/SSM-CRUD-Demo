package com.gundom.crud.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gundom.crud.bean.Employee;
import com.gundom.crud.bean.Msg;
import com.gundom.crud.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @RequestMapping(value="emp/{ids}",method = RequestMethod.DELETE)
    @ResponseBody
    public Msg deleteEmpById(@PathVariable("ids") String ids){
        List<Integer> del_ids=new ArrayList<>();
        if(ids.contains("-")){
            String[] str_ids = ids.split("-");
            for (String str_id : str_ids) {
                int i = Integer.parseInt(str_id);
                del_ids.add(i);
            }
           employeeService.deleteBatch(del_ids);
        }else{
            Integer id = Integer.parseInt(ids);
            employeeService.deleteEmp(id);
        }
        return Msg.success();
    }


    @RequestMapping(value="/emp/{empId}",method = RequestMethod.PUT)
    @ResponseBody
    public Msg saveEmp(Employee employee){
        employeeService.updateEmp(employee);
        return Msg.success();
    }


    @RequestMapping(value = "/emp/{id}",method = RequestMethod.GET)
    @ResponseBody
    public Msg getEmp(@PathVariable("id") Integer id){
        Employee emp=employeeService.getEmp(id);
        return Msg.success().add("emp",emp);
    }


    @RequestMapping(value="/checkuser",method = RequestMethod.POST)
    @ResponseBody
    public Msg checkuser(String empName){
        String regex = "(^[A-Za-z0-9]{6,16}$)|(^[\\u2E80-\\u9FFF]{2,5}$)";
        if (!empName.matches(regex)) {
            return Msg.fail().add("va_msg", "名字必须是2-5个中文或者6-16位英文数字组合");
        }

        boolean b= employeeService.cheuckUser(empName);
       if(b){
           return Msg.success();
       }else{
           return Msg.fail().add("va_msg", "用户名被占用");
       }
    }

    /**
     * 员工保存
     * @return
     */
    @RequestMapping(value="/emp",method = RequestMethod.POST)
    @ResponseBody
    public Msg saveEmp(@Valid Employee employee, BindingResult result) {
        Map<String,Object> map=new HashMap<>();
        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError error : errors) {
                map.put(error.getField(),error.getDefaultMessage());
            }
            return  Msg.fail().add("errorFields",map);
        } else {
            employeeService.saveEmp(employee);
            return Msg.success();
        }
    }

    @RequestMapping("/emps")
    @ResponseBody
    public Msg getEmpsWithJson(@RequestParam(value="pn" ,defaultValue="1") Integer pn){
//这不是一个分页查询
        //引入PageHelper分页插件
        //查询之前只需要调用,传入页码,以及每页的大小
        PageHelper.startPage(pn,5);
        //startPage后面紧跟的这个查询就是一个分页查询
        List<Employee> emps=employeeService.getAll();
        //使用pageinfo来包装查询后的结果,只需要将pageinfo交给页面即可
        //pageinfo封装了详细的分页信息,包括我们查询出来的数据,传入连续显示的页数
        PageInfo page = new PageInfo(emps,5);
        return Msg.success().add("pageInfo",page);

    }
//    @RequestMapping("/emps")
    public String getEmps(@RequestParam(value="pn" ,defaultValue="1") Integer pn, Model model){
        //这不是一个分页查询
        //引入PageHelper分页插件
        //查询之前只需要调用,传入页码,以及每页的大小
        PageHelper.startPage(pn,5);
        //startPage后面紧跟的这个查询就是一个分页查询
        List<Employee> emps=employeeService.getAll();
        //使用pageinfo来包装查询后的结果,只需要将pageinfo交给页面即可
        //pageinfo封装了详细的分页信息,包括我们查询出来的数据,传入连续显示的页数
        PageInfo page = new PageInfo(emps,5);
        model.addAttribute("pageInfo",page);
        return "list";
    }
}
