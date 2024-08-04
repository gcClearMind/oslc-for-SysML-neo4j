package com.example.oslc.contoller;


import com.alibaba.fastjson2.JSON;
import com.example.oslc.constant.NsConstant;
import com.example.oslc.resource.BlockResource;
import com.example.oslc.service.BlockService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.lyo.oslc4j.core.annotation.*;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
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
        int productIdNum = Integer.parseInt(productId);
//        httpServletRequest.setAttribute("productId", productIdNum);
//        httpServletRequest.setAttribute("HostUri", NsConstant.data_path);
//        httpServletRequest.setAttribute("selectionUri",ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString());

//        model.addAttribute("productId", productIdNum);
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



    @PostMapping("/queryResource")
    public Object queryResourceById(
            @RequestParam(name = "oslc.where", required = false, defaultValue = "") String oslcWhere) throws Exception {
        // 返回查询到的资源的RDF数据
        return null;
    }
}
