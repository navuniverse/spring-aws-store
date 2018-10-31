/**
 * 
 */
package com.rockingengineering.spring.aws.exception;

/**
 * @author naveen
 *
 * @date 30-Oct-2018
 */
public class ParameterStoreRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ParameterStoreRuntimeException(String propertyName, Exception e) {
		super(String.format("Accessing Parameter Store for parameter '%s' failed.", propertyName), e);
	}
}