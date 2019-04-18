package com.dou.adm.configuration;

import com.dou.adm.mappers.MetaDataInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static com.dou.adm.shared.TargetDSName.*;

/**
 * Created by Tu.Tran on 9/28/2018.
 */
@Configuration
public class DynamicDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSourceDefault() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.auth")
    public DataSource dataSourceAuth() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.orcl")
    public DataSource dataSourceOrcl() {
        return DataSourceBuilder.create().build();
    }

    //////////////////// Creating dynamic data sources
    @Bean
    public DataSource dataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        // Create a default data source
        dynamicDataSource.setDefaultTargetDataSource(dataSourceDefault());
        // Configuring multiple data sources
        Map<Object, Object> dataSourceMap = new HashMap<>(4);
        dataSourceMap.put(DS_DEFAULT, dataSourceDefault());
        dataSourceMap.put(DS_AUTH, dataSourceAuth());
        dataSourceMap.put(DS_FINNONE, dataSourceOrcl());
        // End DuyPNK Add Connection 21-11-2018
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        return dynamicDataSource;
    }

    //////////////////// Configuring SqlSessionFactory
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // Setting up a data source as a dynamic data source
        sqlSessionFactoryBean.setDataSource(dataSource());
        // mapperThe location of the.Xml file
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mappers/*/*.xml"));
        final Resource configLocation = new ClassPathResource("mybatis-config.xml");
        sqlSessionFactoryBean.setConfigLocation(configLocation);
        sqlSessionFactoryBean.setPlugins(new Interceptor[] {
                new MetaDataInterceptor()
        });
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
