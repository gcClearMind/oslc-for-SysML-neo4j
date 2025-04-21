package com.example.oslc.constant;

public class NsConstant {
    //------------------------------PATH------------------------------
    public final static String data_path = "localhost:8080/neo4j/";
    public final static String block_path = data_path + "block/";

    public final static String node_path = data_path + "node/";

    public final static String BASE_URI = "http://localhost:8081/oslc/services/";

    //------------------------------prefix / namespace------------------------------

    public final static String oslc_neo4j_prefix = "oslc_neo";
    public final static String oslc_neo4j_namespace = "localhost:8080/neo4j/neo4j-vocab#";
    public final static String OSLC_BLOCK_SHAPE= "http://localhost:8081/BLOCK/ResourceShape";
    public final static String LDP_PREFIX="ldp";
    public final static String LDP_NAMESPACE="http://www.w3.org/ns/ldp#";




    //------------------------------CLASS------------------------------
    public final static String BLOCK_NAMESPACE = "localhost:8080/neo4j/neo4j-vocab#Block";

    public final static String NODE_NAMESPACE = "localhost:8080/neo4j/neo4j-vocab#Node";
    public final static String PATH_BLOCK = "Block";

    public final static String PATH_Node = "Node";

    //----------------------------Property------------------------------
    public final static String BLOCK_ID = oslc_neo4j_namespace + "id";
    public final static String BLOCK_NAME = oslc_neo4j_namespace + "name";
    public final static String BLOCK_XMI_TYPE = oslc_neo4j_namespace + "XmiType";
    public final static String BLOCK_XMI_ID = oslc_neo4j_namespace + "XmiId";
    public final static String BLOCK_VISIBILITY = oslc_neo4j_namespace + "visibility";
    public final static String oslcID = oslc_neo4j_prefix + ":id";
    public final static String oslc_NAME = oslc_neo4j_prefix + ":name";
    public final static String oslc_XMI_TYPE = oslc_neo4j_prefix + ":XmiType";
    public final static String oslc_XMI_ID = oslc_neo4j_prefix + ":XmiId";
    public final static String oslc_VISIBILITY = oslc_neo4j_prefix + ":visibility";


    public final static String NODE_IDENTITY = oslc_neo4j_namespace + "identity";
    public final static String NODE_LABELS = oslc_neo4j_namespace + "labels";
    public final static String NODE_PROPERTIES = oslc_neo4j_namespace + "properties";
    public final static String NODE_ELEMENTID= oslc_neo4j_namespace + "elementId";
    public final static String NODE_KEYS = oslc_neo4j_namespace + "keys";
    public final static String NODE_VALUES = oslc_neo4j_namespace + "values";
    //-------------------------------ServerConstants------------------------------------

    public static final String HDR_OSLC_VERSION = "OSLC-Core-Version";
    public static final String OSLC_VERSION_V2 = "2.0";

}
