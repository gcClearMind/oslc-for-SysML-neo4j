package com.example.oslc.contoller;

import com.example.oslc.constant.NsConstant;
import com.example.oslc.servlet.ServiceProviderCatalogSingleton;
import org.eclipse.lyo.oslc4j.core.annotation.OslcDialog;
import org.eclipse.lyo.oslc4j.core.annotation.OslcQueryCapability;
import org.eclipse.lyo.oslc4j.core.annotation.OslcService;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.OslcMediaType;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URISyntaxException;

@OslcService(OslcConstants.OSLC_CORE_DOMAIN)
@RestController
@RequestMapping("/catalog")
public class ServiceProviderCatalogController {
    @Context private HttpServletRequest httpServletRequest;
    @Context private HttpServletResponse httpServletResponse;
    @Context private UriInfo uriInfo;


    @OslcDialog
            (
                    title = "Service Provider Catalog Selection Dialog",
                    label = "Service Provider Catalog Selection Dialog",
                    uri = "/catalog",
                    hintWidth = "1000px",
                    hintHeight = "600px",
                    resourceTypes = {OslcConstants.TYPE_SERVICE_PROVIDER_CATALOG},
                    usages = {OslcConstants.OSLC_USAGE_DEFAULT}
            )
    @OslcQueryCapability
            (
                    title = "Service Provider Catalog Query Capability",
                    label = "Service Provider Catalog Query",
                    resourceShape = OslcConstants.PATH_RESOURCE_SHAPES + "/" + OslcConstants.PATH_SERVICE_PROVIDER_CATALOG,
                    resourceTypes = {OslcConstants.TYPE_SERVICE_PROVIDER_CATALOG},
                    usages = {OslcConstants.OSLC_USAGE_DEFAULT}
            )
    @GetMapping("/")
    public RedirectView getServiceProviderCatalogs(HttpServletResponse response) throws IOException, URISyntaxException
    {
        String forwardUri = uriInfo.getAbsolutePath() + "/singleton";
        response.sendRedirect(forwardUri);
        return new RedirectView(forwardUri);
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


//    @GetMapping("{someId}")
//    @Produces(MediaType.TEXT_HTML)
//    public void getHtmlServiceProvider(@PathParam("someId") final String someId)
//    {
//        ServiceProviderCatalog catalog = ServiceProviderCatalogSingleton.getServiceProviderCatalog(httpServletRequest);
//
//        if (catalog !=null )
//        {
//            httpServletRequest.setAttribute("catalog",catalog);
//            // Start of user code getHtmlServiceProvider_setAttributes
//            // End of user code
//
//            RequestDispatcher rd = httpServletRequest.getRequestDispatcher("/co/oslc/refimpl/rm/gen/serviceprovidercatalog.jsp");
//            try {
//                rd.forward(httpServletRequest, httpServletResponse);
//                return;
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new WebApplicationException(e);
//            }
//        }
//    }
}
