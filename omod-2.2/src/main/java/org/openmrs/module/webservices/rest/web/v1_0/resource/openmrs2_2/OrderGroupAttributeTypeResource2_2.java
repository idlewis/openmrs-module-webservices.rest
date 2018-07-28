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

import org.openmrs.OrderGroupAttributeType;
import org.openmrs.api.OrderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs1_9.BaseAttributeTypeCrudResource1_9;
/**
 * Allows standard CRUD for the {@link OrderGroupAttributeType} domain object
 */
@Resource(name = RestConstants.VERSION_1 + "/ordersetattributetype", supportedClass = OrderGroupAttributeType.class, supportedOpenmrsVersions = {"2.2.*" })
public class OrderGroupAttributeTypeResource2_2 extends BaseAttributeTypeCrudResource1_9<OrderGroupAttributeType> {
	
	public OrderGroupAttributeTypeResource2_2() {
	}
	
	private OrderService service() {
		return Context.getOrderService();
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResource#getByUniqueId(java.lang.String)
	 */
	@Override
	public OrderGroupAttributeType getByUniqueId(String uniqueId) {
		return service().getOrderGroupAttributeTypeByUuid(uniqueId);
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource#doGetAll(org.openmrs.module.webservices.rest.web.RequestContext)
	 */
	@Override
	protected NeedsPaging<OrderGroupAttributeType> doGetAll(RequestContext context) throws ResponseException {
		return new NeedsPaging<OrderGroupAttributeType>(service().getAllOrderGroupAttributeTypes(), context);
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResource#newDelegate()
	 */
	@Override
	public OrderGroupAttributeType newDelegate() {
		return new OrderGroupAttributeType();
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceHandler#save(java.lang.Object)
	 */
	@Override
	public OrderGroupAttributeType save(OrderGroupAttributeType delegate) {
		return service().saveOrderGroupAttributeType(delegate);
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResource#purge(java.lang.Object,
	 *      org.openmrs.module.webservices.rest.web.RequestContext)
	 */
	@Override
	public void purge(OrderGroupAttributeType delegate, RequestContext context) throws ResponseException {
		service().purgeOrderGroupAttributeType(delegate);
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource#doSearch(org.openmrs.module.webservices.rest.web.RequestContext)
	 */
	@Override
	protected NeedsPaging<OrderGroupAttributeType> doSearch(RequestContext context) {
		// TODO: Should be a OrderGroupAttributeType search method in OrderService
		List<OrderGroupAttributeType> allAttrs = service().getAllOrderGroupAttributeTypes();
		List<OrderGroupAttributeType> queryResult = new ArrayList<OrderGroupAttributeType>();
		for (OrderGroupAttributeType locAttr : allAttrs) {
			if (Pattern.compile(Pattern.quote(context.getParameter("q")), Pattern.CASE_INSENSITIVE)
			        .matcher(locAttr.getName()).find()) {
				queryResult.add(locAttr);
			}
		}
		return new NeedsPaging<OrderGroupAttributeType>(queryResult, context);
	}
}