package com.ecom.product.dto;

import lombok.Builder;
import lombok.Data;
import org.apache.catalina.util.StringUtil;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.util.StringUtils;

@Data
public class FilterProductsRequest {
    Double min = Double.MIN_VALUE;
    Double max = Double.MAX_VALUE;
    Integer pageNumber = 0;
    Integer pageSize = 10;
    String searchBy = "";
    String category = "";
    String sortBy = "";
    Boolean ascending = Boolean.TRUE;
}
