package com.example.oslc.contoller;


import com.example.oslc.servlet.ServiceProviderCatalogSingleton;
import org.eclipse.lyo.oslc4j.core.annotation.OslcDialog;
import org.eclipse.lyo.oslc4j.core.annotation.OslcQueryCapability;
import org.eclipse.lyo.oslc4j.core.annotation.OslcService;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.OslcMediaType;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;

@OslcService(OslcConstants.OSLC_CORE_DOMAIN)
@RestController
@RequestMapping("/serviceProviders")
public class ServiceProviderController {
//    @Context private HttpServletRequest httpServletRequest;
//    @Context private HttpServletResponse httpServletResponse;
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


    @GetMapping("/")
    @Produces({OslcMediaType.APPLICATION_RDF_XML, OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON})
    public ServiceProvider[] getServiceProviders(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse)
    {
        httpServletResponse.addHeader("Oslc-Core-Version","2.0");
        return ServiceProviderCatalogSingleton.getServiceProviders(httpServletRequest);
    }

    @GetMapping("/{serviceProviderId}")
    @Produces({OslcMediaType.APPLICATION_RDF_XML, OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON})
    public ServiceProvider getServiceProvider(HttpServletRequest httpServletRequest,
                                              HttpServletResponse httpServletResponse,
                                              @PathParam("serviceProviderId") final String serviceProviderId)
    {
        httpServletResponse.addHeader("Oslc-Core-Version","2.0");
        return ServiceProviderCatalogSingleton.getServiceProvider(httpServletRequest, serviceProviderId);
    }
}
