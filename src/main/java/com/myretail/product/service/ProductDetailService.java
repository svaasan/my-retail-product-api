package com.myretail.product.service;

import com.myretail.product.domain.ProductDetailRequest;
import com.myretail.product.domain.ProductDetailResponse;

public interface ProductDetailService {
    ProductDetailResponse getProductDetail(Integer id) throws Exception;
    ProductDetailResponse updateProductDetail(Integer id, ProductDetailRequest productDetailRequest) throws Exception;
}
