package com.rockingengineering.spring.aws.config;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.ParameterNotFoundException;
import com.rockingengineering.spring.aws.exception.ParameterStoreParameterNotFoundRuntimeException;
import com.rockingengineering.spring.aws.exception.ParameterStoreRuntimeException;

public class ParameterStoreSource {

	private AWSSimpleSystemsManagement ssmClient;
	private boolean haltBoot;

	public ParameterStoreSource(AWSSimpleSystemsManagement ssmClient, boolean haltBoot) {
		this.ssmClient = ssmClient;
		this.haltBoot = haltBoot;
	}

	public Object getProperty(String propertyName) {
		try {
			return ssmClient.getParameter(new GetParameterRequest().withName(propertyName).withWithDecryption(true))
					.getParameter()
					.getValue();
		} catch (ParameterNotFoundException e) {
			if (haltBoot) {
				throw new ParameterStoreParameterNotFoundRuntimeException(propertyName, e);
			}
		} catch (Exception e) {
			throw new ParameterStoreRuntimeException(propertyName, e);
		}
		return null;
	}
}
