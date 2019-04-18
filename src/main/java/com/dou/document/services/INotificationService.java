package com.dou.document.services;

import com.dou.adm.shared.ResponseObject;

public interface INotificationService {

    ResponseObject fetchNotify              (String user, int page);

    ResponseObject retrieveUnread           (String user);

    ResponseObject changeReadState          (String user, int id);

    ResponseObject changeAllMessageState    (String user);

}
