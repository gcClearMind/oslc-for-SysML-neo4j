package com.example.oslc.contoller;


import com.alibaba.fastjson2.JSON;
import com.example.oslc.constant.NsConstant;
import com.example.oslc.resource.BlockResource;
import com.example.oslc.service.BlockService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.oslc.servlet.ServiceProviderCatalogSingleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.lyo.oslc4j.core.annotation.OslcDialog;
import org.eclipse.lyo.oslc4j.core.annotation.OslcDialogs;
import org.eclipse.lyo.oslc4j.core.annotation.OslcQueryCapability;
import org.eclipse.lyo.oslc4j.core.annotation.OslcService;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URISyntaxException;


@OslcService(NsConstant.BLOCK_NAMESPACE)
@RequestMapping("/{productId}/block")
@RestController
public class BlockContoller {

    @Autowired
    private BlockService blockService;

    @OslcDialogs(
            {
                    @OslcDialog
                            (
                                    title = "Change Request Selection Dialog",
                                    label = "Change Request Selection Dialog",
                                    uri = "/{productId}/changeRequests/selector",
                                    hintWidth = "525px",
                                    hintHeight = "325px",
                                    resourceTypes = {NsConstant.BLOCK_NAMESPACE},
                                    usages = {OslcConstants.OSLC_USAGE_DEFAULT}
                            )

            })
    @OslcQueryCapability
            (
                    title = "Change Request Query Capability",
                    label = "Change Request Catalog Query",
                    resourceShape = OslcConstants.PATH_RESOURCE_SHAPES + "/" + "block",
                    resourceTypes = {NsConstant.BLOCK_NAMESPACE},
                    usages = {OslcConstants.OSLC_USAGE_DEFAULT}
            )

    @GetMapping("")
    public void getAllBlock(HttpServletResponse httpServletResponse) throws IOException {
        String uriInfo = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        String forwardUri = uriInfo + "/getAllBlockResourceURI";
        System.out.println(forwardUri);
        httpServletResponse.sendRedirect(forwardUri);
    }

    @GetMapping("/getAllBlockResourceURI")
    public Object getAllBlockResourceURI()  {
        return JSON.toJSON(blockService.getAllResourceURIs());
    }



    @GetMapping("/selector")
    @Consumes({ MediaType.TEXT_HTML, MediaType.WILDCARD })
    public String BlockSelector(@QueryParam("terms")     final String terms,
                                      @PathParam("productId")  final String productId,
                                      HttpServletRequest httpServletRequest,
                                      HttpServletResponse httpServletResponse,
                                      Model model) throws ServletException, IOException
    {
        int productIdNum = Integer.parseInt(productId);
//        httpServletRequest.setAttribute("productId", productIdNum);
//        httpServletRequest.setAttribute("HostUri", NsConstant.data_path);
//        httpServletRequest.setAttribute("selectionUri",ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString());

        model.addAttribute("productId", productIdNum);
        model.addAttribute("HostUri", NsConstant.data_path);
        model.addAttribute("selectionUri",ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString());
        if (terms != null ) {
//            httpServletRequest.setAttribute("terms", terms);
            model.addAttribute("terms", terms);
            return "error";
        } else {
            return "selector";
        }
    }


    @GetMapping("{changeRequestId}/smallPreview")
    @Produces({ MediaType.TEXT_HTML })
    public String smallPreview(@PathParam("productId")       final String productId,
                             @PathParam("blockId") final String blockId,
                             Model model) throws ServletException, IOException, URISyntaxException
    {
        BlockResource resource = blockService.getResourceById(blockId);
        model.addAttribute("resource", resource);
        return "smallPreview";
    }


}
