package com.myretail.product.dao;

import com.myretail.product.domain.ProductDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductDetailRepository extends CrudRepository<ProductDetail, Integer> {
}
