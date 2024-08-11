package com.example.oslc.util;

import com.example.oslc.constant.NsConstant;
import com.example.oslc.resource.BlockResource;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.eclipse.lyo.oslc4j.core.annotation.OslcName;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.Property;
import org.eclipse.lyo.oslc4j.core.model.ResourceShape;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;
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


    
    public static String resourceShapeToRdf(ResourceShape resourceShape) throws UnsupportedEncodingException {
        Model model = createRdfModel();

        Resource rootResource = model.createResource(resourceShape.getAbout().toString())
                .addProperty(RDF.type, model.createResource(OslcConstants.OSLC_CORE_DOMAIN + "ResourceShape"));//<oslc:ResourceShape>
        for(Property property: resourceShape.getProperties()) {
            Resource rdfProperty = model.createResource()
                    .addProperty(RDF.type, model.createResource(OslcConstants.TYPE_PROPERTY));//<oslc:Property>
            if (property.getTitle() != null) {
                rdfProperty.addProperty(DCTerms.title, model.createLiteral(property.getTitle(), true));
                ////<dcterms:title rdf:parseType="Literal">xxx</dcterms:title>
            }

            if (property.getRange() != null && property.getRange().length != 0) {
                for (URI uri : property.getRange()) {
                    rdfProperty.addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN  + "range"),
                            model.createResource(uri.toString())); //<oslc:range rdf:resource=""/>
                }
            }

            if (property.getValueType() != null) {
                rdfProperty.addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN, "valueType"),
                        model.createResource(property.getValueType().toString()));
            }

            if (property.getName() != null) {
                rdfProperty.addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN  + "name"), property.getName());
            }

            if (property.getRepresentation() != null) {
                rdfProperty.addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN  + "representation"),
                        model.createResource(property.getRepresentation().toString()));
            }

            if (property.getDescription() != null) {
                rdfProperty.addProperty(DCTerms.description, model.createLiteral(property.getDescription(), true));
            }

            if (property.isReadOnly() != null) {
                rdfProperty.addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN  + "readOnly"),
                        model.createTypedLiteral(property.isReadOnly()));
            }

            if (property.getPropertyDefinition() != null) {
                rdfProperty.addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN  + "propertyDefinition"),
                        model.createResource(property.getPropertyDefinition().toString()));
            }

            if (property.getOccurs() != null) {
                rdfProperty.addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN  + "occurs"),
                        model.createResource(property.getOccurs().toString()));
            }

            if (property.getValueShape() != null) {
                rdfProperty.addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN  + "valueShape"),
                        model.createResource(property.getValueShape().toString()));
            }

            if (property.getMaxSize() != null) {
                rdfProperty.addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN  + "maxSize"),
                        model.createTypedLiteral(property.getMaxSize()));
            }

            if (property.isHidden() != null) {
                rdfProperty.addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN  + "hidden"),
                        model.createTypedLiteral(property.isHidden()));
            }

            if (property.isMemberProperty() != null) {
                rdfProperty.addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN  + "memberProperty"),
                        model.createTypedLiteral(property.isMemberProperty()));
            }

            if (property.getAllowedValuesRef() != null) {
                rdfProperty.addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN  + "allowedValuesRef"),
                        model.createResource(property.getAllowedValuesRef().toString()));
            }

            rootResource.addProperty(model.createProperty(OslcConstants.OSLC_CORE_DOMAIN + "property"), rdfProperty);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        model.write(out, "RDF/XML-ABBREV");
        return out.toString("UTF-8");
    }
}
