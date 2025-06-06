// Start of user code Copyright
/*******************************************************************************
 * Copyright (c) 2017 Jad El-khoury.
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
 *     Jad El-khoury        - initial implementation of ResourceShape HTML presentation
 *     
 * This file is generated by org.eclipse.lyo.oslc4j.codegenerator
 *******************************************************************************/
// End of user code

package com.example.oslc.contoller;

import com.example.oslc.resource.BlockResource;
import com.example.oslc.resource.NodeResource;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;
import org.eclipse.lyo.oslc4j.core.exception.OslcCoreApplicationException;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.OslcMediaType;
import org.eclipse.lyo.oslc4j.core.model.ResourceShape;
import org.eclipse.lyo.oslc4j.core.model.ResourceShapeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

import static com.example.oslc.util.RdfUtil.resourceShapeToRdf;

// Start of user code imports
// End of user code

// Start of user code pre_class_code
// End of user code

@Controller
@RequestMapping(OslcConstants.PATH_RESOURCE_SHAPES)
public class ResourceShapeController
{

    private static final Logger log = LoggerFactory.getLogger(ResourceShapeController.class.getName());
    public ResourceShapeController() throws OslcCoreApplicationException, URISyntaxException {
        super();
    }



    @GetMapping(value = "/{resourceShapePath}",produces = {OslcMediaType.APPLICATION_RDF_XML, OslcMediaType.APPLICATION_XML})
//    @Produces({OslcMediaType.APPLICATION_RDF_XML, OslcMediaType.APPLICATION_XML, OslcMediaType.TEXT_XML, OslcMediaType.APPLICATION_JSON, OslcMediaType.TEXT_TURTLE})
    @Operation(hidden = true)
    public ResponseEntity<String> getResourceShape(@PathVariable("resourceShapePath") final String resourceShapePath,
                                                   HttpServletRequest httpServletRequest)
            throws OslcCoreApplicationException,
            URISyntaxException, UnsupportedEncodingException {
        //todo 暂时固定为展示block   Node
//        final Class<?> resourceClass = Application.getResourceShapePathToResourceClass0Map().get(resourceShapePath);
        final Class<?> resourceClass = NodeResource.class;
        if (resourceClass != null) {
            final String servletUri = OSLC4JUtils.resolveServletUri(httpServletRequest);
            ResourceShape resourceShape = ResourceShapeFactory.createResourceShape(servletUri, OslcConstants.PATH_RESOURCE_SHAPES,
                    resourceShapePath, resourceClass);
            return ResponseEntity.ok().
                    contentType(MediaType.APPLICATION_XML).
                    body(resourceShapeToRdf(resourceShape));
        }
        throw new WebApplicationException(Status.NOT_FOUND);
    }


}
