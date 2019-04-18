
/*
 * @Author: Nhac.Vo
 * @Email: nhac.vo@dounets.com
 * @Date: 2019-02-19 09:00:00
 * @Last Modified by:
 * @Last Modified time:
 */
package com.dou.document.services;

import com.dou.document.models.ResultDrsModel;

public interface ICheckSystemService {
    boolean checkSystemF1(ResultDrsModel drsModel);
    void getResultFromExternalSystem(ResultDrsModel customerStatus);
}