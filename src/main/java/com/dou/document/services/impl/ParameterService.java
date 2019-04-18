package com.dou.document.services.impl;

import com.dou.adm.shared.ResponseObject;
import com.dou.document.mappers.ParameterMapper;
import com.dou.document.services.IParameterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParameterService implements IParameterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParameterService.class);

    @Autowired
    private ParameterMapper mapper;

    @Override
    public ResponseObject getAllParams() {
        try {
            return new ResponseObject(mapper.getAllParams());
        } catch (Exception e) {
            LOGGER.error("Error occurred while try to get all document parameter", e);
        }
        return ResponseObject.FAILURE;
    }

    @Override
    public ResponseObject setParam(String name, String value) {
        try {
            return new ResponseObject(mapper.setParam(name, value));
        } catch (Exception e) {
            LOGGER.error(String.format("Error occurred while try to set value [%s] for parameter [%s]", value, name), e);
        }
        return ResponseObject.FAILURE;
    }

    @Override
    public ResponseObject getParam(String name) {
        try {
            return new ResponseObject(mapper.getParam(name));
        } catch (Exception e) {
            LOGGER.error(String.format("Error occurred while try to get value of parameter [%s]", name), e);
        }
        return ResponseObject.FAILURE;
    }

}
