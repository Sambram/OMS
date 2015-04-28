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
 
package com.hybris.kernel.persistence.metadata;

import com.hybris.kernel.api.*;

import com.hybris.kernel.api.ManagedObject;

  
/**
 * Generated managedobject class for type YTypeCode first defined at extension <code>kernel-metadata</code>.
 * <p/>
 * .
 */
public interface YTypeCode extends ManagedObject
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "YTypeCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>YTypeCode.groupId</code> attribute defined at extension <code>kernel-metadata</code>. */
	AttributeType<YTypeCode, String> GROUPID = new AttributeType<>("groupId");
	/** <i>Generated constant</i> - Attribute key of <code>YTypeCode.version</code> attribute defined at extension <code>kernel-metadata</code>. */
	AttributeType<YTypeCode, String> VERSION = new AttributeType<>("version");
	/** <i>Generated constant</i> - Attribute key of <code>YTypeCode.typeCodeName</code> attribute defined at extension <code>kernel-metadata</code>. */
	AttributeType<YTypeCode, String> TYPECODENAME = new AttributeType<>("typeCodeName");
	/** <i>Generated constant</i> - Attribute key of <code>YTypeCode.artifactId</code> attribute defined at extension <code>kernel-metadata</code>. */
	AttributeType<YTypeCode, String> ARTIFACTID = new AttributeType<>("artifactId");


	/**
	 * <i>Generated method</i> - Getter of the <code>YTypeCode.groupId</code> attribute defined at extension <code>kernel-metadata</code>.
	 * <p/>
	 * .
	 * 
	 * @return the groupId
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getGroupId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YTypeCode.artifactId</code> attribute defined at extension <code>kernel-metadata</code>.
	 * <p/>
	 * .
	 * 
	 * @return the artifactId
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getArtifactId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YTypeCode.version</code> attribute defined at extension <code>kernel-metadata</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getVersion();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YTypeCode.typeCodeName</code> attribute defined at extension <code>kernel-metadata</code>.
	 * <p/>
	 * .
	 * 
	 * @return the typeCodeName
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getTypeCodeName();
	

	/**
	 * <i>Generated method</i> - Setter of <code>YTypeCode.groupId</code> attribute defined at extension <code>kernel-metadata</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the groupId
	 */
	void setGroupId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>YTypeCode.artifactId</code> attribute defined at extension <code>kernel-metadata</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the artifactId
	 */
	void setArtifactId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>YTypeCode.version</code> attribute defined at extension <code>kernel-metadata</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>YTypeCode.typeCodeName</code> attribute defined at extension <code>kernel-metadata</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the typeCodeName
	 */
	void setTypeCodeName(final java.lang.String value);
	
}
