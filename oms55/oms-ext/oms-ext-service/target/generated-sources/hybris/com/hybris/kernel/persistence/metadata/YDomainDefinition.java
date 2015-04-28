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
 * Generated managedobject class for type YDomainDefinition first defined at extension <code>kernel-metadata</code>.
 * <p/>
 * .
 */
public interface YDomainDefinition extends ManagedObject
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "YDomainDefinition";
	
	/** <i>Generated constant</i> - Attribute key of <code>YDomainDefinition.content</code> attribute defined at extension <code>kernel-metadata</code>. */
	AttributeType<YDomainDefinition, String> CONTENT = new AttributeType<>("content");
	/** <i>Generated constant</i> - Attribute key of <code>YDomainDefinition.name</code> attribute defined at extension <code>kernel-metadata</code>. */
	AttributeType<YDomainDefinition, String> NAME = new AttributeType<>("name");
	/** <i>Generated constant</i> - Attribute key of <code>YDomainDefinition.groupId</code> attribute defined at extension <code>kernel-metadata</code>. */
	AttributeType<YDomainDefinition, String> GROUPID = new AttributeType<>("groupId");
	/** <i>Generated constant</i> - Attribute key of <code>YDomainDefinition.version</code> attribute defined at extension <code>kernel-metadata</code>. */
	AttributeType<YDomainDefinition, String> VERSION = new AttributeType<>("version");
	/** <i>Generated constant</i> - Attribute key of <code>YDomainDefinition.artifactId</code> attribute defined at extension <code>kernel-metadata</code>. */
	AttributeType<YDomainDefinition, String> ARTIFACTID = new AttributeType<>("artifactId");
	/** <i>Generated constant</i> - Attribute key of <code>YDomainDefinition.hash</code> attribute defined at extension <code>kernel-metadata</code>. */
	AttributeType<YDomainDefinition, String> HASH = new AttributeType<>("hash");


	/**
	 * <i>Generated method</i> - Getter of the <code>YDomainDefinition.groupId</code> attribute defined at extension <code>kernel-metadata</code>.
	 * <p/>
	 * .
	 * 
	 * @return the groupId
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getGroupId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YDomainDefinition.artifactId</code> attribute defined at extension <code>kernel-metadata</code>.
	 * <p/>
	 * .
	 * 
	 * @return the artifactId
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getArtifactId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YDomainDefinition.version</code> attribute defined at extension <code>kernel-metadata</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getVersion();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YDomainDefinition.name</code> attribute defined at extension <code>kernel-metadata</code>.
	 * <p/>
	 * .
	 * 
	 * @return the name
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getName();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YDomainDefinition.content</code> attribute defined at extension <code>kernel-metadata</code>.
	 * <p/>
	 * .
	 * 
	 * @return the content
	 */
	java.lang.String getContent();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YDomainDefinition.hash</code> attribute defined at extension <code>kernel-metadata</code>.
	 * <p/>
	 * .
	 * 
	 * @return the hash
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getHash();
	

	/**
	 * <i>Generated method</i> - Setter of <code>YDomainDefinition.groupId</code> attribute defined at extension <code>kernel-metadata</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the groupId
	 */
	void setGroupId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>YDomainDefinition.artifactId</code> attribute defined at extension <code>kernel-metadata</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the artifactId
	 */
	void setArtifactId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>YDomainDefinition.version</code> attribute defined at extension <code>kernel-metadata</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>YDomainDefinition.name</code> attribute defined at extension <code>kernel-metadata</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the name
	 */
	void setName(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>YDomainDefinition.content</code> attribute defined at extension <code>kernel-metadata</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the content
	 */
	void setContent(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>YDomainDefinition.hash</code> attribute defined at extension <code>kernel-metadata</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the hash
	 */
	void setHash(final java.lang.String value);
	
}
