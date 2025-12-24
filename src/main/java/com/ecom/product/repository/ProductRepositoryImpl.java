package com.ecom.product.repository;

import com.ecom.product.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<Product> getFilteredProducts(Double min, Double max, Pageable pageable, String category, String searchBy) {

        Query query = new Query().with(pageable);
        final List<Criteria> criteriaList = new ArrayList<>();

        if(null != searchBy && !searchBy.isBlank()) {
            criteriaList.add(Criteria.where("name").is(searchBy));
        }

        if(min > 0 || max < Double.MAX_VALUE) {
            criteriaList.add(Criteria.where("price").gte(min).lte(max));
        }

        if(null != category && !category.isBlank()){
            criteriaList.add(Criteria.where("categoryId").is(category));
        }

        if(!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        return PageableExecutionUtils.getPage(
                mongoTemplate.find(query,Product.class),
                pageable,
                () -> mongoTemplate.count(query.skip(0).limit(0), Product.class)
        );
    }
}
