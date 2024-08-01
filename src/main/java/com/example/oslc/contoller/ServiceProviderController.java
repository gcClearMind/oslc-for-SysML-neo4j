package com.example.oslc.contoller;


import com.example.oslc.servlet.ServiceProviderCatalogSingleton;
import org.apache.jena.base.Sys;
import org.eclipse.lyo.oslc4j.core.annotation.OslcDialog;
import org.eclipse.lyo.oslc4j.core.annotation.OslcQueryCapability;
import org.eclipse.lyo.oslc4j.core.annotation.OslcService;
import org.eclipse.lyo.oslc4j.core.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;

import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;

import java.net.URI;

@OslcService(OslcConstants.OSLC_CORE_DOMAIN)
@Controller
@RequestMapping("serviceProviders")
public class ServiceProviderController {
    @OslcDialog
            (
                    title = "Service Provider Selection Dialog",
                    label = "Service Provider Selection Dialog",
                    uri = "",
                    hintWidth = "1000px",
                    hintHeight = "600px",
                    resourceTypes = {OslcConstants.TYPE_SERVICE_PROVIDER},
                    usages = {OslcConstants.OSLC_USAGE_DEFAULT}
            )
    @OslcQueryCapability
            (
                    title = "Service Provider Query Capability",
                    label = "Service Provider Query",
                    resourceShape = OslcConstants.PATH_RESOURCE_SHAPES + "/" + OslcConstants.PATH_SERVICE_PROVIDER,
                    resourceTypes = {OslcConstants.TYPE_SERVICE_PROVIDER},
                    usages = {OslcConstants.OSLC_USAGE_DEFAULT}
            )

    @GetMapping("")
    @ResponseBody
    @Produces({OslcMediaType.APPLICATION_RDF_XML, OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON})
    public ServiceProvider[] getServiceProviders(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse)
    {
        httpServletResponse.addHeader("Oslc-Core-Version","2.0");
        return ServiceProviderCatalogSingleton.getServiceProviders(httpServletRequest);
    }


    @GetMapping("/{serviceProviderId}")
    public String getServiceProvider( @PathVariable("serviceProviderId") final String serviceProviderId,
                                      HttpServletRequest httpServletRequest,
                                      HttpServletResponse httpServletResponse,
                                      Model model)
    {
        httpServletResponse.addHeader("Oslc-Core-Version","2.0");
        System.out.println(serviceProviderId);
        ServiceProvider serviceProvider = ServiceProviderCatalogSingleton.getServiceProvider(httpServletRequest, serviceProviderId);

        Service[] services = serviceProvider.getServices();

        Service service = services[0];
//OSLC Dialogs
        Dialog[] selectionDialogs = service.getSelectionDialogs();
        String selectionDialog = selectionDialogs[0].getDialog().toString();

        Dialog [] creationDialogs = service.getCreationDialogs();
        String creationDialog = creationDialogs[0].getDialog().toString();



//OSLC CreationFactory and shape
        CreationFactory [] creationFactories = service.getCreationFactories();

        String creationFactory = creationFactories[0].getCreation().toString();
        URI[] creationShapes = creationFactories[0].getResourceShapes();
        String creationShape = creationShapes[0].toString();

//OSLC QueryCapability and shape
        QueryCapability [] queryCapabilities= service.getQueryCapabilities();
        String queryCapability = queryCapabilities[0].getQueryBase().toString();
        String queryShape = queryCapabilities[0].getResourceShape().toString();

        model.addAttribute("serviceProvider", serviceProvider);
        model.addAttribute("service", service);

        model.addAttribute("selectionDialog",selectionDialog);
        model.addAttribute("creationDialog",creationDialog);
        model.addAttribute("creationFactory", creationFactory);
        model.addAttribute("creationShape", creationShape);
        model.addAttribute("queryCapability", queryCapability);
        model.addAttribute("queryShape", queryShape);


        System.out.println("selectionDialog:"+selectionDialog);
        System.out.println("creationDialog:"+creationDialog);
        System.out.println("creationFactory:"+creationFactory);
        System.out.println("creationShape:"+creationShape);
        System.out.println("queryCapability:"+queryCapability);
        System.out.println("queryShape:"+queryShape);

        return "ServiceProvider";
    }
}
