package com.example.oslc.contoller;


import com.alibaba.fastjson2.JSON;
import com.example.oslc.constant.NsConstant;
import com.example.oslc.resource.BlockResource;
import com.example.oslc.service.BlockService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;

import javax.ws.rs.core.Response;

import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.lyo.oslc4j.core.annotation.*;
import org.eclipse.lyo.oslc4j.core.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.example.oslc.servlet.ServiceProviderCatalogSingleton;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Controller
@OslcService(NsConstant.oslc_neo4j_namespace)
@RequestMapping("{productId}/Blocks")

public class  BlockController {

    @Autowired
    private BlockService blockService;

    @OslcDialogs(
            {
                    @OslcDialog
                            (
                                    title = "Block Selection Dialog",
                                    label = "Block Selection Dialog",
                                    uri = "/{productId}/Blocks/selector",
                                    hintWidth = "525px",
                                    hintHeight = "325px",
                                    resourceTypes = {NsConstant.BLOCK_NAMESPACE},
                                    usages = {OslcConstants.OSLC_USAGE_DEFAULT}

                            )

            })
    @OslcQueryCapability
            (
                    title = "Block Query Capability",
                    label = "Block Catalog Query",
                    resourceShape = OslcConstants.PATH_RESOURCE_SHAPES + "/" + NsConstant.PATH_BLOCK,
                    resourceTypes = {NsConstant.BLOCK_NAMESPACE},
                    usages = {OslcConstants.OSLC_USAGE_DEFAULT}
            )

    @ResponseBody
    @GetMapping("")
    public List<BlockResource> getAllBlocks(@PathVariable String productId,
                                    HttpServletRequest httpServletRequest) throws IOException {
        return blockService.getAllBlockResources(httpServletRequest, productId);
    }

    @OslcDialog
            (
                    title = "Change Request Creation Dialog",
                    label = "Change Request Creation Dialog",
                    uri = "/{productId}/Blocks/creator",
                    hintWidth = "600px",
                    hintHeight = "375px",
                    resourceTypes = {NsConstant.BLOCK_NAMESPACE},
                    usages = {OslcConstants.OSLC_USAGE_DEFAULT}
            )
    @OslcCreationFactory
            (
                    title = "Change Request Creation Factory",
                    label = "Change Request Creation",
                    resourceShapes = {OslcConstants.PATH_RESOURCE_SHAPES + "/" + NsConstant.PATH_BLOCK},
                    resourceTypes = {NsConstant.BLOCK_NAMESPACE},
                    usages = {OslcConstants.OSLC_USAGE_DEFAULT}
            )

    @ResponseBody
    @PostMapping("")
    public String addResource(@PathVariable final String productId,
                                final BlockResource blockResource) throws IOException, ServletException {
        return null;
    }



//
//    @GetMapping("/{BlockId}")
//    public BlockResource getBlockById(@PathVariable String productId,
//                                      @PathVariable String BlockId,
//                                      HttpServletRequest httpServletRequest) throws IOException {
//        return blockService.getResourceById(httpServletRequest, productId, BlockId);
//    }




    @GetMapping(value = "/selector")
    public String BlockSelector(@PathVariable String productId,
                                HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Model model) throws ServletException, IOException
    {

        ServiceProviderCatalog catalog = ServiceProviderCatalogSingleton.getServiceProviderCatalog(httpServletRequest);
        ServiceProvider serviceProvider = ServiceProviderCatalogSingleton.getServiceProvider(httpServletRequest, productId);

        model.addAttribute("catalog", catalog);
        model.addAttribute("serviceProvider", serviceProvider);
//        model.addAttribute("HostUri", NsConstant.data_path);
//        model.addAttribute("selectionUri",ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString());


        return "selectResource";

    }


    @ResponseBody
    @PostMapping(value = "/creator")
    public String BlockResourceCreator(@PathVariable String productId,
                                     Model model){
//        model.addAttribute("blockResource", new BlockResource());
        return "createResource";
    }


    @GetMapping(value = "{blockId}/smallPreview", produces = {MediaType.TEXT_HTML})
    public String smallPreview(@PathVariable("productId")       final String productId,
                               @PathVariable("blockId") final String blockId,
                               HttpServletRequest httpServletRequest,
                               Model model)
    {
        BlockResource resource = blockService.getResourceById(httpServletRequest, productId, blockId);
        model.addAttribute("resource", resource);
        return "smallPreview";
    }


    @ResponseBody
    @PostMapping("/queryResource")
    public List<BlockResource> queryResourceById(@RequestParam(name = "oslc.where", required = false, defaultValue = "") String oslcWhere,
                                    HttpServletRequest httpServletRequest,
                                    @PathVariable("productId")       final String productId) throws Exception {
        System.out.println("queryResource");
        // 返回查询到的资源的RDF数据
        List<BlockResource> list = new ArrayList<>();
        list.add(blockService.queryResourceById(oslcWhere, httpServletRequest, productId));
        return list;
    }

    @GetMapping(value = "/{BlockId}", produces = {OslcMediaType.APPLICATION_X_OSLC_COMPACT_XML})
    public Compact getCompact(@PathVariable("productId") final String productId,
                              @PathVariable("BlockId") final String BlockId,
                              HttpServletRequest httpServletRequest)
            throws URISyntaxException, IOException, ServletException
    {
        BlockResource resource = blockService.getResourceById(httpServletRequest, productId, BlockId);

        if (resource != null) {
            final Compact compact = new Compact();


            compact.setAbout(resource.getAbout());
//            compact.setTitle(resource.getTitle());

//            String iconUri = Uri() + "/images/favicon.ico";
//            compact.setIcon(new URI(iconUri));

            //Create and set attributes for OSLC preview resource
            final Preview smallPreview = new Preview();
            smallPreview.setHintHeight("11em");
            smallPreview.setHintWidth("45em");
            smallPreview.setDocument(new URI(compact.getAbout().toString() + "/smallPreview"));
            compact.setSmallPreview(smallPreview);

            //Use the HTML representation of a change request as the large preview as well
            final Preview largePreview = new Preview();
            largePreview.setHintHeight("20em");
            largePreview.setHintWidth("45em");
            largePreview.setDocument(compact.getAbout());
            compact.setLargePreview(largePreview);

            return compact;
        }

        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }
}
