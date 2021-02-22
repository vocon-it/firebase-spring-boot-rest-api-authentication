package io.thepro.apiservice.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("public")
public class PublicController {

	@Value("${developer-cloud-desktop.url}")
	String url;

	@Value("${developer-cloud-desktop.port}")
	String port;

	@Value("${developer-cloud-desktop.password}")
	String password;

	@GetMapping("data")
	public String getPublicData() {

		return "You have accessed public data from spring boot";
	}

	@GetMapping("developersCloudDesktop")
	public String getDevelopersCloudDesktop() {

		return "{"
				+ "\"url\":\"" + url + "\"" + ","
				+ "\"port\":\"" + port + "\"" + ","
				+ "\"password\":\"" + password + "\""
				+ "}";
	}

	@GetMapping("listHeaders")
	public ResponseEntity<String> listAllHeaders(
			@RequestHeader Map<String, String> headers) {
		headers.forEach((key, value) -> {
			System.out.printf("Header '%s' = %s", key, value);
		});

		return new ResponseEntity<String>(
				String.format("Listed %d headers: %s", headers.size(), headers), HttpStatus.OK);
	}

}
