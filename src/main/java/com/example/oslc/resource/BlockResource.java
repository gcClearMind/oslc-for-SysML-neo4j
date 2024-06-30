package com.example.oslc.resource;
import com.example.oslc.constant.NsConstant;
import org.eclipse.lyo.oslc4j.core.annotation.*;
import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import org.eclipse.lyo.oslc4j.core.model.Occurs;
import org.eclipse.lyo.oslc4j.core.model.Representation;

@OslcNamespace(NsConstant.oslc_neo4j_namespace)
@OslcName("BLock")
@OslcResourceShape(title = "Block Resource Shape", describes = NsConstant.BLOCK_NAMESPACE)
public class BlockResource extends AbstractResource {
    private Long id;

    private String name;

    private String XmiType;

    private String XmiId;

    private String visibility;

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
    @OslcRepresentation(Representation.Inline)
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
    @OslcRepresentation(Representation.Inline)
    @OslcTitle("name")
    public String getName() {
        return name;
    }

    public void  setName(String name) {
        this.name = name;
    }

    @OslcPropertyDefinition(NsConstant.BLOCK_XMI_TYPE)
    @OslcDescription("xmi:type property shape.")
    @OslcOccurs(Occurs.ExactlyOne)
    @OslcRepresentation(Representation.Inline)
    @OslcTitle("XmiType")
    public String getXmiType() {
        return XmiType;
    }

    public void setXmiType(String xmiType) {
        XmiType = xmiType;
    }

    @OslcPropertyDefinition(NsConstant.BLOCK_XMI_ID)
    @OslcDescription("xmi:id property shape.")
    @OslcOccurs(Occurs.ExactlyOne)
    @OslcRepresentation(Representation.Inline)
    @OslcTitle("XmiID")
    public String getXmiId() {
        return XmiId;
    }

    public void setXmiId(String xmiId) {
        XmiId = xmiId;
    }

    @OslcPropertyDefinition(NsConstant.BLOCK_VISIBILITY)
    @OslcDescription("visibility property shape.")
    @OslcOccurs(Occurs.ExactlyOne)
    @OslcRepresentation(Representation.Inline)
    @OslcTitle("visibility")
    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    @Override
    public String toString() {
        return "[" + this.id + "," + this.name + "," + this.XmiId + "," + this.XmiType + "," + this.visibility + "]";
    }

}
