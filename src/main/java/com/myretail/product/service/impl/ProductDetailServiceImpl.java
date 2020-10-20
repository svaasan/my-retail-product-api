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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {

    Logger logger = LoggerFactory.getLogger(ProductDetailServiceImpl.class);

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private RedskyClient redskyClient;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * This method will retrieve data from database and redsky rest service and make ProductDetailResponse.
     * @param id
     * @return ProductDetailResponse
     */
    @Override
    public ProductDetailResponse getProductDetail(Integer id) throws Exception{

        logger.info("Retrieving product details for {} ", id);
        Product product = null;

        CompletableFuture<ResponseEntity<RedskyProductResponse>> futureResponse = redskyClient.getProductById(id);
        ProductDetail productDetail = productDetailRepository.findById(id).orElse(null);
        ResponseEntity<RedskyProductResponse> responseEntity = futureResponse.get();
        if (responseEntity != null && responseEntity.hasBody()) {
            product = responseEntity.getBody().getProduct();
        }
        return constructProductDetailResponse(id, productDetail, product);
    }

    @Override
    public ProductDetailResponse updateProductDetail(Integer id, ProductDetailRequest productDetailRequest) throws Exception {
        logger.info("Saving the product details for {}", id);

        ProductDetailResponse productDetailResponse = null;
        ProductDetail productDetail = new ProductDetail();
        productDetail.setId(productDetailRequest.getId());
        try {
            productDetail.setCurrentPrice(objectMapper.writeValueAsString(productDetailRequest.getProductPrice()));
        } catch (JsonProcessingException ex) {
            logger.error("Error  parsing the current_price", ex);
            throw new Exception("Error  parsing the current_price");
        }
        ProductDetail savedProductDetail = productDetailRepository.save(productDetail);

        return constructProductDetailResponse(id, savedProductDetail, null);
    }

    private ProductDetailResponse constructProductDetailResponse(Integer id, ProductDetail productDetail, Product product) throws Exception {
        logger.info("constructing the ProductDetailResponse for {}", id);
        ProductDetailResponse productDetailResponse = null;

        if(productDetail != null) {
            productDetailResponse = new ProductDetailResponse();
            productDetailResponse.setId(id);
            try {
             productDetailResponse.setProductPrice(objectMapper.readValue(productDetail.getCurrentPrice(), ProductPrice.class));
            } catch (JsonProcessingException ex) {
                logger.error("Error  parsing the current_price", ex);
                throw new Exception("Error  parsing the current_price");
            }
        } else {
            logger.info("Price information not found, id={}", id);
        }

        if(product != null) {
            if(productDetailResponse == null) {
                productDetailResponse = new ProductDetailResponse();
                productDetailResponse.setId(id);
            }
            if(product.getItem() != null && product.getItem().getProductDescription() != null) {
                productDetailResponse.setName(product.getItem().getProductDescription().getTitle());
            }
        } else {
            logger.info("Product not found in redsky, id={}", id);
        }
        return productDetailResponse;
    }
}
