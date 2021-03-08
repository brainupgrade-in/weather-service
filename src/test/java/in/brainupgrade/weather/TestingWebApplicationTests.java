package in.brainupgrade.weather;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
public class TestingWebApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	WeatherController controller;
	@Test
	public void contextLoads() {
		assertThat(controller).isNotNull();
	}


//	@Test
//	public void shouldReturnDefaultMessage() throws Exception {
//		ResultActions result = this.mockMvc.perform(post("/get-cities","{\"Delhi\"}"));
//		
//		result.andDo(print()).andExpect(status().isOk())
//				.andExpect(content().string(containsString("Delhi")));
//	}

}