package com.example.oslc.service;

import com.example.oslc.constant.NsConstant;
import com.example.oslc.resource.BlockResource;
import com.example.oslc.servlet.ServiceProviderCatalogSingleton;
import com.example.oslc.util.RdfUtil;
import com.example.oslc.util.neo4jUtil;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            resource.setAbout(getAboutURI(productId + "/Blocks/" + resource.getId()));
        }


        return list;
    }

    public BlockResource queryResourceById(String oslcWhere,HttpServletRequest httpServletRequest, String productId) throws Exception {
        // 正则表达式，匹配 oslc_ex:id= 后面的值
        String regex = "oslc_neo:id=([^\\\\s&]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(oslcWhere);

        // 查找匹配项，提取匹配到的ID值
        String id = matcher.find()? matcher.group(1):null;
        if(id == null) return null;

        BlockResource e = getResourceById(httpServletRequest,productId,id);

//        Model model = RdfUtil.createRdfModel();

//        Resource container = model.createResource(NsConstant.BASE_URI + productId + "/Blocks");
//        Resource resource = model.createResource(NsConstant.BASE_URI + productId + "/Blocks/" + e.getId());
//
//        // 添加类型声明和属性
//        container.addProperty(RDF.type, model.createResource(NsConstant.LDP_NAMESPACE + "BasicContainer"));
//        container.addProperty(model.createProperty(NsConstant.LDP_NAMESPACE + "contains"), resource);
//
//        // 序列化RDF模型为RDF/XML字符串
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        model.write(out, "RDF/XML");
//        return out.toString();
        return e;
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


}
