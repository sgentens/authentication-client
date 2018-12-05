package com.example.client.application.extensions;

import com.example.client.auth.WebSecurityConfig;
import com.foreach.across.core.annotations.ModuleConfiguration;
import com.foreach.across.modules.spring.security.SpringSecurityModule;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @author ${AUTHOR_NAME}
 * @since ${MODULE_VERSION}
 */
@ModuleConfiguration(SpringSecurityModule.NAME)
@Import(OAuth2AutoConfiguration.class)
@ComponentScan(basePackageClasses = WebSecurityConfig.class)
public class SecurityConfig
{
}


