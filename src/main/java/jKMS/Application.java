package jKMS;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application extends WebMvcConfigurerAdapter {
	
	public static AppGui gui;

	public static void main(String[] args) {
		gui = new AppGui();

		SpringApplication.run(Application.class, args);
		
		URI index = null;
		
		try {
			index = new URI("http://localhost:8080/index");
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}

		try {
			Desktop.getDesktop().browse(index);
			gui.setReady();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		System.out.println();
		System.out.println("     ██╗██╗  ██╗███╗   ███╗███████╗");
		System.out.println("     ██║██║ ██╔╝████╗ ████║██╔════╝");
		System.out.println("     ██║█████╔╝ ██╔████╔██║███████╗");
		System.out.println("██   ██║██╔═██╗ ██║╚██╔╝██║╚════██║");
		System.out.println("╚█████╔╝██║  ██╗██║ ╚═╝ ██║███████║");
		System.out.println(" ╚════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝");
		System.out.println("01101010 01001011 01001101 01010011");
		System.out.println();
		System.out.println("Pit Market 2.0 by Quiryn, freeDom, jUSTUS, yangxinyu and siegmund42.");
		System.out.println();
		System.out.println();
		
	}
	
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.GERMAN);
        return slr;
    }
 
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }
    
    @Bean 
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("256KB");
        factory.setMaxRequestSize("256KB");
        return factory.createMultipartConfig();
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
    
}
