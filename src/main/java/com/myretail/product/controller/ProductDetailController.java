package com.myretail.product.controller;

import com.myretail.product.domain.ProductDetailRequest;
import com.myretail.product.domain.ProductDetailResponse;
import com.myretail.product.service.ProductDetailService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
     * Retrieves the product details for the given product Id..
     *
     * <b>Note: </b>The response may have one of following.
     * <p>
     *    <ul>
     *        <li>ProductDetailResponse data with status code 200 in case of successful retrieval.</li>
     *        <li>null value with status code of 404 in case of data not found in database.</li>
     *        <li>null value with status code of 500 in case of any exception.</li>
     *
     *    </ul>
     * </p>
     *
     * @param id       is a product id for which the product details has to be retrieved.
     * @param response is used to send http status codes based on data retrieval operation status.
     * @return ProductDetailResponse for given id.
     */
    @GetMapping(path = "/products/{id}", produces = "application/json")
    @ApiOperation(value = "Retrieves the product details for the given product Id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "ProductDetail Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
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
     * Updates the detail for the given product.
     *
     * <b>Note: </b>The response may have one of following.
     * <p>
     *    <ul>
     *       <li>ProductDetailResponse data with status code 200 in case of successful retrieval.</li>
     *       <li>null value with status code of 400 in case of invalid input.</li>
     *       <li>null value with status code of 500 in case of any exception.</li>
     *    </ul>
     * </p>
     *
     * @param id                   is a product id for which the product details has to be updated.
     * @param productDetailRequest is the request body which has to be updated.
     * @param response             is used to send http status codes based on data retrieval operation status.
     * @return
     */
    @PutMapping(path = "/products/{id}", consumes = "application/json")
    @ApiOperation(value = "Updates the detail for the given product.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product Details updated Successfully"),
            @ApiResponse(code = 400, message = "Invalid Input"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    ProductDetailResponse putProductDetails(@PathVariable Integer id,
                                            @Valid @RequestBody ProductDetailRequest productDetailRequest,
                                            HttpServletResponse response) throws Exception {
        log.info("Incoming put request, id={}", id);

       if (productDetailRequest.getId() != id) {
            log.info("Invalid data, id={}", id);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        ProductDetailResponse productDetailResponse = productDetailService.updateProductDetail(id, productDetailRequest);
        if (productDetailResponse != null) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        log.info("Updated product detail for id={}", id);
        return productDetailResponse;
    }
}
