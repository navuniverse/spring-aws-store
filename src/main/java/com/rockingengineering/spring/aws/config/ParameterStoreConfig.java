/**
 * 
 */
package com.rockingengineering.spring.aws.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;

/**
 * @author naveen
 *
 * @date 30-Oct-2018
 */
@Configuration
public class ParameterStoreConfig {

	@Value("${cloud.aws.credentials.accessKey}")
	private String acessKey;

	@Value("${cloud.aws.credentials.secretKey}")
	private String secretKey;

	@Bean
	public AWSCredentials credential() {
		return new BasicAWSCredentials(acessKey, secretKey);
	}

	@Bean
	public AWSSimpleSystemsManagement systemManager() {

		AWSCredentialsProvider credentials = new AWSStaticCredentialsProvider(credential());

		return AWSSimpleSystemsManagementClientBuilder.standard().withCredentials(credentials).withRegion("ap-south-1").build();
	}
}