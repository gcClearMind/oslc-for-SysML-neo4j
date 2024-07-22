package com.example.oslc.service;

import com.example.oslc.constant.NsConstant;
import com.example.oslc.resource.BlockResource;
import com.example.oslc.servlet.ServiceProviderCatalogSingleton;
import com.example.oslc.util.RdfUtil;
import com.example.oslc.util.neo4jUtil;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.example.oslc.util.neo4jUtil.getBlockResourceById;

@Service
public class BlockService {
    public String BlockResourceToRdf(List<BlockResource> resources) throws IOException {
        Model model = RdfUtil.createRdfModel();
        for(BlockResource resource : resources) {
            Resource rdfResource = model.createResource(NsConstant.block_path + resource.getId());
            rdfResource.addProperty(RDF.type, model.createResource(NsConstant.BLOCK_NAMESPACE));
            rdfResource.addProperty(model.createProperty(NsConstant.BLOCK_ID), resource.getId().toString());
            rdfResource.addProperty(model.createProperty(NsConstant.BLOCK_NAME), resource.getName());
            rdfResource.addProperty(model.createProperty(NsConstant.BLOCK_XMI_ID), resource.getXmiId());
            rdfResource.addProperty(model.createProperty(NsConstant.BLOCK_XMI_TYPE), resource.getXmiType());
            rdfResource.addProperty(model.createProperty(NsConstant.BLOCK_VISIBILITY), resource.getVisibility());

        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        // RDF/XML-ABBREV 格式输出 RDF，这种格式会将 rdf:type 声明为属性
        // 标准的 RDF/XML 格式输出 RDF，这种格式会将 rdf:type 声明为子元素
        model.write(os, "RDF/XML-ABBREV");
        os.close();
        return os.toString();
    }



    public List<String> getAllResourceURIs()  {
        List<String> res = new ArrayList<>();
        List<BlockResource> list = neo4jUtil.getAllBlockResource();
        // 排序 按id大小
        list.sort(new Comparator<BlockResource>() {
            @Override
            public int compare(BlockResource r1, BlockResource r2) {
                return Long.compare(r1.getId(), r2.getId());
            }
        });

        for (BlockResource resource : list) {
            res.add(RdfUtil.getBlockResourceURI(resource));
        }

        return res;
    }

    public List<BlockResource> getAllBlockResources(HttpServletRequest httpServletRequest, final String productId)  {
        List<BlockResource> res = new ArrayList<>();
        List<BlockResource> list = neo4jUtil.getAllBlockResource();
        // 排序 按id大小
        list.sort(new Comparator<BlockResource>() {
            @Override
            public int compare(BlockResource r1, BlockResource r2) {
                return Long.compare(r1.getId(), r2.getId());
            }
        });

        for(BlockResource resource : list) {
            resource.setServiceProvider(ServiceProviderCatalogSingleton.getServiceProvider(httpServletRequest, productId).getAbout());
            resource.setAbout(URI.create("123"));
        }


        return list;
    }




    public BlockResource getResourceById(HttpServletRequest httpServletRequest, String productId, String id) {
        BlockResource resource =  getBlockResourceById(id).get(0);
        resource.setServiceProvider(ServiceProviderCatalogSingleton.getServiceProvider(httpServletRequest, productId).getAbout());
        resource.setAbout(getAboutURI(productId + "/Blocks/" + resource.getId()));
        return resource;
    }

    public String getRdfById(String id) throws IOException {
        return BlockResourceToRdf(getBlockResourceById(id));
    }


    public URI getAboutURI(final String text) {
        URI about;
        try {
            String basePath = OSLC4JUtils.getServletURI(); //basePath: http://localhost:8081/oslc/services/
            about =new URI(basePath + text);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return about;
    }


    public String getBlockResourceShape() throws IOException {
        Model model = RdfUtil.createRdfModel();
        Resource r = model.createResource(NsConstant.OSLC_BLOCK_SHAPE);
        r.addProperty(RDF.type, model.createResource(OslcConstants.TYPE_RESOURCE_SHAPE));
        Resource idProp = model.createResource()
                .addProperty(RDF.type, model.createResource(OslcConstants.TYPE_PROPERTY))
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN ,"range"),
                        model.createResource(NsConstant.BLOCK_NAMESPACE))
                .addProperty(DCTerms.title, model.createLiteral("id", true))
                .addProperty(DCTerms.description, model.createLiteral("id property shape.", true))
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "occurs"),
                        model.createResource(OslcConstants.OSLC_CORE_DOMAIN + "Exactly-one"))
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "valueType"),
                        model.createResource(OslcConstants.XML_NAMESPACE + "string"))
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "readOnly"),
                        model.createTypedLiteral(true))
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "propertyDefinition"),
                        model.createResource(NsConstant.BLOCK_ID))
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "name"),"id")
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "representation"),
                        model.createResource(OslcConstants.OSLC_CORE_DOMAIN + "Reference"));

        Resource nameProp = model.createResource()
                .addProperty(RDF.type, model.createResource(OslcConstants.TYPE_PROPERTY))
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN ,"range"),
                        model.createResource(NsConstant.BLOCK_NAMESPACE))
                .addProperty(DCTerms.title, model.createLiteral("name", true))   //1
                .addProperty(DCTerms.description, model.createLiteral("text property shape.", true))
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "occurs"),
                        model.createResource(OslcConstants.OSLC_CORE_DOMAIN + "Zero-or-one"))
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "valueType"),
                        model.createResource(OslcConstants.XML_NAMESPACE + "string"))
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "propertyDefinition"),
                        model.createResource(NsConstant.BLOCK_NAME)) // 2
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "name"),"name") // 3
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "representation"),
                        model.createResource(OslcConstants.OSLC_CORE_DOMAIN + "Reference"));

        Resource xmiIdProp = model.createResource()
                .addProperty(RDF.type, model.createResource(OslcConstants.TYPE_PROPERTY))
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN ,"range"),
                        model.createResource(NsConstant.BLOCK_NAMESPACE))
                .addProperty(DCTerms.title, model.createLiteral("xmi:id", true))   //1
                .addProperty(DCTerms.description, model.createLiteral("text property shape.", true))
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "occurs"),
                        model.createResource(OslcConstants.OSLC_CORE_DOMAIN + "Zero-or-one"))
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "valueType"),
                        model.createResource(OslcConstants.XML_NAMESPACE + "string"))
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "propertyDefinition"),
                        model.createResource(NsConstant.BLOCK_XMI_ID)) // 2
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "name"),"xmi:id") // 3
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "representation"),
                        model.createResource(OslcConstants.OSLC_CORE_DOMAIN + "Reference"));

        Resource xmiTypeProp = model.createResource()
                .addProperty(RDF.type, model.createResource(OslcConstants.TYPE_PROPERTY))
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN ,"range"),
                        model.createResource(NsConstant.BLOCK_NAMESPACE))
                .addProperty(DCTerms.title, model.createLiteral("xmi:type", true))   //1
                .addProperty(DCTerms.description, model.createLiteral("text property shape.", true))
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "occurs"),
                        model.createResource(OslcConstants.OSLC_CORE_DOMAIN + "Zero-or-one"))
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "valueType"),
                        model.createResource(OslcConstants.XML_NAMESPACE + "string"))
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "propertyDefinition"),
                        model.createResource(NsConstant.BLOCK_XMI_TYPE)) // 2
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "name"),"xmi:type") // 3
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "representation"),
                        model.createResource(OslcConstants.OSLC_CORE_DOMAIN + "Reference"));

        Resource visibilityProp = model.createResource()
                .addProperty(RDF.type, model.createResource(OslcConstants.TYPE_PROPERTY))
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN ,"range"),
                        model.createResource(NsConstant.BLOCK_NAMESPACE))
                .addProperty(DCTerms.title, model.createLiteral("visibility", true))   //1
                .addProperty(DCTerms.description, model.createLiteral("text property shape.", true))
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "occurs"),
                        model.createResource(OslcConstants.OSLC_CORE_DOMAIN + "Zero-or-one"))
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "valueType"),
                        model.createResource(OslcConstants.XML_NAMESPACE + "string"))
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "propertyDefinition"),
                        model.createResource(NsConstant.BLOCK_VISIBILITY)) // 2
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "name"),"visibility") // 3
                .addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "representation"),
                        model.createResource(OslcConstants.OSLC_CORE_DOMAIN + "Reference"));

        r.addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "property"), idProp);
        r.addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "property"), nameProp);
        r.addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "property"), xmiIdProp);
        r.addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "property"), xmiTypeProp);
        r.addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "property"), visibilityProp);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        model.write(out, "RDF/XML-ABBREV");
        return out.toString("UTF-8");
    }

}
