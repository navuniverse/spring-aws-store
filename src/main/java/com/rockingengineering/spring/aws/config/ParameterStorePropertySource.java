package com.rockingengineering.spring.aws.config;

import org.springframework.core.env.PropertySource;

public class ParameterStorePropertySource extends PropertySource<ParameterStoreSource> {

	public static final String PARAMETER_STORE_HIERARCHY_SPLIT_CHARACTER = "/";

	public ParameterStorePropertySource(String name, ParameterStoreSource source) {
		super(name, source);
	}

	@Override
	public Object getProperty(String name) {

		if (name.startsWith(PARAMETER_STORE_HIERARCHY_SPLIT_CHARACTER)) {
			return source.getProperty(name);
		}
		
		return null;
	}
}