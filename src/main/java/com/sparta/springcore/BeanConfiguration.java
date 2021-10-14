package com.sparta.springcore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 처음 스프링이 기동될 때 BeanConfiguration 클래스를 바라보고 @Bean 함수들을 살펴보고 필요한 내용들을
// Bean으로 담는다는 의미
@Configuration
public class BeanConfiguration {
    @Bean
    public ProductRepository productRepository() {
        String dbId = "sa";
        String dbPassword = "";
        String dbUrl = "jdbc:h2:mem:springcoredb";

        //ProductRepository 객체를 만들어서 IoC컨테이너 안으로 넣어줌.
        return new ProductRepository(dbId, dbPassword, dbUrl);
    }

    @Bean
    @Autowired
    public ProductService productService(ProductRepository productRepository) {
        //ProductService를 생성할 때 ProductRepository 정보가 필요한데, productRepository를 DI받음.
        //IoC컨테이너에 있는 productRepository bean을 꺼내와서 DI해주는 것임.
        //이렇게 꺼내와서 사용하려면 @Autowired를 넣어줘야함.
        return new ProductService(productRepository);
    }
}
