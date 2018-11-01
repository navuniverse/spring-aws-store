/**
 * 
 */
package com.rockingengineering.spring.aws.controller;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import com.rockingengineering.spring.aws.config.ParameterStorePropertySource;

/**
 * @author naveen
 *
 * @date 30-Oct-2018
 */
@RestController
public class StorePropertyController {

	@Autowired
	private ConfigurableEnvironment env;

	@Autowired
	private AWSSimpleSystemsManagement awsSimpleSystemsManagement;

	@Value("${hello}")
	private String storeValue;

	@Value("${securehello}")
	private String secureStoreValue;
	
	@GetMapping("store/value")
	public String getStoreValue() {
		return storeValue;
	}

	@GetMapping("store/value/secure")
	public String getSecureStoreValue() {
		return secureStoreValue;
	}
	
	@GetMapping("store/parameter/default")
	public Parameter getParameterFromSSMByName() {

		GetParameterRequest parameterRequest = new GetParameterRequest();
		parameterRequest.withName("/config/awsstore/hello").setWithDecryption(Boolean.valueOf(true));
		GetParameterResult parameterResult = awsSimpleSystemsManagement.getParameter(parameterRequest);

		return parameterResult.getParameter();
	}

	@GetMapping("store/env")
	public String getEnvironmentProperty() {

		String hello = env.getProperty("hello");
		String helloStore = env.getProperty("/config/awsstore/hello");

		return "hello: " + hello + ", helloStore: " + helloStore;

	}

	@GetMapping("store/all")
	public Properties getAllProperties() {
		MutablePropertySources propertySources = env.getPropertySources();
		Properties props = new Properties();

		for (PropertySource<?> nestedSource : propertySources) {

			System.out.println("Property Source Name: " + nestedSource.getName() + " : ");

			if (nestedSource instanceof EnumerablePropertySource) {

				@SuppressWarnings("rawtypes")
				EnumerablePropertySource eps = (EnumerablePropertySource) nestedSource;

				for (String propName : eps.getPropertyNames()) {
					System.out.println("\t" + propName + " = " + eps.getProperty(propName));
					props.setProperty(propName, env.getProperty(propName));
				}
			} else if (nestedSource instanceof MapPropertySource) {

				MapPropertySource eps = (MapPropertySource) nestedSource;

				for (String propName : eps.getPropertyNames()) {
					System.out.println("\t" + propName + " = " + eps.getProperty(propName));
					props.setProperty(propName, env.getProperty(propName));
				}
			} else if (nestedSource instanceof ParameterStorePropertySource) {

				ParameterStorePropertySource psps = (ParameterStorePropertySource) nestedSource;

				System.out.println(psps.getSource());

			}
		}

		return props;
	}

}