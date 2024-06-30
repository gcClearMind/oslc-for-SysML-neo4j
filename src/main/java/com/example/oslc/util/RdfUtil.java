package com.example.oslc.util;

import com.example.oslc.constant.NsConstant;
import com.example.oslc.resource.BlockResource;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;

import java.util.List;

public class RdfUtil {
    public static Model createRdfModel(){
        Model model = ModelFactory.createDefaultModel(); //创建jena RDF模型

        // 设置自定义的命名空间前缀
        model.setNsPrefix(NsConstant.oslc_neo4j_prefix, NsConstant.oslc_neo4j_namespace);
        model.setNsPrefix(OslcConstants.OSLC_CORE_NAMESPACE_PREFIX, OslcConstants.OSLC_CORE_DOMAIN);
        model.setNsPrefix(OslcConstants.DCTERMS_NAMESPACE_PREFIX, OslcConstants.DCTERMS_NAMESPACE);
        model.setNsPrefix(OslcConstants.RDFS_NAMESPACE_PREFIX, OslcConstants.RDFS_NAMESPACE);
        model.setNsPrefix(NsConstant.LDP_PREFIX, NsConstant.LDP_NAMESPACE);
        model.setNsPrefix(OslcConstants.RDF_NAMESPACE_PREFIX, OslcConstants.RDF_NAMESPACE);

        return model;
    }

    public static String getBlockResourceURI(BlockResource resource ){
        return NsConstant.block_path + resource.getId();
    }

}
