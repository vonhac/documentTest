package com.dou.document.mappers;

import com.dou.document.models.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface DmtDocumentMapper {

    List<TraInfoModel> searchDmtDocumentModel (DmtDocumentSearchModel searchModel);
    List<TraInfoModel> searchDocumentByIdNo(@Param("idNo") String idNo);
    List<TraInfoModel> searchDocumentByDMT(@Param("documentCd") int documentCd);
    List<BranchModel> getBranchModel(@Param("branchId") String branchId);
    List<SaleAccountModel> getSaleAccountModel(@Param("accountId") String accountId, @Param("department") String department);
    SaleAccountModel checkSaleCode(@Param("salesCd") String salesCd,@Param("department") String department);
    SaleAccountModel getSaleAccountModelByDM(@Param("department") String department);

    public List<DocumentStatusModel> getStatusModel();
    public List<DmtProductModel> getProductModel(@Param("productCd") String productCd);
    public Boolean addDmtDocumentModel(TraInfoModel model);
    public Boolean insertDmtDocumentStatus(DocumentDetailStatusModel model);
    public Boolean updateDmtDocument(TraInfoModel model);
    public Boolean updateDocumentByIdNo(TraInfoModel model);
    public Boolean updateDmtDocumentStatus(TraInfoModel model);
    int deleteDmtDocument(int id);
    void getResultFromExternalSystem(ResultDrsModel customerStatus);

    List<TraInfoModel> check(String idOrPhone);
    SaleAccountModel retrieveNameSaleById(@Param("table") String targetProfileTable,@Param("saleCd") String saleCd);
}
