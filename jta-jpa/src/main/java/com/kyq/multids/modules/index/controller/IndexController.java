package com.kyq.multids.modules.index.controller;

import com.kyq.multids.modules.index.service.MultiDataSourceTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Description： com.kyq.multids.modules.index.controller
 *
 * @author kyq1024
 * @version 1.0
 * @timestamp 2021-08-18 10:00
 */
@Controller
public class IndexController {
    @Autowired
    MultiDataSourceTestService multiDataSourceTestService;

    @RequestMapping("/")
    @ResponseBody
    public String getHello() {
        return "Hello MultiDs";
    }


    @RequestMapping("/multiSave")
    @ResponseBody
    public Map<String, Object> testSave(){
        Map<String, Object> ret = new HashMap<String, Object>(4);
        multiDataSourceTestService.modifyDataAcrossDs();
        ret.put("msg","保存成功");
        return ret;
    }

    @RequestMapping("/multiQuery")
    @ResponseBody
    public Map<String, Object> testQuery(){
        return multiDataSourceTestService.findDataAcrossDs();
    }

    @RequestMapping("/multiJpaQuery")
    @ResponseBody
    public Map<String, Object> testJpaQuery(){
        return multiDataSourceTestService.findDataAcrossDsByJpa();
    }

    @RequestMapping("/multiErrorSave")
    @ResponseBody
    public Map<String, Object> testErrorSave(){
        Map<String, Object> ret = new HashMap<String, Object>(4);
        multiDataSourceTestService.errorModifyDataAcrossDs();
        ret.put("msg","保存成功");
        return ret;
    }

    @RequestMapping("/addDict")
    @ResponseBody
    public Map<String, Object> addDict(){
        Map<String, Object> ret = new HashMap<String, Object>(4);
        multiDataSourceTestService.addDict();
        ret.put("msg","新增字典成功");
        return ret;
    }

    @RequestMapping("/addByJdbc")
    @ResponseBody
    public Map<String, Object> addByJdbc(){
        Map<String, Object> ret = new HashMap<String, Object>(4);
        multiDataSourceTestService.addByTemplate();
        ret.put("msg","新增字典成功");
        return ret;
    }
}
