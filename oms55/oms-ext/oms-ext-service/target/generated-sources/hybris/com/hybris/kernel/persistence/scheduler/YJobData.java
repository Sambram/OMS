/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
 
package com.hybris.kernel.persistence.scheduler;

import com.hybris.kernel.api.*;

import com.hybris.kernel.api.ManagedObject;

  
/**
 * Generated managedobject class for type YJobData first defined at extension <code>kernel-scheduler</code>.
 * <p/>
 * .
 */
public interface YJobData extends ManagedObject
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "YJobData";
	
	/** <i>Generated constant</i> - Attribute key of <code>YJobData.status</code> attribute defined at extension <code>kernel-scheduler</code>. */
	AttributeType<YJobData, String> STATUS = new AttributeType<>("status");
	/** <i>Generated constant</i> - Attribute key of <code>YJobData.jobName</code> attribute defined at extension <code>kernel-scheduler</code>. */
	AttributeType<YJobData, String> JOBNAME = new AttributeType<>("jobName");

	/** <i>Generated constant</i> - Index of <code>YJobData</code> type defined at extension <code>kernel-scheduler</code>. */
	UniqueIndexSingle<YJobData, String> UX_YJOBDATA_JOBNAME = new UniqueIndexSingle<>("UX_YJobData_jobName", YJobData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>YJobData.jobName</code> attribute defined at extension <code>kernel-scheduler</code>.
	 * <p/>
	 * .
	 * 
	 * @return the jobName
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getJobName();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YJobData.status</code> attribute defined at extension <code>kernel-scheduler</code>.
	 * <p/>
	 * .
	 * 
	 * @return the status
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getStatus();
	

	/**
	 * <i>Generated method</i> - Setter of <code>YJobData.jobName</code> attribute defined at extension <code>kernel-scheduler</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the jobName
	 */
	void setJobName(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>YJobData.status</code> attribute defined at extension <code>kernel-scheduler</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the status
	 */
	void setStatus(final java.lang.String value);
	
}
