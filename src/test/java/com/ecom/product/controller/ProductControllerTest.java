package com.ecom.product.controller;

import com.ecom.product.dto.Product;
import com.ecom.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URL;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductController productController;

    @Test
    void getProductById() throws Exception {
        //Product product = new Product("name","brand",Double.valueOf(25),"category",new URL("https://www.google.com"), Map.of("feature1","value1"));

        Mockito.when(productService.getProductById(Mockito.anyString())).thenReturn(null);

        this.mockMvc.perform(get("/product/byId/demoId"))
                .andExpect(status().is4xxClientError());

        Mockito.verify(productService,Mockito.times(1)).getProductById(Mockito.anyString());
    }
}