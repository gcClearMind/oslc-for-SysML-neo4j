package com.example.oslc.contoller;


import com.example.oslc.constant.NsConstant;
import com.example.oslc.resource.NodeResource;
import com.example.oslc.service.NodeService;
import com.example.oslc.servlet.ServiceProviderCatalogSingleton;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.lyo.oslc4j.core.annotation.*;
import org.eclipse.lyo.oslc4j.core.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Controller
@OslcService(NsConstant.oslc_neo4j_namespace)
@RequestMapping("{productId}/Nodes")

public class NodeController {

    @Autowired
    private NodeService NodeService;

    @OslcDialogs(
            {
                    @OslcDialog
                            (
                                    title = "Node Selection Dialog",
                                    label = "Node Selection Dialog",
                                    uri = "/{productId}/Nodes/selector",
                                    hintWidth = "525px",
                                    hintHeight = "325px",
                                    resourceTypes = {NsConstant.NODE_NAMESPACE},
                                    usages = {OslcConstants.OSLC_USAGE_DEFAULT}

                            )

            })
    @OslcQueryCapability
            (
                    title = "Node Query Capability",
                    label = "Node Catalog Query",
                    resourceShape = OslcConstants.PATH_RESOURCE_SHAPES + "/" + NsConstant.PATH_Node,
                    resourceTypes = {NsConstant.BLOCK_NAMESPACE},
                    usages = {OslcConstants.OSLC_USAGE_DEFAULT}
            )

    @ResponseBody
    @GetMapping("")
    public List<NodeResource> getAllNodes(@PathVariable String productId,
                                    HttpServletRequest httpServletRequest) throws IOException {
        return NodeService.getAllNodeResources(httpServletRequest, productId);
    }

    @OslcDialog
            (
                    title = "Change Request Creation Dialog",
                    label = "Change Request Creation Dialog",
                    uri = "/{productId}/Nodes/creator",
                    hintWidth = "600px",
                    hintHeight = "375px",
                    resourceTypes = {NsConstant.NODE_NAMESPACE},
                    usages = {OslcConstants.OSLC_USAGE_DEFAULT}
            )
    @OslcCreationFactory
            (
                    title = "Change Request Creation Factory",
                    label = "Change Request Creation",
                    resourceShapes = {OslcConstants.PATH_RESOURCE_SHAPES + "/" + NsConstant.PATH_Node},
                    resourceTypes = {NsConstant.NODE_NAMESPACE},
                    usages = {OslcConstants.OSLC_USAGE_DEFAULT}
            )

    @ResponseBody
    @PostMapping("")
    public String addResource(@PathVariable final String productId,
                                final NodeResource NodeResource) throws IOException, ServletException {
        return null;
    }



//
//    @GetMapping("/{NodeId}")
//    public NodeResource getNodeById(@PathVariable String productId,
//                                      @PathVariable String NodeId,
//                                      HttpServletRequest httpServletRequest) throws IOException {
//        return NodeService.getResourceById(httpServletRequest, productId, NodeId);
//    }




    @GetMapping(value = "/selector")
    public String NodeSelector(@PathVariable String productId,
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
    public String NodeResourceCreator(@PathVariable String productId,
                                     Model model){
//        model.addAttribute("NodeResource", new NodeResource());
        return "createResource";
    }


    @GetMapping(value = "{NodeId}/smallPreview", produces = {MediaType.TEXT_HTML})
    public String smallPreview(@PathVariable("productId")       final String productId,
                               @PathVariable("NodeId") final String NodeId,
                               HttpServletRequest httpServletRequest,
                               Model model)
    {
        NodeResource resource = NodeService.getResourceById(httpServletRequest, productId, NodeId);
        model.addAttribute("resource", resource);
        return "smallPreview";
    }



    @ResponseBody
    @PostMapping("/queryResource")
    public List<NodeResource> queryResourceById(@RequestParam(name = "oslc.where", required = false, defaultValue = "") String oslcWhere,
                                    HttpServletRequest httpServletRequest,
                                    @PathVariable("productId")       final String productId) throws Exception {
        System.out.println("queryResource");
        // 返回查询到的资源的RDF数据
        List<NodeResource> list = new ArrayList<>();
        list.add(NodeService.queryResourceById(oslcWhere, httpServletRequest, productId));
        return list;
    }

    @GetMapping(value = "/{NodeId}", produces = {OslcMediaType.APPLICATION_X_OSLC_COMPACT_XML})
    public Compact getCompact(@PathVariable("productId") final String productId,
                              @PathVariable("NodeId") final String NodeId,
                              HttpServletRequest httpServletRequest)
            throws URISyntaxException, IOException, ServletException
    {
        NodeResource resource = NodeService.getResourceById(httpServletRequest, productId, NodeId);

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
