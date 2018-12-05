package com.example.client.application.web.admin;

import com.foreach.across.modules.adminweb.annotations.AdminWebController;
import com.foreach.across.modules.adminweb.menu.AdminMenuEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;

@AdminWebController
public class DemoAdminController
{
	@EventListener
	public void registerMenu( AdminMenuEvent adminMenu ) {
		adminMenu.builder()
		         .group( "/custom", "Custom controllers" ).and()
		         .item( "/custom/demo", "Demo controller", "/demo" );
	}

	@GetMapping("/demo")
	public String demoAdminPage() {
		return "th/client/admin/demo";
	}
}
