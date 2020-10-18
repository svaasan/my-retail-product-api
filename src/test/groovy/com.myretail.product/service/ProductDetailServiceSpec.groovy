package com.myretail.product.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.myretail.product.config.RedskyClient
import com.myretail.product.dao.ProductDetailRepository
import com.myretail.product.domain.*
import com.myretail.product.service.impl.ProductDetailServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

import java.util.concurrent.CompletableFuture

class ProductDetailServiceSpec extends Specification {

    ProductDetailRepository productDetailRepository = Mock(ProductDetailRepository)
    RedskyClient redskyClient = Mock(RedskyClient)
    ObjectMapper objectMapper = Mock(ObjectMapper)

    ProductDetailService productDetailService = new ProductDetailServiceImpl(
            productDetailRepository: productDetailRepository,
            redskyClient: redskyClient,
            objectMapper: objectMapper
    )

    def "All product details available"() {

        given:
        ProductPrice productPrice = new ProductPrice()
        productPrice.value = 9.49
        productPrice.currencyCode = "USD"
        ProductDetail productDetail = new ProductDetail(id: 1234, currentPrice: "{\"value\":9.49,\"currency_code\":\"USD\"}")
        Product product = new Product(item: new Item(productDescription: new ProductDescription(title: "Sample item Desc")))

        when:
        ProductDetailResponse result = productDetailService.getProductDetail(1234)

        then:
        1 * productDetailRepository.findById(1234) >> new Optional<ProductDetail>(productDetail)
        1 * redskyClient.getProductById(1234) >> new CompletableFuture<ResponseEntity<Product>>().completedFuture(new ResponseEntity<Product>(product, HttpStatus.OK))
        1 * objectMapper.readValue("{\"value\":9.49,\"currency_code\":\"USD\"}", ProductPrice.class) >> productPrice
        0 * _

        result.id == 1234
        result.productPrice.currencyCode == productPrice.currencyCode
        result.productPrice.value == productPrice.value
        result.name == "Sample item Desc"
    }

    def "Product price not available in database"() {
        given:
        Product product= new Product(item: new Item(productDescription: new ProductDescription(title: "Sample item Desc")))

        when:
        ProductDetailResponse result = productDetailService.getProductDetail(1234)

        then:
        1 * productDetailRepository.findById(1234) >> new Optional<ProductDetail>()
        1 * redskyClient.getProductById(1234) >> new CompletableFuture<ResponseEntity<Product>>().completedFuture(new ResponseEntity<Product>(product, HttpStatus.OK))
        0 * _

        result.id == 1234
        result.productPrice == null
        result.name == "Sample item Desc"
    }

    def "Product detail not available in redsky"() {

        given:
        ProductPrice productPrice = new ProductPrice()
        productPrice.value = 9.49
        productPrice.currencyCode = "USD"
        ProductDetail productDetail = new ProductDetail(id: 1234, currentPrice: "{\"value\":9.49,\"currency_code\":\"USD\"}")

        when:
        ProductDetailResponse result = productDetailService.getProductDetail(1234)

        then:
        1 * productDetailRepository.findById(1234) >> new Optional<ProductDetail>(productDetail)
        1 * redskyClient.getProductById(1234) >> new CompletableFuture<ResponseEntity<Product>>().completedFuture(new ResponseEntity<Product>(null, HttpStatus.NOT_FOUND))
        1 * objectMapper.readValue("{\"value\":9.49,\"currency_code\":\"USD\"}", ProductPrice.class) >> productPrice
        0 * _

        result.id == 1234
        result.productPrice.currencyCode == productPrice.currencyCode
        result.productPrice.value == productPrice.value
        result.name == null
    }

    def "getProductDetail - exception scenario"() {
        given:
        Product product= new Product(item: new Item(productDescription: new ProductDescription(title: "Sample item Desc")))

        when:
        productDetailService.getProductDetail(1234)

        then:
        1 * redskyClient.getProductById(1234) >> new CompletableFuture<ResponseEntity<Product>>().completedFuture(new ResponseEntity<Product>(product, HttpStatus.OK))
        1 * productDetailRepository.findById(1234) >> { throw new Exception("some exception") }
        0 * _

        thrown Exception
    }

