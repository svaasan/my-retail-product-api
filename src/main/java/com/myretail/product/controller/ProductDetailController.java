package com.myretail.product.controller;

import com.myretail.product.domain.ProductDetailRequest;
import com.myretail.product.domain.ProductDetailResponse;
import com.myretail.product.service.ProductDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
public class ProductDetailController {
    private static final Logger log = LoggerFactory.getLogger(ProductDetailController.class);

    @Autowired
    ProductDetailService productDetailService;

    /**
     * This GET endpoint will serve product detail for given id.
     *
     * <b>Note: </b>The response may have one of following.
     * <p>
     *    <ul>
     *       <li>ProductDetail data with status code 200 in case of successful retrieval.</li>
     *       <li>null value with status code of 500 in case of any exception.</li>
     *       <li>null value with status code of 400 in case of invalid input.</li>
     *    </ul>
     * </p>
     *
     * @param id       is a product id for which the product details has to be retrieved.
     * @param response is used to send http status codes based on data retrieval operation status.
     * @return ProductDetailResponse for given id.
     */
    @GetMapping(path = "/products/{id}", produces = "application/json")
    ProductDetailResponse getProductDetails(@PathVariable Integer id,
                                            HttpServletResponse response) throws Exception {
        log.info("Incoming get request, id={}", id);

        ProductDetailResponse productDetailResponse = productDetailService.getProductDetail(id);
        if (productDetailResponse == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        return productDetailResponse;
    }

    /**
     * This PUT endpoint will update the product detail.
     *
     * <b>Note: </b>The response may have one of following.
     * <p>
     *    <ul>
     *       <li>ProductDetail data with status code 200 in case of successful retrieval.</li>
     *       <li>null value with status code of 500 in case of any exception.</li>
     *       <li>null value with status code of 400 in case of invalid input.</li>
     *    </ul>
     * </p>
     *
     * @param id                   is a product id for which the product details has to be updated.
     * @param productDetailRequest is the request body which has to be updated.
     * @param response             is used to send http status codes based on data retrieval operation status.
     * @return
     */
    @PutMapping(path = "/products/{id}", consumes = "application/json")
    ProductDetailResponse putProductDetails(@PathVariable Integer id,
                                            @Valid @RequestBody ProductDetailRequest productDetailRequest,
                                            HttpServletResponse response) throws Exception {
        log.info("Incoming put request, id={}", id);

        if (productDetailRequest == null || productDetailRequest.getId() != id) {
            log.info("Invalid data, id={}", id);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        ProductDetailResponse productDetailResponse = productDetailService.updateProductDetail(id, productDetailRequest);
        if (productDetailResponse != null) {
            if (productDetailResponse.isCreated()) {
                response.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        log.info("Updated product detail for id={}", id);
        return productDetailResponse;
    }
}
