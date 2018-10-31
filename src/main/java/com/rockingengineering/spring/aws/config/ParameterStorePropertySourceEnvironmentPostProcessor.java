package com.rockingengineering.spring.aws.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ObjectUtils;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;

public class ParameterStorePropertySourceEnvironmentPostProcessor implements EnvironmentPostProcessor {

	static final String PARAMETER_STORE_ACCEPTED_PROFILE = "awsParameterStorePropertySourceEnabled";

	static final String PARAMETER_STORE_ACCEPTED_PROFILES_CONFIGURATION_PROPERTY = "awsParameterStorePropertySource.enabledProfiles";
	static final String PARAMETER_STORE_ENABLED_CONFIGURATION_PROPERTY = "awsParameterStorePropertySource.enabled";
	static final String PARAMETER_STORE_HALT_BOOT_CONFIGURATION_PROPERTY = "awsParameterStorePropertySource.haltBoot";

	private static final String PARAMETER_STORE_PROPERTY_SOURCE_NAME = "AWSParameterStorePropertySource";

	private boolean initialized;

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

		if (!initialized && isParameterStorePropertySourceEnabled(environment)) {

			System.out.println("Initializing Environment from AWS Parameter Store");

			AWSCredentialsProvider credentials =
					new AWSStaticCredentialsProvider(
							new BasicAWSCredentials(
									environment.getProperty("cloud.aws.credentials.accessKey"),
									environment.getProperty("cloud.aws.credentials.secretKey")));

			System.out.println("Initialized AWS Credentials Provider");

			environment.getPropertySources()
					.addFirst(new ParameterStorePropertySource(PARAMETER_STORE_PROPERTY_SOURCE_NAME,
							new ParameterStoreSource(
									AWSSimpleSystemsManagementClientBuilder.standard().withCredentials(credentials).withRegion(environment.getProperty("cloud.aws.region")).build(),
									environment.getProperty(PARAMETER_STORE_HALT_BOOT_CONFIGURATION_PROPERTY, Boolean.class, Boolean.FALSE))));

			System.out.println("Created Environment from AWS Parameter Store: " + environment.getPropertySources());

			initialized = true;
		}
	}

	private boolean isParameterStorePropertySourceEnabled(ConfigurableEnvironment environment) {

		String[] userDefinedEnabledProfiles =
				environment.getProperty(PARAMETER_STORE_ACCEPTED_PROFILES_CONFIGURATION_PROPERTY, String[].class);

		return environment.getProperty(PARAMETER_STORE_ENABLED_CONFIGURATION_PROPERTY, Boolean.class, Boolean.FALSE)
				|| environment.acceptsProfiles(PARAMETER_STORE_ACCEPTED_PROFILE)
				|| (!ObjectUtils.isEmpty(userDefinedEnabledProfiles) && environment.acceptsProfiles(userDefinedEnabledProfiles));

	}
}