package com.dou.adm.mappers;

import com.dou.adm.models.DataFiltering;
import com.dou.adm.security.JwtUser;
import com.dou.document.models.MasterModel;
import com.dou.document.shared.HashMapCoverter;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import static com.dou.adm.security.JwtAuthFilter.JWT_USER;

@Intercepts({
        @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})

public class MetaDataInterceptor implements Interceptor {
    private static final Logger LOGGER              = LoggerFactory.getLogger(MetaDataInterceptor.class);
    private static final int    STATEMENT_INDEX     = 0;
    private static final int    MODEL_INDEX         = 1;
    private static final String CMD_INSERT          = "INSERT";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] queryArgs = invocation.getArgs();
        DataFiltering filtering = getAllInfoUser();
        if (queryArgs[MODEL_INDEX] instanceof Map) {
            Map model = (Map) queryArgs[MODEL_INDEX];
            try {
                model.put("filtering", HashMapCoverter.convert(DataFiltering.class, filtering));
            } catch (Exception e) {
                LOGGER.error("Cannot auto update meta data for mapper statement.", e);
            }
        } else if (queryArgs[MODEL_INDEX] instanceof MasterModel) {
            MasterModel model = (MasterModel) queryArgs[MODEL_INDEX];
            MappedStatement statement = (MappedStatement) queryArgs[STATEMENT_INDEX];
            try {
                BoundSql boundSql = statement.getBoundSql(model);
                String sql = boundSql.getSql().trim();
                String username = getCurrentUser();
                if (sql.startsWith(CMD_INSERT)) {
                    model.setCreatedBy(username);
                    model.setCreatedDate(new Date());
                }
                model.setModifiedBy(username);
                model.setModifiedDate(new Date());
                    model.setFiltering(filtering);
            } catch (Exception e) {
                LOGGER.error("Cannot auto update meta data for mapper statement.", e);
            }
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {}

    public static String getCurrentUser(){
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes instanceof ServletRequestAttributes) {
                HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

                JwtUser user = (JwtUser) request.getAttribute(JWT_USER);
                return user.getUsername();
            }
        } catch (Exception e) {
            LOGGER.error("Not called in the context of an HTTP request", e);
        }
        return null;
    }

    public static DataFiltering getAllInfoUser(){
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes instanceof ServletRequestAttributes) {
                HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                JwtUser user = (JwtUser) request.getAttribute(JWT_USER);

                if(user != null) {
                    return user.getFiltering();
                }
            }
        } catch (Exception e) {
            LOGGER.error("Not called in the context of an HTTP request", e);
        }
        return new DataFiltering();
    }
}
