/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs2_2;

import java.util.List;

import org.openmrs.OrderSet;
import org.openmrs.OrderSetAttribute;
import org.openmrs.OrderSetAttributeType;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.annotation.SubResource;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs1_9.BaseAttributeCrudResource1_9;

/**
 * {@link Resource} for OrderSetAttributes, supporting standard CRUD operations
 */
@SubResource(parent = OrderSetResource2_2.class, path = "attribute", supportedClass = OrderSetAttribute.class, supportedOpenmrsVersions = {"2.2.*" })
public class OrderSetAttributeResource2_2 extends BaseAttributeCrudResource1_9<OrderSetAttribute, OrderSet, OrderSetResource2_2> {
	
	/**
	 * Sets attributeType on the given OrderSetAttribute.
	 * 
	 * @param instance
	 * @param attr
	 */
	@PropertySetter("attributeType")
	public static void setAttributeType(OrderSetAttribute instance, OrderSetAttributeType attr) {
		instance.setAttributeType(attr);
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingSubResource#getParent(java.lang.Object)
	 */
	@Override
	public OrderSet getParent(OrderSetAttribute instance) {
		return instance.getOrderSet();
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResource#newDelegate()
	 */
	@Override
	public OrderSetAttribute newDelegate() {
		return new OrderSetAttribute();
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingSubResource#setParent(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public void setParent(OrderSetAttribute instance, OrderSet orderSet) {
		instance.setOrderSet(orderSet);
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResource#getByUniqueId(java.lang.String)
	 */
	@Override
	public OrderSetAttribute getByUniqueId(String uniqueId) {
		return Context.getOrderSetService().getOrderSetAttributeByUuid(uniqueId);
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingSubResource#doGetAll(java.lang.Object,
	 *      org.openmrs.module.webservices.rest.web.RequestContext)
	 */
	@Override
	public NeedsPaging<OrderSetAttribute> doGetAll(OrderSet parent, RequestContext context) throws ResponseException {
		return new NeedsPaging<OrderSetAttribute>((List<OrderSetAttribute>) parent.getActiveAttributes(), context);
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceHandler#save(java.lang.Object)
	 */
	@Override
	public OrderSetAttribute save(OrderSetAttribute delegate) {
		// make sure it has not already been added to the orderSet
		boolean needToAdd = true;
		for (OrderSetAttribute pa : delegate.getOrderSet().getActiveAttributes()) {
			if (pa.equals(delegate)) {
				needToAdd = false;
				break;
			}
		}
		if (needToAdd) {
			delegate.getOrderSet().addAttribute(delegate);
		}
		Context.getOrderSetService().saveOrderSet(delegate.getOrderSet());
		return delegate;
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource#delete(java.lang.Object,
	 *      java.lang.String, org.openmrs.module.webservices.rest.web.RequestContext)
	 */
	@Override
	protected void delete(OrderSetAttribute delegate, String reason, RequestContext context) throws ResponseException {
		delegate.setVoided(true);
		delegate.setVoidReason(reason);
		Context.getOrderSetService().saveOrderSet(delegate.getOrderSet());
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource#purge(java.lang.Object,
	 *      org.openmrs.module.webservices.rest.web.RequestContext)
	 */
	@Override
	public void purge(OrderSetAttribute delegate, RequestContext context) throws ResponseException {
		throw new UnsupportedOperationException("Cannot purge OrderSetAttribute");
	}
}
