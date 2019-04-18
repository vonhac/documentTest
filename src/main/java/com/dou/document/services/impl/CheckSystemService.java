/*
 * @Author: Nhac.Vo
 * @Email: nhac.vo@dounets.com
 * @Date: 2019-02-19 09:00:00
 * @Last Modified by:
 * @Last Modified time:
 */

package com.dou.document.services.impl;

import com.dou.adm.shared.DepartmentConstant;
import com.dou.document.mappers.CheckSystemMapper;
import com.dou.document.models.ResultDrsModel;
import com.dou.document.services.ICheckSystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CheckSystemService implements ICheckSystemService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckSystemService.class);

    @Autowired
    private CheckSystemMapper mapper;

    /* condition of result return after it was check system*/
    private static List<Integer> VALID_DOCUMENT_RESULTS = Arrays.asList(new Integer[]{0, 34, 35});
    private static List<Integer> INVALID_DOCUMENT_RESULTS = Arrays.asList(new Integer[]{2, 4, 5, 31, 32});

    /* this casd: document of call center*/
    private static List<Integer> VALID_DOCUMENT_RESULTS_CALL_CENTER = Arrays.asList(new Integer[]{0, 34, 35, 4});
    private static List<Integer> INVALID_DOCUMENT_RESULTS_CALL_CENTER = Arrays.asList(new Integer[]{2, 5, 31, 32});


    /* ResultDrsModel will be use in this case*/
    @Override
    public boolean checkSystemF1(ResultDrsModel drsModel) {
        if (drsModel.getIdOrPhone() != null && drsModel.getChannelId() != DepartmentConstant.DEPR_CALLCENTER) {
            getResultFromExternalSystem(drsModel);
            if (VALID_DOCUMENT_RESULTS.contains(drsModel.getSum())) {
                return true;
            } else if (INVALID_DOCUMENT_RESULTS.contains(drsModel.getSum())) {
                return false;
            }  else  return false;
        } else if (drsModel.getIdOrPhone() != null && drsModel.getChannelId() == DepartmentConstant.DEPR_CALLCENTER) {
            getResultFromExternalSystem(drsModel);
            if (VALID_DOCUMENT_RESULTS_CALL_CENTER.contains(drsModel.getSum())) {
                return true;
            } else if (INVALID_DOCUMENT_RESULTS_CALL_CENTER.contains(drsModel.getSum())) {
                return false;
            }else  return false;

        }else return false;
    }

    @Override
    public void getResultFromExternalSystem(ResultDrsModel customerStatus) {
        mapper.getResultFromExternalSystem(customerStatus);
    }
}