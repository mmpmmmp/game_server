package com.wre.game.api.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProductList implements Serializable {
    private static final long serialVersionUID = 1762796227337192313L;
    private Map<String, Map<String, Product>> productListMap = new HashMap<>();

    public Map<String, Map<String, Product>> getProductListMap() {
        return productListMap;
    }

    public void setProductListMap(Map<String, Map<String, Product>> productListMap) {
        this.productListMap = productListMap;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProductList{");
        sb.append("productListMap=").append(productListMap);
        sb.append('}');
        return sb.toString();
    }
}
