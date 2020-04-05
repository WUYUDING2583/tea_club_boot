package com.yuyi.tea.service;

import com.yuyi.tea.bean.AuthorityEnd;
import com.yuyi.tea.bean.Clerk;
import com.yuyi.tea.bean.PositionAutorityEndDetail;
import com.yuyi.tea.mapper.AuthorityMapper;
import com.yuyi.tea.mapper.ClerkMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 校验权限service
 */
@Service
@Slf4j
public class AuthorityService {

    @Autowired
    private AuthorityMapper authorityMapper;

    @Autowired
    private ClerkMapper clerkMapper;

    /**
     * 根据职位id校验后端权限
     * @param uid
     * @param url
     * @param method http方法
     * @return
     */
    public boolean checkAuthorityEnd(int uid,String url,String method){
        AuthorityEnd authorityEnd = authorityMapper.getAuthorityEndByUrlMethod(url,method);
        if(authorityEnd==null){
            return false;
        }
        Clerk clerk = clerkMapper.getClerk(uid);
        PositionAutorityEndDetail positionAutorityEndDetail = authorityMapper.getPositionAutorityEndDetail(clerk.getPosition().getUid(), authorityEnd.getUid());
        if(positionAutorityEndDetail==null){
            return false;
        }
        return true;
    }
}
