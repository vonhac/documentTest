package com.dou.document.services.impl;

import com.dou.adm.shared.ResponseObject;
import com.dou.document.mappers.NotificationMapper;
import com.dou.document.services.INotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements INotificationService {
    public static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);
    public static final int NOTIFY_FETCH_SIZE    = 10;

    @Autowired
    private NotificationMapper mapper;

    @Override
    public ResponseObject fetchNotify(String user, int page) {
        int startIndex = (page - 1) * NOTIFY_FETCH_SIZE;
        if (startIndex < 0) {
            startIndex = 0;
        }
        try {
            return new ResponseObject(mapper.fetchNotify(user, startIndex, NOTIFY_FETCH_SIZE));
        } catch (Exception e) {
            LOGGER.error(String.format("Error occurred while try to load the list messages of user [%s]", user), e);
        }
        return ResponseObject.failResponse("Had issue while try to load the list messages");
    }

    @Override
    public ResponseObject retrieveUnread(String user) {
        try {
            return new ResponseObject(mapper.retrieveUnread(user));
        } catch (Exception e) {
            LOGGER.error(String.format("Had issue while try to load the list unread messages of user [%s]", user), e);
        }
        return ResponseObject.failResponse("Had issue while try to load the list unread messages");
    }

    @Override
    public ResponseObject changeReadState(String user, int id) {
        try {
            if (id > 0) {
                int effectCount = mapper.changeReadState(user, id);
                if (effectCount > 0) {
                    return ResponseObject.SUCCESS_WITHOUT_DATA;
                }
                return ResponseObject.NOT_FOUND;
            }
        } catch (Exception e) {
            LOGGER.error(String.format("Error occurred while try to change state for message id [%d]", id), e);
        }
        return ResponseObject.FAILURE;
    }

    @Override
    public ResponseObject changeAllMessageState(String user) {
        try {
            mapper.changeAllMessageState(user);
            return ResponseObject.SUCCESS_WITHOUT_DATA;
        } catch (Exception e) {
            LOGGER.error(String.format("Error occurred while try to change state for all message of user [%s]", user), e);
        }
        return ResponseObject.FAILURE;
    }
}
