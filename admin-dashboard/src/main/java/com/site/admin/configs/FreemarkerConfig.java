package com.site.admin.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import no.api.freemarker.java8.Java8ObjectWrapper;
@Slf4j
@Configuration
public class FreemarkerConfig implements BeanPostProcessor {


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof FreeMarkerConfigurer) {
            try {
                FreeMarkerConfigurer configurer = (FreeMarkerConfigurer) bean;
                Java8ObjectWrapper objectWrapper = new Java8ObjectWrapper(freemarker.template.Configuration.getVersion());
                configurer.getConfiguration().setObjectWrapper(objectWrapper);
                log.info("FreeMarkerConfigurer has been successfully configured with Java8ObjectWrapper.");
            } catch (Exception e) {
                log.error("Error configuring FreeMarkerConfigurer: " + e.getMessage(), e);
            }
        }
        return bean;
    }

}
