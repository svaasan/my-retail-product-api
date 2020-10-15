package com.myretail.product.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myretail.product.config.RedskyClient;
import com.myretail.product.dao.ProductDetailRepository;
import com.myretail.product.domain.*;
import com.myretail.product.service.ProductDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {

    Logger logger = LoggerFactory.getLogger(ProductDetailServiceImpl.class);

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private RedskyClient redskyClient;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * This method will retrieve data from database and redsky rest service and make ProductDetailResponse.
     * @param id
     * @return ProductDetailResponse
     */
    @Override
    public ProductDetailResponse getProductDetail(Integer id) {

        logger.info("Retrieving product details for {} ", id);
        Product product = redskyClient.getProductById(id);
        ProductDetail productDetail = productDetailRepository.findById(id).orElse(null);

        return constructProductDetailResponse(id, productDetail, product, false);
    }

    @Override
    public ProductDetailResponse updateProductDetail(Integer id, ProductDetailRequest productDetailRequest) throws Exception {
        logger.info("Saving the product details for {}", id);
        ProductDetailResponse productDetailResponse = null;
        boolean created = false;
        if(productDetailRepository.findById(id).orElse(null) == null){
            created = true;
        }

        ProductDetail productDetail = new ProductDetail();
        productDetail.setId(productDetailRequest.getId());
        productDetail.setCurrentPrice(objectMapper.writeValueAsString(productDetailRequest.getProductPrice()));

        ProductDetail savedProductDetail = productDetailRepository.save(productDetail);

        return constructProductDetailResponse(id, savedProductDetail, null, created);
    }

    private ProductDetailResponse constructProductDetailResponse(Integer id, ProductDetail productDetail, Product product, boolean created) {
        ProductDetailResponse productDetailResponse = null;

        if(productDetail != null){
            productDetailResponse = new ProductDetailResponse();
            productDetailResponse.setId(id);
            try {
             productDetailResponse.setProductPrice(objectMapper.readValue(productDetail.getCurrentPrice(), ProductPrice.class));
            } catch (JsonProcessingException e) {
                logger.error("Error Occurred during parsing", e);
            }
        } else {
            logger.info("Price information not found, id={}", id);
        }

        if(product != null){
            if(productDetailResponse == null){
                productDetailResponse = new ProductDetailResponse();
                productDetailResponse.setId(id);
            }
            try{
                productDetailResponse.setName(product.getItem().getProductDescription().getTitle());
            } catch (NullPointerException ex){
                logger.warn("Product endpoint does not have name, id={}", id);
            }
        }else {
            logger.info("Product not found in redsky, id={}", id);
        }

        productDetailResponse.setCreated(created);
        return productDetailResponse;
    }
}
