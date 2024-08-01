package com.example.oslc.contoller;

import com.example.oslc.constant.NsConstant;
import com.example.oslc.servlet.ServiceProviderCatalogSingleton;
import org.eclipse.lyo.oslc4j.core.annotation.OslcDialog;
import org.eclipse.lyo.oslc4j.core.annotation.OslcQueryCapability;
import org.eclipse.lyo.oslc4j.core.annotation.OslcService;
import org.eclipse.lyo.oslc4j.core.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;



import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@OslcService(OslcConstants.OSLC_CORE_DOMAIN)
@Controller
@RequestMapping("catalog")
public class ServiceProviderCatalogController  {

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

    @GetMapping("")
    public void getServiceProviderCatalogs(HttpServletResponse httpServletResponse) throws IOException, URISyntaxException
    {
        String uriInfo = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        String forwardUri = uriInfo + "/singleton";
        System.out.println(forwardUri);
        httpServletResponse.sendRedirect(forwardUri);
//        return httpServletResponse.sendRedirect(new URI(forwardUri)).build();

    }

//    @RequestMapping("/{serviceProviderCatalogId}") // Required to distinguish from array result.  But, ignored.
//    @Produces({OslcMediaType.APPLICATION_RDF_XML, OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON})
//    public ServiceProviderCatalog getServiceProviderCatalog(
//            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
//    {
//        ServiceProviderCatalog catalog =  ServiceProviderCatalogSingleton.getServiceProviderCatalog(httpServletRequest);
//
//        if (catalog != null) {
//
//            httpServletResponse.addHeader("OSLC-Core-Version","2.0");
//            return catalog;
//        }
//
//        throw new WebApplicationException(Response.Status.NOT_FOUND);
//    }

    @GetMapping("{serviceProviderId}")
//    @Produces(MediaType.TEXT_HTML)
    public String getHtmlServiceProvider(@PathVariable("serviceProviderId") final String serviceProviderId,
                                         HttpServletRequest httpServletRequest,
                                         Model model)
    {
        ServiceProviderCatalog catalog = ServiceProviderCatalogSingleton.getServiceProviderCatalog(httpServletRequest);

        model.addAttribute("catalog", catalog);
        System.out.println(catalog.getAbout());
        for(ServiceProvider sc : catalog.getServiceProviders()) {
            System.out.println(sc.getAbout());
            for (Service ss : sc.getServices()) {
                System.out.println(ss);
            }
        }
//        return catalog;
        return "ServiceProviderCatalog";
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
