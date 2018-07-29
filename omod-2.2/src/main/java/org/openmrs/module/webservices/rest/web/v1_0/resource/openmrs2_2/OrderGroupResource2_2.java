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

import org.openmrs.OrderGroup;
import org.openmrs.api.OrderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

/**
 * {@link Resource} for OrderGroup, supporting standard CRUD operations
 */
@Resource(name = RestConstants.VERSION_1 + "/ordergroup", supportedClass = OrderGroup.class, supportedOpenmrsVersions = {
        "2.2.*" })
public class OrderGroupResource2_2 extends DataDelegatingCrudResource<OrderGroup> {
	
	private OrderService orderService = Context.getOrderService();
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource#getRepresentationDescription(org.openmrs.module.webservices.rest.web.representation.Representation)
	 */
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
		if (representation instanceof DefaultRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addProperty("patient", Representation.REF);
			description.addProperty("orderSet", Representation.REF);
			description.addProperty("encounter", Representation.REF);
			description.addProperty("orders", Representation.REF);
			description.addProperty("voided");
			description.addSelfLink();
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			return description;
		} else if (representation instanceof FullRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addProperty("patient", Representation.REF);
			description.addProperty("orderSet", Representation.REF);
			description.addProperty("encounter", Representation.REF);
			description.addProperty("orders", Representation.DEFAULT);
			description.addProperty("voided");
			description.addSelfLink();
			return description;
		}
		return null;
	}
	
	/*
	 * TODO add the swagger documentation methods 
	 * https://wiki.openmrs.org/display/docs/Documenting+REST+Resources 
	 */
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResource#getCreatableProperties()
	 *      TODO fix this up may not need it
	 */
	/*
	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		
		description.addProperty("patient");
		
		return description;
	}
	*/
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResource#getUpdatableProperties()
	 *      TODO fix this up, may not need it /*
	 * @Override public DelegatingResourceDescription getUpdatableProperties() {
	 *           DelegatingResourceDescription description = new DelegatingResourceDescription();
	 *           description.addProperty("condition"); description.removeProperty("patient");
	 *           description.addProperty("clinicalStatus");
	 *           description.addProperty("verificationStatus");
	 *           description.addProperty("onsetDate"); description.addProperty("endDate");
	 *           description.addProperty("additionalDetail"); description.addProperty("voided");
	 *           return description; }
	 */
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource#getByUniqueId(java.lang.String)
	 */
	@Override
	public OrderGroup getByUniqueId(String uuid) {
		return orderService.getOrderGroupByUuid(uuid);
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource#delete(java.lang.Object,
	 *      java.lang.String, org.openmrs.module.webservices.rest.web.RequestContext)
	 */
	@Override
	protected void delete(OrderGroup orderGroup, String reason, RequestContext requestContext) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
		// TODO this probably needs to void each individual order
		// and then void the order group itself.
		// Or maybe it isn't needed at all. Orders are immutable, maybe order groups shouldn't
		// be deletable
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource#newDelegate()
	 */
	@Override
	public OrderGroup newDelegate() {
		return new OrderGroup();
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource#save(java.lang.Object)
	 */
	@Override
	public OrderGroup save(OrderGroup orderGroup) {
		return orderService.saveOrderGroup(orderGroup);
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource#purge(java.lang.Object,
	 *      org.openmrs.module.webservices.rest.web.RequestContext)
	 */
	@Override
	public void purge(OrderGroup orderGroup, RequestContext requestContext) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
}
