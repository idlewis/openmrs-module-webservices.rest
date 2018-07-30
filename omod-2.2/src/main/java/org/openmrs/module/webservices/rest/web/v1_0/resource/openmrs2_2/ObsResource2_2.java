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

import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.api.RestService;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.EmptySearchResult;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs1_8.OrderResource1_8;
import org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs2_1.ObsResource2_1;

/**
 * Resource for `obs`, supporting the new properties added in openmrs-core 2.1 (status and
 * interpretation)
 */
@Resource(name = RestConstants.VERSION_1 + "/obs", supportedClass = Obs.class, supportedOpenmrsVersions = { "2.2.*" })
public class ObsResource2_2 extends ObsResource2_1 {
	
	/**
	 * Add ability to get obs based on order id
	 * 
	 * @param context
	 * @see org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource#doSearch(org.openmrs.module.webservices.rest.web.RequestContext)
	 */
	@Override
	protected PageableResult doSearch(RequestContext context) {
		String orderUuid = context.getRequest().getParameter("order");
		if (orderUuid != null) {
			Order ord = ((OrderResource1_8) Context.getService(RestService.class).getResourceBySupportedClass(
			    Order.class)).getByUniqueId(orderUuid);
			if (ord == null)
				return new EmptySearchResult();
			
			List<Obs> obs = Context.getObsService().getObservationsByOrder(ord);
			return new NeedsPaging<Obs>(obs, context);
		}
		
		return super.doSearch(context);
	}
}
