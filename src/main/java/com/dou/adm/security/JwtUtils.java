package com.dou.adm.security;

import com.dou.adm.models.DataFiltering;
import com.dou.adm.models.UserProfiles;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JwtUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    public static final String PROFILES             = "X-PROFILES";
    public static final String SELECTOR             = "X-SELECTOR";
    public static final String TARGET_TABLE         = "X-TARGET-TABLE";

    public static JwtUser retrieveUserFromClaims(Claims claims, ObjectMapper objectMapper) {
        JwtUser jwtUser = new JwtUser(
                Long.valueOf(claims.getId()),
                claims.getSubject(),
                getStringValue(claims, TARGET_TABLE)
        );
        try {
            jwtUser.setProfiles(objectMapper.readValue(getStringValue(claims, PROFILES), UserProfiles.class));
            jwtUser.setFiltering(objectMapper.readValue(getStringValue(claims, SELECTOR), DataFiltering.class));
        } catch (IOException e) {
            LOGGER.error(String.format("Error occurred while trying to retrieve UserProfile and Selector of user [%s] from JwtClaims", jwtUser.getUsername()), e);
        }
        return jwtUser;
    }

    private static String getStringValue(Claims claims, String keyName) {
        Object value = claims.get(keyName);
        return value == null ? null : String.valueOf(value);
    }

    private static Long getLongValue(Claims claims, String keyName) {
        Object value = claims.get(keyName);
        if (value == null) return 0L;
        try {
            return Long.valueOf(String.valueOf(value));
        } catch (Exception e) {
            LOGGER.warn(String.format("Error occurred while trying to retrieve value of %s JwtClaims", keyName),e);
            return 0L;
        }
    }

    private static boolean getBooleanValue(Claims claims, String keyName) {
        Object value = claims.get(keyName);
        if (value == null) return false;
        try {
            return Boolean.valueOf(String.valueOf(value));
        } catch (Exception e) {
            LOGGER.warn(String.format("Error occurred while trying to retrieve value of %s from JwtClaims", keyName),e);
            return false;
        }
    }
}
