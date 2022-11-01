package com.programmers.common;

import com.programmers.configuration.jwt.portal.PortalJwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class AppUtils {

    @Autowired
    private static PortalJwtTokenUtil jwtTokenUtil;

    private AppUtils() {
        throw new IllegalStateException("Exception occured in AppUtility class");
    }

    public static Claims fetchClaimsFromToken(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader("Authorization");

        /*
         * JWT Token is in the form "programmers.io token". Remove
         * programmers.io word and get only the Token
         */
        final String jwtToken = requestTokenHeader.substring(15);
        jwtTokenUtil = new PortalJwtTokenUtil();

        return jwtTokenUtil.getAllClaimsFromToken(jwtToken);

    }

}
