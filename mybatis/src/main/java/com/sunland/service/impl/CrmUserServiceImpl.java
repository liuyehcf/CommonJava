package com.sunland.service.impl;

import com.sunland.dao.CrmUserDAO;
import com.sunland.entity.CrmUser;
import com.sunland.service.CrmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by HCF on 2017/3/31.
 */
@Service("crmUserServiceImpl")
public class CrmUserServiceImpl implements CrmUserService{
    @Autowired
    private CrmUserDAO crmUserDAO;

    public CrmUser selectCrmUserById(Long id) {
        return crmUserDAO.selectCrmUserById(id);
    }

    public int insertCrmUser(CrmUser crmUser) {
        return crmUserDAO.insertCrmUser(crmUser);
    }

    public int updateCrmUser(CrmUser crmUser){
        return crmUserDAO.updateCrmUser(crmUser);
    }
}
