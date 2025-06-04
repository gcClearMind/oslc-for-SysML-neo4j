# OSLC for SysML Neo4j

## 项目概述

`oslc-for-SysML-neo4j` 项目致力于将 OSLC（开放服务生命周期协作）标准与 Neo4j 图数据库相结合，以管理和协作 SysML（系统建模语言）相关的数据。当前核心资源为 `NodeResource`，`BlockResource` 基本弃用。项目基于 Spring Boot 构建，提供了资源管理、服务提供者管理、RDF 数据处理等功能，并配备了用户界面。

## 项目结构

```plaintext
oslc-for-SysML-neo4j/
├── .idea/
│   ├── .gitignore
│   ├── encodings.xml
│   └── misc.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   └── example/
│   │   │   │       └── oslc/
│   │   │   │           ├── contoller/
│   │   │   │           │   └── BlockController.java
│   │   │   │           ├── resource/
│   │   │   │           │   └── BlockResource.java
│   │   │   │           ├── service/
│   │   │   │           │   ├── BlockService.java
│   │   │   │           │   └── NodeService.java
│   │   │   │           ├── servlet/
│   │   │   │           │   └── RestDelegate.java
│   │   │   │           └── util/
│   │   │   │               ├── JsonUtil.java
│   │   │   │               └── RdfUtil.java
│   │   │   └── Test.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/
│   │       │   │   ├── catalog.css
│   │       │   │   ├── createResource.css
│   │       │   │   ├── provider.css
│   │       │   │   └── queryResource.css
│   │       │   ├── js/
│   │       │   │   ├── preview.js
│   │       │   │   └── queryResource.js
│   │       │   └── index.html
│   │       └── templates/
│   │           ├── createResource.html
│   │           ├── resourceCreated.html
│   │           ├── selectResource.html
│   │           ├── smallPreview.html
│   │           ├── ServiceProvider.html
│   │           └── test.html
├── .gitignore
└── pom.xml
```

## 主要依赖

- **Spring Data Neo4j**：用于与 Neo4j 图数据库进行交互。
- **Spring Boot Starter Thymeleaf**：Thymeleaf 是一个现代的服务器端 Java 模板引擎，用于构建 Web 应用的视图层。
- **Spring Boot Starter Jersey**：Jersey 是 JAX - RS（Java API for RESTful Web Services）的参考实现，用于构建 RESTful Web 服务。

## 主要功能模块

### 资源管理

- **NodeResource**：核心资源类，定义了节点资源的结构和属性，如 `identity`、`elementId`、`labels` 和 `properties` 等。
- **NodeController**：处理与 `NodeResource` 相关的 HTTP 请求，包括获取所有资源、创建资源、查询资源等操作。支持分页查询和按 ID 查询。
- **NodeService**：负责处理与 `NodeResource` 相关的业务逻辑，如将资源列表转换为 RDF 格式，便于与其他支持 RDF 的系统进行交互。

```java
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
        model.write(os, "RDF/XML-ABBREV");
        os.close();
        return os.toString();
    }
    // 其他方法...
}
```

### 服务提供者管理

- **RestDelegate**：提供服务提供者目录和服务提供者的管理功能，支持选择和查询服务提供者。同时，还实现了需求和需求集合的管理，包括创建、查询和索引等操作。

```java
public class RestDelegate {
    private static final Logger log = LoggerFactory.getLogger(RestDelegate.class);
    @Inject ResourcesFactory resourcesFactory;
    private final static Map<String, Map<String, Requirement>> requirements = new HashMap<>();
    private final static Map<String, Map<String, RequirementCollection>> requirementCollections = new HashMap<>();
    private final static Directory searchIndex = new ByteBuffersDirectory();
    private static final StandardAnalyzer indexAnalyser = new StandardAnalyzer();
    private static final Executor indexer = Executors.newSingleThreadExecutor();
    // 其他方法...
}
```

### RDF 数据处理

- **RdfUtil**：提供创建 RDF 模型和将资源形状转换为 RDF 格式的方法。为自定义命名空间设置前缀，方便在 RDF 模型中使用。

