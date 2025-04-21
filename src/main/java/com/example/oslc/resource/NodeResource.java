package com.example.oslc.resource;

import com.example.oslc.constant.NsConstant;
import com.example.oslc.util.JsonUtil;
import org.eclipse.lyo.oslc4j.core.annotation.*;
import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import org.eclipse.lyo.oslc4j.core.model.Occurs;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.ValueType;

import java.net.URI;
import java.util.*;

@OslcNamespace(NsConstant.oslc_neo4j_namespace)
@OslcName("Node")
@OslcResourceShape(title = "Node Resource Shape", describes = NsConstant.NODE_NAMESPACE)
public class NodeResource extends AbstractResource {
    private Long identity;
    private String elementId;
    private List<String> labels;
    private Map<String, Object> properties;
    private Set<String> keys;
    private List<String> values;
    private URI serviceProvider;

    public NodeResource(){super();}

    public NodeResource(Long identity, String elementId, List<String> labels, Map<String, Object> properties) {
        super();
        this.identity = identity;
        this.elementId = elementId;
        this.labels = labels;
        this.properties = properties;
        this.keys =  this.properties.keySet();
        this.values = List.of(Arrays.toString(this.properties.values().toArray()));;
    }




    @OslcPropertyDefinition(NsConstant.NODE_IDENTITY)
    @OslcDescription("identity property shape.")
    @OslcOccurs(Occurs.ExactlyOne)
    @OslcTitle("identity")
    public Long getIdentity() {
        return identity;
    }

    public void setIdentity(Long identity) {
        this.identity = identity;
    }

    @OslcPropertyDefinition(NsConstant.NODE_ELEMENTID)
    @OslcDescription("elementId property shape.")
    @OslcOccurs(Occurs.ExactlyOne)
    @OslcTitle("elementId")
    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    @OslcPropertyDefinition(NsConstant.NODE_LABELS)
    @OslcDescription("labels property shape.")
    @OslcTitle("labels")
    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }


    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
//    @OslcPropertyDefinition(NsConstant.NODE_PROPERTIES)
//    @OslcDescription("properties property shape.")
//    @OslcTitle("properties")
//    @OslcRange("http://example.com/ns#Property")
    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setKeys(Set<String> keys) {
        this.keys = keys;
    }

    @OslcPropertyDefinition(NsConstant.NODE_KEYS)
    @OslcDescription("keys property shape.")
    @OslcTitle("keys")
    public Set<String> getKeys() {
        if(keys == null || keys.isEmpty()) {
            return  properties.keySet();
        }
        return keys;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    @OslcPropertyDefinition(NsConstant.NODE_VALUES)
    @OslcDescription("values property shape.")
    @OslcTitle("values")
    public List<String> getValues() {
        if(values == null || values.isEmpty()) {
            return List.of(Arrays.toString(properties.values().toArray()));
        }
        return values;
    }


    @OslcDescription("The scope of a resource is a URI for the resource's OSLC Service Provider.")
    @OslcPropertyDefinition(OslcConstants.OSLC_CORE_NAMESPACE + "serviceProvider")
    @OslcRange(OslcConstants.TYPE_SERVICE_PROVIDER)
    @OslcTitle("Service Provider")
    public URI getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(URI serviceProvider) {
        this.serviceProvider = serviceProvider;
    }
}