    def "getProductDetail - Exception while parsing"() {

        given:
        ProductPrice productPrice = new ProductPrice()
        productPrice.value = 9.49
        productPrice.currencyCode = "USD"
        ProductDetail productDetail = new ProductDetail(id: 1234, currentPrice: "{\"value\":9.49,\"currency_code\":\"USD\"}")
        Product product= new Product(item: new Item(productDescription: new ProductDescription(title: "Sample item Desc")))

        when:
        ProductDetailResponse result = productDetailService.getProductDetail(1234)

        then:
        1 * productDetailRepository.findById(1234) >> new Optional<ProductDetail>(productDetail)
        1 * redskyClient.getProductById(1234) >> new CompletableFuture<ResponseEntity<Product>>().completedFuture(new ResponseEntity<Product>(product, HttpStatus.OK))
        1 * objectMapper.readValue("{\"value\":9.49,\"currency_code\":\"USD\"}", ProductPrice.class) >>  { throw new Exception("some exception") }
        0 * _

        thrown Exception
    }

    def "update product detail - update existing product detail"() {
        given:
        ProductPrice productPrice = new ProductPrice()
        productPrice.value = 4.99
        productPrice.currencyCode = "USD"
        ProductDetail productDetail = new ProductDetail(id: 1234, currentPrice: "{\"value\":4.49,\"currency_code\":\"USD\"}")
        ProductDetailRequest productDetailRequest = new ProductDetailRequest(id: 1234, productPrice: productPrice)

        when:
        ProductDetailResponse actual = productDetailService.updateProductDetail(1234, productDetailRequest)

        then:
        1 * objectMapper.writeValueAsString(productPrice) >> "{\"value\":4.99,\"currency_code\":\"USD\"}"
        1 * productDetailRepository.save(_ as ProductDetail) >> productDetail
        1 * objectMapper.readValue("{\"value\":4.49,\"currency_code\":\"USD\"}", ProductPrice.class) >> productPrice
        0 * _

        actual.getId() == 1234
        actual.getProductPrice().value == 4.99
        actual.getProductPrice().currencyCode == "USD"
    }

    def "update product detail - Exceptional scenario"() {
        given:
        ProductPrice productPrice = new ProductPrice()
        productPrice.value = 4.99
        productPrice.currencyCode = "USD"
        ProductDetail productDetail = new ProductDetail(id: 1234, currentPrice: "{\"value\":4.49,\"currency_code\":\"USD\"}")
        ProductDetailRequest productDetailRequest = new ProductDetailRequest(id: 1234, productPrice: productPrice)

        when:
        ProductDetailResponse actual = productDetailService.updateProductDetail(1234, productDetailRequest)

        then:
        1 * objectMapper.writeValueAsString(productPrice) >>  { throw new Exception("some exception") }
        0 * _

        thrown Exception
    }

    def "construct product details - Happy path"() {
        given:
        ProductPrice productPrice = new ProductPrice()
        productPrice.value = 4.99
        productPrice.currencyCode = "USD"
        ProductDetail productDetail = new ProductDetail(id: 1234, currentPrice: "{\"value\":4.99,\"currency_code\":\"USD\"}")
        Product product = new Product(item: new Item(productDescription: new ProductDescription(title: "Sample item Desc")))

        when:
        ProductDetailResponse actual = productDetailService.constructProductDetailResponse(1234, productDetail, product)

        then:
        1 * objectMapper.readValue("{\"value\":4.99,\"currency_code\":\"USD\"}", ProductPrice.class) >> productPrice
        0 * _

        actual.id == 1234
        actual.name == "Sample item Desc"
        actual.productPrice.value == 4.99
        actual.productPrice.currencyCode == "USD"
    }

    def "construct product details - Exception scenario"() {
        given:
        ProductPrice productPrice = new ProductPrice()
        productPrice.value = 4.99
        productPrice.currencyCode = "USD"
        ProductDetail productDetail = new ProductDetail(id: 1234, currentPrice: "{\"value\":4.99,\"currency_code\":\"USD\"}")
        Product product = new Product(item: new Item(productDescription: new ProductDescription(title: "Sample item Desc")))

        when:
        ProductDetailResponse actual = productDetailService.constructProductDetailResponse(1234, productDetail, product)

        then:
        1 * objectMapper.readValue("{\"value\":4.99,\"currency_code\":\"USD\"}", ProductPrice.class) >> { throw new Exception("some exception") }
        0 * _

        thrown Exception
    }
}
