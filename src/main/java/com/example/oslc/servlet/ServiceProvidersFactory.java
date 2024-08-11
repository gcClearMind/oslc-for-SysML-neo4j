/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 *  
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *
 *     Russell Boykin       - initial API and implementation
 *     Alberto Giammaria    - initial API and implementation
 *     Chris Peters         - initial API and implementation
 *     Gianluca Bernardini  - initial API and implementation
 *     Michael Fiedler      - Bugzilla adapter implementation
 *******************************************************************************/
package com.example.oslc.servlet;

import com.example.oslc.contoller.BlockController;
import com.example.oslc.info.ServiceProviderInfo;
import org.eclipse.lyo.oslc.domains.DctermsDomainConstants;
import org.eclipse.lyo.oslc.domains.FoafDomainConstants;
import org.eclipse.lyo.oslc.domains.rm.Oslc_rmDomainConstants;
import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;
import org.eclipse.lyo.oslc4j.core.exception.OslcCoreApplicationException;
import org.eclipse.lyo.oslc4j.core.model.*;

import jakarta.ws.rs.core.UriBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ServiceProvidersFactory
{
    private static Class<?>[] RESOURCE_CLASSES =
    {
        BlockController.class
    };

    private ServiceProvidersFactory()
    {
        super();
    }


    public static URI constructURI(final String serviceProviderId)
    {
        String basePath = OSLC4JUtils.getServletURI();
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("serviceProviderId", serviceProviderId);
        String instanceURI = "serviceProviders/{serviceProviderId}";

        System.out.println("basePath: " + basePath);
        if(basePath == null) {
            basePath = "";
        }
        final UriBuilder builder = UriBuilder.fromUri(basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }

    public static URI constructURI(final ServiceProviderInfo serviceProviderInfo)
    {
        return constructURI(serviceProviderInfo.ServiceProviderId);
    }

    public static String constructIdentifier(final String serviceProviderId)
    {
        return serviceProviderId;
    }

    public static String  constructIdentifier(final ServiceProviderInfo serviceProviderInfo)
    {
        return constructIdentifier(serviceProviderInfo.ServiceProviderId);
    }


    public static ServiceProvider createServiceProvider(final ServiceProviderInfo serviceProviderInfo)
            throws OslcCoreApplicationException, URISyntaxException, IllegalArgumentException {
        // Start of user code init
        // End of user code
        URI serviceProviderURI = constructURI(serviceProviderInfo);
        String identifier = constructIdentifier(serviceProviderInfo);
        String ServletURI = OSLC4JUtils.getServletURI();
        if(ServletURI == null) {
            ServletURI = "";
        }
        String title = serviceProviderInfo.name;
        String description = String.format("%s (id: %s; kind: %s)",
                "Service Provider for the neo4j-SysML resources",
                identifier,
                "RM Service Provider");
        Publisher publisher = null;
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("productId", serviceProviderInfo.ServiceProviderId);

//        ServiceProvider serviceProvider = org.eclipse.lyo.oslc4j.core.model.ServiceProviderFactory.createServiceProvider(ServletURI,
//                "",
//                title,
//                description,
//                publisher,
//                RESOURCE_CLASSES,
//                parameterMap);
        ServiceProvider serviceProvider = ServiceProviderFactory.createServiceProvider(ServletURI,
                "",
                title,
                description,
                publisher,
                RESOURCE_CLASSES,
                parameterMap);
        final PrefixDefinition[] prefixDefinitions =
                {
                        new PrefixDefinition(OslcConstants.DCTERMS_NAMESPACE_PREFIX, new URI(OslcConstants.DCTERMS_NAMESPACE)),
                        new PrefixDefinition(OslcConstants.OSLC_CORE_NAMESPACE_PREFIX, new URI(OslcConstants.OSLC_CORE_NAMESPACE)),
                        new PrefixDefinition(OslcConstants.OSLC_DATA_NAMESPACE_PREFIX, new URI(OslcConstants.OSLC_DATA_NAMESPACE)),
                        new PrefixDefinition(OslcConstants.RDF_NAMESPACE_PREFIX, new URI(OslcConstants.RDF_NAMESPACE)),
                        new PrefixDefinition(OslcConstants.RDFS_NAMESPACE_PREFIX, new URI(OslcConstants.RDFS_NAMESPACE)),

                        new PrefixDefinition(DctermsDomainConstants.DUBLIN_CORE_NAMSPACE_PREFIX, new URI(DctermsDomainConstants.DUBLIN_CORE_NAMSPACE))
                        ,
                        new PrefixDefinition(FoafDomainConstants.FOAF_NAMSPACE_PREFIX, new URI(FoafDomainConstants.FOAF_NAMSPACE))
                        ,
                        new PrefixDefinition(OslcDomainConstants.OSLC_NAMSPACE_PREFIX, new URI(OslcDomainConstants.OSLC_NAMSPACE))
                        ,
                        new PrefixDefinition(Oslc_rmDomainConstants.REQUIREMENTS_MANAGEMENT_SHAPES_NAMSPACE_PREFIX, new URI(Oslc_rmDomainConstants.REQUIREMENTS_MANAGEMENT_SHAPES_NAMSPACE))
                };
        serviceProvider.setPrefixDefinitions(prefixDefinitions);

        serviceProvider.setAbout(serviceProviderURI);
        serviceProvider.setIdentifier(identifier);
        serviceProvider.setCreated(new Date());
        serviceProvider.setDetails(new URI[] {serviceProviderURI});

        // Start of user code finalize todo serviceProvider - service

        // End of user code
        return serviceProvider;
    }

//    public static ServiceProvider initServiceProvider(ServiceProvider serviceProvider, String baseURI, String genericBaseURI, String title, String description, Publisher publisher, Class<?>[] resourceClasses, Map<String, Object> pathParameterValues) throws OslcCoreApplicationException, URISyntaxException {
//        serviceProvider.setTitle(title);
//        serviceProvider.setDescription(description);
//        serviceProvider.setPublisher(publisher);
//
//        Map<String, Service> serviceMap = new HashMap<>();
//        for (Class<?> resourceClass : resourceClasses) {
//            OslcService serviceAnnotation = resourceClass.getAnnotation(OslcService.class);
//            if (serviceAnnotation == null) {
//                throw new OslcCoreMissingAnnotationException(resourceClass, OslcService.class);
//            }
//
//            String domain = serviceAnnotation.value();
//            Service service = serviceMap.computeIfAbsent(domain, k -> {
//                try {
//                    return new Service(new URI(domain));
//                } catch (URISyntaxException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//
//            Map<String, Object> initPathParameterValues = pathParameterValues != null ? pathParameterValues : new HashMap<>();
//            handleResourceClass(baseURI, genericBaseURI, resourceClass, service, initPathParameterValues);
//        }
//
//        for (Service service : serviceMap.values()) {
//            serviceProvider.addService(service);
//        }
//
//        return serviceProvider;
//    }

}
