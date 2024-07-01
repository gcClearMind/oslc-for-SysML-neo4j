package com.example.oslc.contoller;

import com.example.oslc.constant.NsConstant;
import com.example.oslc.servlet.ServiceProviderCatalogSingleton;
import org.eclipse.lyo.oslc4j.core.annotation.OslcService;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.OslcMediaType;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@OslcService(OslcConstants.OSLC_CORE_DOMAIN)
@RestController
@RequestMapping("/catalog")
public class ServiceProviderCatalogController {
    @Context private HttpServletRequest httpServletRequest;
    @Context private HttpServletResponse httpServletResponse;
    @Context private UriInfo uriInfo;


    @GetMapping("/")
    public Response getServiceProviderCatalogs() throws IOException, URISyntaxException
    {
        String forwardUri = uriInfo.getAbsolutePath() + "/singleton";
        httpServletResponse.sendRedirect(forwardUri);
        return Response.seeOther(new URI(forwardUri)).build();
    }

    @GetMapping("{serviceProviderCatalogId}") // Required to distinguish from array result.  But, ignored.
    @Produces({OslcMediaType.APPLICATION_RDF_XML, OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON})
    public ServiceProviderCatalog getServiceProviderCatalog()
    {
        ServiceProviderCatalog catalog =  ServiceProviderCatalogSingleton.getServiceProviderCatalog(httpServletRequest);

        if (catalog != null) {

            httpServletResponse.addHeader(NsConstant.HDR_OSLC_VERSION,"2.0");
            return catalog;
        }

        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }


}
