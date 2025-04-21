package com.example.oslc.service;

import com.example.oslc.constant.NsConstant;
import com.example.oslc.resource.NodeResource;
import com.example.oslc.servlet.ServiceProviderCatalogSingleton;
import com.example.oslc.util.RdfUtil;
import com.example.oslc.util.neo4jUtil;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.oslc.util.neo4jUtil.getNodeResourceById;

@Service
public class NodeService {
    public String NodeResourceToRdf(List<NodeResource> resources) throws IOException {
        Model model = RdfUtil.createRdfModel();
        for(NodeResource resource : resources) {
            Resource rdfResource = model.createResource(NsConstant.node_path + resource.getIdentity());
            rdfResource.addProperty(RDF.type, model.createResource(NsConstant.NODE_NAMESPACE));
            rdfResource.addProperty(model.createProperty(NsConstant.NODE_IDENTITY), resource.getIdentity().toString());
            rdfResource.addProperty(model.createProperty(NsConstant.NODE_LABELS), resource.getLabels().toString());
            rdfResource.addProperty(model.createProperty(NsConstant.NODE_ELEMENTID), resource.getElementId());
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        // RDF/XML-ABBREV 格式输出 RDF，这种格式会将 rdf:type 声明为属性
        // 标准的 RDF/XML 格式输出 RDF，这种格式会将 rdf:type 声明为子元素
        model.write(os, "RDF/XML-ABBREV");
        os.close();
        return os.toString();
    }



//    public List<String> getAllResourceURIs()  {
//        List<String> res = new ArrayList<>();
//        List<NodeResource> list = neo4jUtil.getAllNodeResource();
//        // 排序 按id大小
//        list.sort(new Comparator<NodeResource>() {
//            @Override
//            public int compare(NodeResource r1, NodeResource r2) {
//                return Long.compare(r1.getIdentity(), r2.getIdentity());
//            }
//        });
//
//        for (NodeResource resource : list) {
//            res.add(RdfUtil.getNodeResourceURI(resource));
//        }
//
//        return res;
//    }

    public List<NodeResource> getAllNodeResources(HttpServletRequest httpServletRequest, final String productId)  {
        List<NodeResource> res = new ArrayList<>();
        List<NodeResource> list = neo4jUtil.getAllNodeResource();
        // 排序 按id大小
//        list.sort(new Comparator<NodeResource>() {
//            @Override
//            public int compare(NodeResource r1, NodeResource r2) {
//                return Long.compare(r1.getIdentity(), r2.getIdentity());
//            }
//        });

        for(NodeResource resource : list) {
            resource.setServiceProvider(ServiceProviderCatalogSingleton.getServiceProvider(httpServletRequest, productId).getAbout());
            resource.setAbout(getAboutURI(productId + "/Nodes/" + resource.getIdentity()));
        }


        return list;
    }

    public NodeResource queryResourceById(String oslcWhere,HttpServletRequest httpServletRequest, String productId) throws Exception {
        // 正则表达式，匹配 oslc_ex:id= 后面的值
        String regex = "oslc_neo:id=([^\\\\s&]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(oslcWhere);

        // 查找匹配项，提取匹配到的ID值
        String id = matcher.find()? matcher.group(1):null;
        if(id == null) return null;

        NodeResource e = getResourceById(httpServletRequest,productId,id);

//        Model model = RdfUtil.createRdfModel();

//        Resource container = model.createResource(NsConstant.BASE_URI + productId + "/Nodes");
//        Resource resource = model.createResource(NsConstant.BASE_URI + productId + "/Nodes/" + e.getId());
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


    public NodeResource getResourceById(HttpServletRequest httpServletRequest, String productId, String id) {
        NodeResource resource =  getNodeResourceById(id).get(0);
        resource.setServiceProvider(ServiceProviderCatalogSingleton.getServiceProvider(httpServletRequest, productId).getAbout());
        resource.setAbout(getAboutURI(productId + "/Nodes/" + resource.getIdentity()));
        return resource;
    }

    public String getRdfById(String id) throws IOException {
        return NodeResourceToRdf(getNodeResourceById(id));
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
