package com.programmers.configuration.jwt.portal;

import com.programmers.configuration.jwt.JwtRequestFilterBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class for RequestFilter of Users.
 */
@Component
public class PortalJwtRequestFilter extends JwtRequestFilterBase {

    @Autowired
    public final void setJwtUserDetailsServiceBase(PortalJwtUserDetailsService userDetailsService) {
        jwtUserDetailsService = userDetailsService;
    }

    @Autowired
    public final void setJwtTokenUtilBase(PortalJwtTokenUtil tokenUtil) {
        jwtTokenUtil = tokenUtil;
    }
}
