package jKMS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class JKMSConfiguration {
    
	 @Bean
	 public ReloadableResourceBundleMessageSource messageSource() {
		 ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		 messageSource.setBasename("classpath:lang/messages");
		 messageSource.setDefaultEncoding("UTF-8");
		 messageSource.setCacheSeconds(10); //reload messages every 10 seconds
		 return messageSource;
	 }
	
	
//    @Bean
//    public ViewResolver viewResolver() {
//        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
//        templateResolver.setTemplateMode("HTML5");
//        templateResolver.setPrefix("/resources/templates/");
//        templateResolver.setSuffix(".html");
//        //templateResolver.setCharacterEncoding("UTF-8");
//        
//        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
//        messageSource.setBasename("/src/main/resources/lang/messages");
//        messageSource.setDefaultEncoding("UTF-8");
//        // # -1 : never reload, 0 always reload
//        messageSource.setCacheSeconds(0);
//        
//        SpringTemplateEngine engine = new SpringTemplateEngine();
//        engine.setTemplateResolver(templateResolver);
//        engine.setMessageSource(messageSource);
// 
//        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
//        //viewResolver.setCharacterEncoding("UTF-8");
//        viewResolver.setTemplateEngine(engine);
//        return viewResolver;
//    }
}
