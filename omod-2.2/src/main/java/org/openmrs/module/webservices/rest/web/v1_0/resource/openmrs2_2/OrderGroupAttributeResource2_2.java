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

import org.openmrs.OrderGroup;
import org.openmrs.OrderGroupAttribute;
import org.openmrs.OrderGroupAttributeType;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.annotation.SubResource;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs1_9.BaseAttributeCrudResource1_9;

/**
 * {@link Resource} for OrderGroupAttributes, supporting standard CRUD operations
 */
@SubResource(parent = OrderGroupResource2_2.class, path = "attribute", supportedClass = OrderGroupAttribute.class, supportedOpenmrsVersions = { "2.2.*" })
public class OrderGroupAttributeResource2_2 extends BaseAttributeCrudResource1_9<OrderGroupAttribute, OrderGroup, OrderGroupResource2_2> {
	
	/**
	 * Sets attributeType on the given OrderGroupAttribute.
	 * 
	 * @param instance
	 * @param attr
	 */
	@PropertySetter("attributeType")
	public static void setAttributeType(OrderGroupAttribute instance, OrderGroupAttributeType attr) {
		instance.setAttributeType(attr);
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingSubResource#getParent(java.lang.Object)
	 */
	@Override
	public OrderGroup getParent(OrderGroupAttribute instance) {
		return instance.getOrderGroup();
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResource#newDelegate()
	 */
	@Override
	public OrderGroupAttribute newDelegate() {
		return new OrderGroupAttribute();
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingSubResource#setParent(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public void setParent(OrderGroupAttribute instance, OrderGroup orderGroup) {
		instance.setOrderGroup(orderGroup);
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResource#getByUniqueId(java.lang.String)
	 */
	@Override
	public OrderGroupAttribute getByUniqueId(String uniqueId) {
		return Context.getOrderService().getOrderGroupAttributeByUuid(uniqueId);
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingSubResource#doGetAll(java.lang.Object,
	 *      org.openmrs.module.webservices.rest.web.RequestContext)
	 */
	@Override
	public NeedsPaging<OrderGroupAttribute> doGetAll(OrderGroup parent, RequestContext context) throws ResponseException {
		return new NeedsPaging<OrderGroupAttribute>((List<OrderGroupAttribute>) parent.getActiveAttributes(), context);
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceHandler#save(java.lang.Object)
	 */
	@Override
	public OrderGroupAttribute save(OrderGroupAttribute delegate) {
		// make sure it has not already been added to the orderGroup
		boolean needToAdd = true;
		for (OrderGroupAttribute pa : delegate.getOrderGroup().getActiveAttributes()) {
			if (pa.equals(delegate)) {
				needToAdd = false;
				break;
			}
		}
		if (needToAdd) {
			delegate.getOrderGroup().addAttribute(delegate);
		}
		Context.getOrderService().saveOrderGroup(delegate.getOrderGroup());
		return delegate;
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource#delete(java.lang.Object,
	 *      java.lang.String, org.openmrs.module.webservices.rest.web.RequestContext)
	 */
	@Override
	protected void delete(OrderGroupAttribute delegate, String reason, RequestContext context) throws ResponseException {
		delegate.setVoided(true);
		delegate.setVoidReason(reason);
		Context.getOrderService().saveOrderGroup(delegate.getOrderGroup());
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource#purge(java.lang.Object,
	 *      org.openmrs.module.webservices.rest.web.RequestContext)
	 */
	@Override
	public void purge(OrderGroupAttribute delegate, RequestContext context) throws ResponseException {
		throw new UnsupportedOperationException("Cannot purge OrderGroupAttribute");
	}
}
