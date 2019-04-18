package com.dou.adm.mappers;

import com.dou.adm.models.User;
import com.dou.adm.models.UserProfiles;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface UserMapper {

    User loginUser                      (@Param("accountId") String accountId);

    List getInfoPerMissionByAccount     (@Param("accountId") String accountId);

    UserProfiles retrieveUserProfile    (@Param("table") String targetTable, @Param("accountId") String accountId);

    int changePassword                  (@Param("accountId") String accountId, @Param("password") String password);

    List lsAccSubervisor                (@Param("table") String targetTable, @Param("supervisor") String supervisor);

    int managerLevel                    (@Param("staffCd") String staffCd);

    List ListBranchSip                  (@Param("staffCd") String staffCd);


}