package com.algaworks.brewer.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.algaworks.brewer.mail.Mailer;

/**
 * @author joaovictor
 *pode ser assim
 *@PropertySources({	
	@PropertySource({ "classpath:env/mail-${enviroment:local}.properties" }),
	@PropertySource(value = { "file:${USERPROFILE}/brewer-mail.properties" }, ignoreResourceNotFound = true)
})
 */

@Configuration
@ComponentScan(basePackageClasses = Mailer.class)
//@PropertySource({"classpath:env/mail.properties"})
//@PropertySource({"classpath:env/mail-${ambiente:local}.properties"})
//para iOS
//@PropertySource( value={"file://${HOME}/.brewer-mail.properties"}, ignoreResourceNotFound = true)
//para windowns
@PropertySource( value={"file:${HOME}/.brewer-mail.properties"}, ignoreResourceNotFound = true) //Sobrescreve a propriedade password
public class MailConfig {
	
	@Autowired
	private Environment env;
	
	@Bean
	public JavaMailSender mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.sendgrid.net");
		mailSender.setPort(587);
		mailSender.setUsername(env.getProperty("brewer2.email.username"));
		//mailSender.setPassword(env.getProperty("brewer2.password"));
		mailSender.setPassword(env.getProperty("SENDGRID_PASSWORD"));
		
		System.out.println(env.getProperty("brewer2.email.username"));
		System.out.println(env.getProperty("SENDGRID_PASSWORD"));
		
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.debug", false);
		props.put("mail.smtp.connectiontimeout", 10000); //milisegundos
		
		mailSender.setJavaMailProperties(props);
		
		return mailSender;
	}
}
