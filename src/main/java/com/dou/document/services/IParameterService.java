/*
 * @Author: Nhac.Vo
 * @Email: nhac.vo@dounets.com
 * @Date: 2019-02-19 09:00:00
 * @Last Modified by:
 * @Last Modified time:
 */
package com.dou.document.services;

import com.dou.adm.shared.ResponseObject;

public interface IParameterService {

    ResponseObject getAllParams();

    ResponseObject getParam(String name);

    ResponseObject setParam(String name, String value);

}