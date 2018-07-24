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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.openmrs.OrderSetAttributeType;
import org.openmrs.api.OrderSetService;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs1_9.BaseAttributeTypeCrudResource1_9;

/**
 * Allows standard CRUD for the {@link OrderSetAttributeType} domain object
 */
@Resource(name = RestConstants.VERSION_1 + "/ordersetattributetype", supportedClass = OrderSetAttributeType.class, supportedOpenmrsVersions = {"2.2.*" })
public class OrderSetAttributeTypeResource2_2 extends BaseAttributeTypeCrudResource1_9<OrderSetAttributeType> {
	
	public OrderSetAttributeTypeResource2_2() {
	}
	
	private OrderSetService service() {
		return Context.getOrderSetService();
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResource#getByUniqueId(java.lang.String)
	 */
	@Override
	public OrderSetAttributeType getByUniqueId(String uniqueId) {
		return service().getOrderSetAttributeTypeByUuid(uniqueId);
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource#doGetAll(org.openmrs.module.webservices.rest.web.RequestContext)
	 */
	@Override
	protected NeedsPaging<OrderSetAttributeType> doGetAll(RequestContext context) throws ResponseException {
		return new NeedsPaging<OrderSetAttributeType>(service().getAllOrderSetAttributeTypes(), context);
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResource#newDelegate()
	 */
	@Override
	public OrderSetAttributeType newDelegate() {
		return new OrderSetAttributeType();
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceHandler#save(java.lang.Object)
	 */
	@Override
	public OrderSetAttributeType save(OrderSetAttributeType delegate) {
		return service().saveOrderSetAttributeType(delegate);
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResource#purge(java.lang.Object,
	 *      org.openmrs.module.webservices.rest.web.RequestContext)
	 */
	@Override
	public void purge(OrderSetAttributeType delegate, RequestContext context) throws ResponseException {
		service().purgeOrderSetAttributeType(delegate);
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource#doSearch(org.openmrs.module.webservices.rest.web.RequestContext)
	 */
	@Override
	protected NeedsPaging<OrderSetAttributeType> doSearch(RequestContext context) {
		// TODO: Should be a OrderSetAttributeType search method in OrderSetService
		List<OrderSetAttributeType> allAttrs = service().getAllOrderSetAttributeTypes();
		List<OrderSetAttributeType> queryResult = new ArrayList<OrderSetAttributeType>();
		for (OrderSetAttributeType locAttr : allAttrs) {
			if (Pattern.compile(Pattern.quote(context.getParameter("q")), Pattern.CASE_INSENSITIVE)
			        .matcher(locAttr.getName()).find()) {
				queryResult.add(locAttr);
			}
		}
		return new NeedsPaging<OrderSetAttributeType>(queryResult, context);
	}
}
