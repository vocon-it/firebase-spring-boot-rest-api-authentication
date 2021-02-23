package io.thepro.apiservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.thepro.apiservice.security.SecurityService;

@RestController
@RequestMapping("protected")
public class ProtectedController {

	@Autowired
	private SecurityService securityService;

	@Value("${developers-cloud-desktop.url}")
	String url;

	@Value("${developers-cloud-desktop.port}")
	String port;

	@Value("${developers-cloud-desktop.password}")
	String password;

	@GetMapping("data")
	public String getProtectedData() {
		String name = securityService.getUser().getName();
		return name.split("\\s+")[0] + ", you have accessed protected data from spring boot";
	}

	@GetMapping("developersCloudDesktop")
	public String getDevelopersCloudDesktop() {

		return "{"
				+ "\"url\":\"" + url + "\"" + ","
				+ "\"port\":\"" + port + "\"" + ","
				+ "\"password\":\"" + password + "\""
				+ "}";
	}

}
