package com.dou.document.mappers;

import com.dou.document.models.BranchSip;
import com.dou.document.models.StaffInforBranchSip;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StaffInforBranchSipMapper {

    int saveAdd(StaffInforBranchSip StaffInforBranchSip);

    int updateData(StaffInforBranchSip StaffInforBranchSip);

    List<StaffInforBranchSip> getDataStaffBranchSipByFilterCondition(@Param("staffCd") String staffCd);

    int deletebranchsip(List<String> lstBranch);

    List <BranchSip> getBranchSip();

    List<String> checkDuplicatedBranSip(@Param("branchSip") String branchSip);

    boolean checkAccountCSR(String userAccount);
}
