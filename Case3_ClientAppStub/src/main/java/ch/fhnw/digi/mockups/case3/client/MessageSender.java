package ch.fhnw.digi.mockups.case3.client;

import ch.fhnw.digi.mockups.case3.JobRequestMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.stereotype.Component;

import ch.fhnw.digi.mockups.case3.JobMessage;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;

@Component
public class MessageSender {


	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private MessageConverter jacksonJmsMessageConverter;


	public void requestJobFromDispo(JobMessage jm) {
		jmsTemplate.setMessageConverter(jacksonJmsMessageConverter);
		jmsTemplate.setPubSubDomain(false); // we want to send to a queue, not a topic

		final String jobNumber = "message" + jm.getJobnumber();

		// publish a new JobRequestMessage to the channel "dispo.jobs.requestAssignment"
		jmsTemplate.convertAndSend("dispo.jobs.requestAssignment\t", new JobRequestMessage(jm.getJobnumber(), "Franz"), m -> {
			m.setStringProperty("someHeaderField", "someImportantValue");
			m.setJMSCorrelationID(jobNumber);
			return m;
		});

	}


	@Bean
	public JmsListenerContainerFactory<?> myFactory2(ConnectionFactory connectionFactory,
													DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		// This provides all boot's default to this factory, including the message
		// converter
		configurer.configure(factory, connectionFactory);
		factory.setPubSubDomain(true);
		factory.setMessageConverter(jacksonJmsMessageConverter);

		// You could still override some of Boot's default if necessary.
		return factory;
	}

	// used to convert our java messagage object into a JSON String that can be sent
	/*public MessageConverter jacksonJmsMessageConverter2() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}*/

}
