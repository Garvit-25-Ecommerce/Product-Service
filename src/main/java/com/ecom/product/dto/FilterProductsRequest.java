package com.ecom.product.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
