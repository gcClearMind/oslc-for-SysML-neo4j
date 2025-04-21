package com.example.oslc.resource;

import org.eclipse.lyo.oslc4j.core.annotation.*;
import org.eclipse.lyo.oslc4j.core.model.ValueType;

@OslcNamespace("http://example.com/ns#")  // 定义命名空间
@OslcResourceShape(title = "Property Resource Shape")  // 标记为资源
public class Property {
    private String key;
    private Object value;

    // 必须有 getter/setter
    @OslcPropertyDefinition("http://example.com/ns#key")
    @OslcValueType(ValueType.String)  // 明确类型
    @OslcTitle("key")
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    @OslcPropertyDefinition("http://example.com/ns#key")
    @OslcValueType(ValueType.XMLLiteral)  // 明确类型
    @OslcTitle("value")
    public Object getValue() { return value; }
    public void setValue(Object value) { this.value = value; }
}