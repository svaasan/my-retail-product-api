package com.myretail.product.controller

import com.myretail.product.domain.ProductDetailRequest
import com.myretail.product.domain.ProductDetailResponse
import com.myretail.product.domain.ProductPrice
import com.myretail.product.service.ProductDetailService
import spock.lang.Specification

import javax.servlet.http.HttpServletResponse

class ProductDetailControllerSpec extends Specification {
    ProductDetailService productDetailService = Mock(ProductDetailService)

    ProductDetailController productDetailController = new ProductDetailController(productDetailService: productDetailService)
    HttpServletResponse httpServletResponse = Mock(HttpServletResponse)

    def "Valid get request"(){
        given:
        ProductDetailResponse expected = new ProductDetailResponse(id: 1234)

        when:
        ProductDetailResponse actual = productDetailController.getProductDetails(1234, httpServletResponse)

        then:
        1 * productDetailService.getProductDetail(1234) >> expected
        0 * _

        expected == actual
    }

    def "Valid get request but no data found"(){
        given:

        when:
        productDetailController.getProductDetails(1234, httpServletResponse)

        then:
        1 * productDetailService.getProductDetail(1234) >> null
        1 * httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND)
        0 * _
    }

    def "Valid put request - update product"(){
        given:
        ProductPrice productPrice = new ProductPrice()
        productPrice.value = 9.49
        productPrice.currencyCode = "USD"
        ProductDetailRequest productDetailRequest = new ProductDetailRequest(id: 1234, productPrice: productPrice)
        ProductDetailResponse expected = new ProductDetailResponse(id: 1234, productPrice: productPrice)

        when:
        ProductDetailResponse actual = productDetailController.putProductDetails(1234,productDetailRequest, httpServletResponse)

        then:
        1 * productDetailService.updateProductDetail(1234, productDetailRequest) >> expected
        1 * httpServletResponse.setStatus(HttpServletResponse.SC_OK)
        0 * _
        actual == expected
    }

    def "Valid put request - Validation failure"(){
        given:
        ProductPrice productPrice = new ProductPrice()
        productPrice.value = 9.49
        productPrice.currencyCode = "USD"
        ProductDetailRequest productDetailRequest = new ProductDetailRequest(id: 1234, productPrice: productPrice)

        when:
        ProductDetailResponse actual = productDetailController.putProductDetails(123, productDetailRequest, httpServletResponse)

        then:
        1 * httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST)
        0 * _
        null == actual
    }
}
