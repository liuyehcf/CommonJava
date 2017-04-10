package com.sunland.service;

import com.sunland.entity.CrmUser;

/**
 * Created by HCF on 2017/3/31.
 */
public interface CrmUserService {

    CrmUser selectCrmUserById(Long id);

    int insertCrmUser(CrmUser crmUser);

    int updateCrmUser(CrmUser crmUser);
}

