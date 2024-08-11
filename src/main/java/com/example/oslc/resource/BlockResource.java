package com.example.oslc.resource;
import com.example.oslc.constant.NsConstant;
import org.eclipse.lyo.oslc4j.core.annotation.*;
import org.eclipse.lyo.oslc4j.core.model.*;

import java.net.URI;

@OslcNamespace(NsConstant.oslc_neo4j_namespace)
@OslcName("Block")
@OslcResourceShape(title = "Block Resource Shape", describes = NsConstant.BLOCK_NAMESPACE)
public class BlockResource extends AbstractResource {
    private Long id;

    private String name;

    private String XmiType;

    private String XmiId;

    private String visibility;

    private URI serviceProvider;

    public BlockResource(){
        super();
    }

    public BlockResource(Long id, String name, String XmiType, String XmiId, String visibility) {
        super();
        this.id = id;
        this.XmiType = XmiType;
        this.XmiId = XmiId;
        this.visibility = visibility;
    }

    @OslcPropertyDefinition(NsConstant.BLOCK_ID)
    @OslcDescription("id property shape.")
    @OslcOccurs(Occurs.ExactlyOne)
    @OslcTitle("id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OslcPropertyDefinition(NsConstant.BLOCK_NAME)
    @OslcDescription("name property shape.")
    @OslcOccurs(Occurs.ExactlyOne)
    @OslcTitle("name")
    public String getName() {
        return name;
    }

    public void  setName(String name) {
        this.name = name;
    }

    @OslcPropertyDefinition(NsConstant.BLOCK_XMI_TYPE)
    @OslcDescription("xmiType property shape.")
    @OslcOccurs(Occurs.ZeroOrOne)
    @OslcName("XmiType")
    @OslcTitle("XmiType")
    public String getXmiType() {
        return XmiType;
    }

    public void setXmiType(String xmiType) {
        XmiType = xmiType;
    }

    @OslcPropertyDefinition(NsConstant.BLOCK_XMI_ID)
    @OslcDescription("xmiId property shape.")
    @OslcOccurs(Occurs.ZeroOrOne)
    @OslcReadOnly
    @OslcName("XmiId")
    @OslcTitle("XmiId")
    public String getXmiId() {
        return XmiId;
    }

    public void setXmiId(String xmiId) {
        XmiId = xmiId;
    }

    @OslcPropertyDefinition(NsConstant.BLOCK_VISIBILITY)
    @OslcDescription("visibility property shape.")
    @OslcOccurs(Occurs.ExactlyOne)
    @OslcTitle("visibility")
    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }


    @OslcDescription("The scope of a resource is a URI for the resource's OSLC Service Provider.")
    @OslcPropertyDefinition(OslcConstants.OSLC_CORE_NAMESPACE + "serviceProvider")
    @OslcRange(OslcConstants.TYPE_SERVICE_PROVIDER)
    @OslcTitle("Service Provider")
    public URI getServiceProvider()
    {
        return serviceProvider;
    }
    public void setServiceProvider(final URI serviceProvider)
    {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public String toString() {
        return "[" + this.id + "," + this.name + "," + this.XmiId + "," + this.XmiType + "," + this.visibility + "]";
    }

}
