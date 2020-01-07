package com.baizhi.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class ShiRoFilter {
    @Autowired
    private MyShiRo myShiRo;
    @Bean
    //配置过滤器交给工厂
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();
        //设置过滤器如何拦截
        HashMap<String, String > hashMap = new HashMap<>();
        //设置登录是匿名资源
        hashMap.put("/admin/login","anon");
        //分别放行
        hashMap.put("/jsp/assets/**","anon");
        hashMap.put("/code/getCode","anon");
        hashMap.put("/boot/**","anon");
        //设置所有资源都是验证资源
        hashMap.put("/**","authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(hashMap);
        //是个默认重定向的路径
        shiroFilterFactoryBean.setLoginUrl("/jsp/login.jsp");
        //要设置过滤器的安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        return shiroFilterFactoryBean;
    }
    @Bean
    //配置安全管理器交给工厂
    public DefaultWebSecurityManager defaultWebSecurityManager(HashedCredentialsMatcher hashedCredentialsMatcher,MemoryConstrainedCacheManager memoryConstrainedCacheManager){
        DefaultWebSecurityManager defaultWebSecurityManager=new DefaultWebSecurityManager();
        //为自定义的域设置自定义的匹配器
        myShiRo.setCredentialsMatcher(hashedCredentialsMatcher);
        //需要将reaml设置给安全管理器
        defaultWebSecurityManager.setRealm(myShiRo);
        //设置缓存
        defaultWebSecurityManager.setCacheManager(memoryConstrainedCacheManager);
        return defaultWebSecurityManager;
    }
//    @Bean
//    public MyShiRo myShiRo(){
//        MyShiRo myShiRo=new MyShiRo();
//        return myShiRo;
//    }
    @Bean
    public MemoryConstrainedCacheManager memoryConstrainedCacheManager(){
        MemoryConstrainedCacheManager memoryConstrainedCacheManager=new MemoryConstrainedCacheManager();
        return memoryConstrainedCacheManager;
    }
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        //自定义凭证匹配器
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        hashedCredentialsMatcher.setHashIterations(1024);
        return hashedCredentialsMatcher;
    }
}
