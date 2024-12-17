package br.ufs.dcomp.dropoutguard;

import br.ufs.dcomp.dropoutguard.shared.infrastructure.knowledgedatabase.entrypoint.shell.ShellConfiguration;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.jline.utils.AttributedString;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.command.annotation.EnableCommand;
import org.springframework.shell.jline.PromptProvider;

@SpringBootApplication
@EnableCommand(ShellConfiguration.class)
public class DropoutguardApplication {

	public static void main(String[] args) {
		SpringApplication.run(DropoutguardApplication.class, args);
	}

	@Bean
	public ObjectMapper mapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.setDateFormat(new com.fasterxml.jackson.databind.util.StdDateFormat());
		mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
		return mapper;
	}

	@Bean
	public PromptProvider promptProvider() {
		return () -> new AttributedString("dropoutguard:>");
	}
}
