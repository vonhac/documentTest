/*
 * @Author: Nhac.Vo
 * @Email: nhac.vo@dounets.com
 * @Date: 2019-02-19 09:00:00
 * @Last Modified by:
 * @Last Modified time:
 */
package com.dou.document.controllers;


import com.dou.adm.shared.ResponseObject;
import com.dou.document.services.IParameterService;
import com.dou.document.shared.DMTConfigurationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parameters")
public class ParameterController {

    @Autowired
    private IParameterService service;

    @GetMapping
    public ResponseObject getAll() {
        return service.getAllParams();
    }

    @GetMapping("/deadline")
    public ResponseObject getParamDeadline(){
        return service.getParam(DMTConfigurationCode.PARAM_DEADLINE);
    }

    @PutMapping("/deadline")
    public ResponseObject setParamDeadline(@RequestBody String value){
        return service.setParam(DMTConfigurationCode.PARAM_DEADLINE, value);
    }

    @GetMapping("/periodic-fetch")
    public ResponseObject getParamPeriodicFetch(){
        return service.getParam(DMTConfigurationCode.PARAM_PERIODIC_FETCH_NOTIFY);
    }

    @PutMapping("/periodic-fetch")
    public ResponseObject setParamPeriodicFetch(@RequestBody String value){
        return service.setParam(DMTConfigurationCode.PARAM_PERIODIC_FETCH_NOTIFY, value);
    }
}