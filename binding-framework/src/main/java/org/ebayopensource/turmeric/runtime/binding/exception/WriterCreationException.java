/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
/**
 * 
 */
package org.ebayopensource.turmeric.runtime.binding.exception;


/**
 * WriterCreationException is thrown when an abnormal condition is detected when creating 
 * a writer for outbound payload.
 * 
 * @author wdeng
 *
 */
public class WriterCreationException extends BindingException {
	/**
	 * serial version UID.
	 */
	static final long serialVersionUID = 0L;
	private String m_payloadType;
	
	
	/**
     * Constructs a <code>WriterCreationException</code> with the payload type and cause. 
	 * 
	 * @param payloadType  The type of payload that are being processed 
	 *                     when the exception occurs 
	 * @param  t - The cause (which is saved for later retrieval by the 
	 * <code>{@link java.lang.Throwable#getCause}</code> method). 
	 * (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public WriterCreationException(String payloadType, Throwable t) {
		super(t);
		m_payloadType = payloadType;
	}
	
	
	/**
	 * Gets the payload type being processed when the exception is thrown.
	 * @return  - The payload type.
	 */
	public String getPayloadType() {
		return m_payloadType;
	}
}
