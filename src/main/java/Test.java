import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.XSD;

public class Test {
    public static void main(String[] args) {
        // 创建模型
        Model model = ModelFactory.createDefaultModel();

        // 定义命名空间
        String oslc = "http://open-services.net/ns/core#";
        String cm = "http://open-services.net/ns/cm#";
        String rm = "http://open-services.net/ns/rm#";
        String qm = "http://open-services.net/ns/qm#";
        String dcterms = "http://purl.org/dc/terms/";

        // 添加命名空间前缀
        model.setNsPrefix("oslc", oslc);
        model.setNsPrefix("cm", cm);
        model.setNsPrefix("rm", rm);
        model.setNsPrefix("qm", qm);
        model.setNsPrefix("dcterms", dcterms);

        // 创建资源形状
        Resource resourceShape = model.createResource("http://example.com/services/resourceShapes/myResource")
                .addProperty(RDF.type, model.createResource(oslc + "ResourceShape"));

        // 定义第一个属性 implementsRequirement
        Resource implementsRequirement = model.createResource("http://example.com/properties/implementsRequirement")
                .addProperty(model.createProperty(dcterms + "title"), "Implements Requirements")
                .addProperty(model.createProperty(oslc + "range"), model.createResource(rm + "Requirement"))
                .addProperty(model.createProperty(oslc + "valueType"), model.createResource(oslc + "Resource"))
                .addProperty(model.createProperty(oslc + "name"), "implementsRequirement")
                .addProperty(model.createProperty(oslc + "representation"), model.createResource(oslc + "Reference"))
                .addProperty(model.createProperty(dcterms + "description"), "Implements associated Requirement.")
                .addProperty(model.createProperty(oslc + "readOnly"), model.createTypedLiteral(false))
                .addProperty(model.createProperty(oslc + "propertyDefinition"), model.createResource(cm + "implementsRequirement"))
                .addProperty(model.createProperty(oslc + "occurs"), model.createResource(oslc + "ZeroOrMany"));

        // 将属性添加到资源形状
        resourceShape.addProperty(model.createProperty(oslc + "property"), implementsRequirement);

        // 定义第二个属性 status
        Resource status = model.createResource("http://example.com/properties/status")
                .addProperty(model.createProperty(dcterms + "title"), "Status")
                .addProperty(model.createProperty(oslc + "occurs"), model.createResource(oslc + "ZeroOrOne"))
                .addProperty(model.createProperty(oslc + "valueType"), XSD.xstring)
                .addProperty(model.createProperty(oslc + "propertyDefinition"), model.createResource(cm + "status"))
                .addProperty(model.createProperty(dcterms + "description"), "Used to indicate the status of the change request.")
                .addProperty(model.createProperty(oslc + "name"), "status");

        // 将属性添加到资源形状
        resourceShape.addProperty(model.createProperty(oslc + "property"), status);

        // 定义第三个属性 testedByTestCase
        Resource testedByTestCase = model.createResource("http://example.com/properties/testedByTestCase")
                .addProperty(model.createProperty(dcterms + "title"), "Tested by Test Cases")
                .addProperty(model.createProperty(oslc + "occurs"), model.createResource(oslc + "ZeroOrMany"))
                .addProperty(model.createProperty(oslc + "valueType"), model.createResource(oslc + "Resource"))
                .addProperty(model.createProperty(oslc + "representation"), model.createResource(oslc + "Reference"))
                .addProperty(model.createProperty(dcterms + "description"), "Test case by which this change request is tested.")
                .addProperty(model.createProperty(oslc + "readOnly"), model.createTypedLiteral(false))
                .addProperty(model.createProperty(oslc + "propertyDefinition"), model.createResource(cm + "testedByTestCase"))
                .addProperty(model.createProperty(oslc + "range"), model.createResource(qm + "TestCase"))
                .addProperty(model.createProperty(oslc + "name"), "testedByTestCase");

        // 将属性添加到资源形状
        resourceShape.addProperty(model.createProperty(oslc + "property"), testedByTestCase);

        // 定义第四个属性 verified
        Resource verified = model.createResource("http://example.com/properties/verified")
                .addProperty(model.createProperty(dcterms + "title"), "Verified")
                .addProperty(model.createProperty(oslc + "occurs"), model.createResource(oslc + "ZeroOrOne"))
                .addProperty(model.createProperty(oslc + "valueType"), XSD.xboolean)
                .addProperty(model.createProperty(oslc + "propertyDefinition"), model.createResource(cm + "verified"))
                .addProperty(model.createProperty(dcterms + "description"), "Whether or not the resolution or fix of the Change Request has been verified.")
                .addProperty(model.createProperty(oslc + "readOnly"), model.createTypedLiteral(true))
                .addProperty(model.createProperty(oslc + "name"), "verified");

        // 将属性添加到资源形状
        resourceShape.addProperty(model.createProperty(oslc + "property"), verified);

        // 序列化为RDF/XML格式并输出
        model.write(System.out, "RDF/XML-ABBREV");
    }
}