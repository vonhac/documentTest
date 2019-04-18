package com.dou.document.mappers;

import com.dou.document.models.NotifyModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationMapper {

    List<NotifyModel>   fetchNotify                     (@Param("user") String user, @Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    List<NotifyModel>   retrieveUnread                  (@Param("user") String user);

    int                 changeReadState                 (@Param("user") String user, @Param("id") int id);

    int                 changeAllMessageState           (@Param("user") String user);
}
