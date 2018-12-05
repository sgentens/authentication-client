package com.example.client.auth;

import com.foreach.across.core.AcrossContext;
import com.foreach.across.modules.adminweb.AdminWeb;
import com.foreach.across.modules.adminweb.AdminWebModule;
import com.foreach.across.modules.adminweb.AdminWebModuleSettings;
import com.foreach.across.modules.adminweb.events.AdminWebUrlRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author ${AUTHOR_NAME}
 * @since ${MODULE_VERSION}
 */
@Order(0)
@EnableOAuth2Sso
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
	private final ApplicationEventPublisher publisher;
	private final AdminWeb adminWeb;
	private final AcrossContext acrossContext;

	@Override
	protected void configure( HttpSecurity http ) throws Exception {
		AdminWebModuleSettings adminWebModuleSettings = getAdminWebModuleSettings();
		http.antMatcher( adminWeb.path( "/**" ) )
		    .authorizeRequests()
		    .and().authorizeRequests().anyRequest().authenticated()
		    .and().csrf()
		    .csrfTokenRepository( CookieCsrfTokenRepository.withHttpOnlyFalse() )
		    .and().logout().logoutUrl( adminWeb.path( "/logout" ) )
		    .logoutSuccessUrl( adminWeb.path( "/" ) )
		    .permitAll()
		    .logoutRequestMatcher( new AntPathRequestMatcher( adminWeb.path( "/logout" ) ) )
		    .and();

		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry urlRegistry =
				http.authorizeRequests();

		publisher.publishEvent( new AdminWebUrlRegistry( adminWeb, urlRegistry ) );

//         Only users with any of the configured admin permissions can login
		String[] authorities = {};
		// if we have AdminWebModuleSettings, use those instead.
		if ( adminWebModuleSettings != null ) {
			authorities = adminWebModuleSettings.getAccessPermissions();
		}

		urlRegistry.anyRequest().hasAnyAuthority( authorities );
	}

	private AdminWebModuleSettings getAdminWebModuleSettings() {
		return acrossContext.getModule( AdminWebModule.NAME )
		                    .getAcrossApplicationContextHolder()
		                    .getBeanFactory()
		                    .getBean( AdminWebModuleSettings.class );
	}

}
