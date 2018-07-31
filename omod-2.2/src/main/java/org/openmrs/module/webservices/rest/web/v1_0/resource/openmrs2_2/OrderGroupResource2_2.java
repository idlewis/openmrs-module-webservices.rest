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
import java.util.Set;

import org.openmrs.Order;
import org.openmrs.OrderGroup;
import org.openmrs.Patient;
import org.openmrs.api.OrderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.api.RestService;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.EmptySearchResult;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ConversionException;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs1_9.PatientResource1_9;

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
			description.addProperty("display");
			description.addProperty("orderGroupReason", Representation.REF);
			description.addProperty("parentOrderGroup", Representation.REF);
			description.addProperty("previousOrderGroup", Representation.REF);
			description.addProperty("nestedOrderGroups", Representation.REF);
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
			description.addProperty("display");
			description.addProperty("orderGroupReason");
			description.addProperty("parentOrderGroup", Representation.REF);
			description.addProperty("previousOrderGroup", Representation.REF);
			description.addProperty("nestedOrderGroups", Representation.FULL);
			description.addSelfLink();
			return description;
		}
		return null;
	}
	
	@PropertySetter("orders")
	public static void setOrders(OrderGroup instance, Set<Order> orders) {
		// TODO These should possibly get added with a position of the order
		// currently has one
		for (Order o : orders)
			instance.addOrder(o);
	}
	
	/*
	 * TODO add the swagger documentation methods 
	 * https://wiki.openmrs.org/display/docs/Documenting+REST+Resources 
	 */
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResource#getCreatableProperties()
	 *      TODO This probably needs orders added, and also patient should come from the encounter
	 */
	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addRequiredProperty("encounter");
		description.addProperty("orderSet"); // TODO how do I make this fail if it is invalid
		// this is an OrderSet property on OrderGroup, so the resource needs to check it
		description.addRequiredProperty("orders"); // TODO this will be empty for a parent
		// order group so it can't be required, but maybe the service should check for empty ones
		// TODO 'encounter' is a required property on order, but it should really come from the
		// encounter
		description.addProperty("orderGroupReason");
		description.addProperty("previousOrderGroup");
		description.addProperty("nestedOrderGroups");
		return description;
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResource#getUpdatableProperties()
	 *      TODO fix this up, may not need it
	 */
	/*
	 * @Override 
	 * public DelegatingResourceDescription getUpdatableProperties() {
	 *           DelegatingResourceDescription description = new DelegatingResourceDescription();
	 *           description.addProperty("condition"); 
	 *           description.removeProperty("patient");
	 *           return description; 
	 * }
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
		// TODO should this fail if the number of orders is 0?
		// seems odd to have an empty order group
		// validation on the new fields
		// orderGroupReason doesn't have a useful subset, so we can't limit it to anything other than concept
		// parentOrderGroup needs to be set in the service
		// previousOrderGroup should be an order group, but we can't check that
		/*			description.addProperty("orderGroupReason");
		            description.addProperty("parentOrderGroup", Representation.REF);
		            description.addProperty("previousOrderGroup", Representation.REF);
		*/
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
	
	/**
	 * Gets orders by given patient (paged according to context if necessary) only if a patient
	 * parameter exists in the request set on the {@link RequestContext}, optional careSetting,
	 * asOfDate request parameters can be specified to filter on
	 * 
	 * @param context
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource#doSearch(org.openmrs.module.webservices.rest.web.RequestContext)
	 * @return all orders for a given patient (possibly filtered by context.type)
	 */
	@Override
	protected PageableResult doSearch(RequestContext context) {
		String patientUuid = context.getRequest().getParameter("patient");
		if (patientUuid == null) {
			return new EmptySearchResult();
		}
		
		// TODO URK this cast is nasty, and is it correct?
		Patient patient = ((PatientResource1_9) Context.getService(RestService.class).getResourceBySupportedClass(
		    Patient.class)).getByUniqueId(patientUuid);
		if (patient == null) {
			return new EmptySearchResult();
		}
		
		List<OrderGroup> groups = Context.getOrderService().getOrderGroupsByPatient(patient);
		
		return new NeedsPaging<OrderGroup>(groups, context);
		
	}
	
	/**
	 * Display string for {@link OrderGroup}
	 * 
	 * @param orderGroup
	 * @return ConceptName
	 */
	@PropertyGetter("display")
	public String getDisplayString(OrderGroup orderGroup) {
		// TODO this seems to be required for paging, but there is no obvious
		// field at the moment which we can use for something meaningful. Could aggregate
		// all the order concepts I suppose?
		return null;
	}
	
	/**
	 * Returns null if there are no nested groups
	 */
	@PropertyGetter("nestedOrderGroups")
	public static Object getNestedMembers(OrderGroup orderGroup) throws ConversionException {
		if (orderGroup.getNestedOrderGroups() != null &&
		        orderGroup.getNestedOrderGroups().size() > 0) {
			return orderGroup.getNestedOrderGroups();
		}
		return null;
	}
	
	/**
	 * Sets the members of the nestedOrderGroups
	 * 
	 * @param obsGroup the obs group whose members to set
	 * @param members the members to set
	 */
	@PropertySetter("nestedOrderGroups")
	public static void setNestedMembers(OrderGroup orderGroup, Set<OrderGroup> members) {
		for (OrderGroup member : members) {
			member.setParentOrderGroup(orderGroup);
		}
		orderGroup.setNestedOrderGroups(members);
	}
	
}
