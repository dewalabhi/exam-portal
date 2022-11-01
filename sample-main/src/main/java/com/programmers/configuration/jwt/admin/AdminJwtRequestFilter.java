package com.programmers.configuration.jwt.admin;

import com.programmers.configuration.jwt.JwtRequestFilterBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminJwtRequestFilter extends JwtRequestFilterBase {

    @Autowired
    public final void setJwtUserDetailsServiceBase(AdminJwtUserDetailsService userDetailsService) {
        jwtUserDetailsService = userDetailsService;
    }

    @Autowired
    public final void setJwtTokenUtilBase(AdminJwtTokenUtil tokenUtil) {
        jwtTokenUtil = tokenUtil;
    }
}
