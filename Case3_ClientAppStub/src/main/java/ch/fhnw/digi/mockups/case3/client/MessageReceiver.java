package ch.fhnw.digi.mockups.case3.client;

import javax.jms.ConnectionFactory;

import ch.fhnw.digi.mockups.case3.JobRequestMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {

	@Autowired
	private UI ui;


	// FIXME: Nachrichten Empfangen und an das GUI weitergeben 
	// @see ui.addJobToList()
	// @see ui.assignJob()

	@JmsListener(destination = "greetRequests", containerFactory = "myFactory")
	public void receiveMessage(JobRequestMessage c) {


	}
	
	
	
	
	@Bean
	public DefaultJmsListenerContainerFactory myFactory(ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

		configurer.configure(factory, connectionFactory);
		factory.setPubSubDomain(true);
		factory.setMessageConverter(jacksonJmsMessageConverter());

		return factory;
	}

	@Bean // Serialize message content to json/from using TextMessage
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}

}
