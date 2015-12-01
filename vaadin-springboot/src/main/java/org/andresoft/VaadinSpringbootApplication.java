package org.andresoft;

import org.andresoft.datasource.FoodEstablismentServlet;
import org.andresoft.datasource.SimpleExampleServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.andresoft")
@ServletComponentScan(basePackages = "org.andresoft")
public class VaadinSpringbootApplication
{


    @Bean
    public ServletRegistrationBean simpleServletRegistrationBean()
    {
        return new ServletRegistrationBean(new SimpleExampleServlet(), "/simpleexample");
    }

    public static void main(String[] args)
    {
        SpringApplication.run(VaadinSpringbootApplication.class, args);
    }
}
