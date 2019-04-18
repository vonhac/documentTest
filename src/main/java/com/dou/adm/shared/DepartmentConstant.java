package com.dou.adm.shared;

public abstract class DepartmentConstant {
    public static final String DEPR_ACCOUNTANT                   = "DM01";

    public static final String DEPR_AUDIT                        = "DM02";

    public static final String DEPR_BUSSINESS                    = "DM03";

    public static final String DEPR_DIRECTSALES                  = "DM04";

    public static final String DEPR_SALESOPERATION               = "DM05";

    public static final String DEPR_UNDERWRITTING                = "DM06";

    public static final String DEPR_TELESALE                     = "DM07";

    public static final String DEPR_ANTI                         = "DM08";

    public static final String DEPR_RISK                         = "DM09";

    public static final String DEPR_BRANCHNETWORK                = "DM10";

    public static final String DEPR_CALLCENTER                   = "DM11";

    public static boolean isCSRStaff(String department) {
        return DepartmentConstant.DEPR_BRANCHNETWORK.equalsIgnoreCase(department)
                || DepartmentConstant.DEPR_SALESOPERATION.equalsIgnoreCase(department);
    }
}
