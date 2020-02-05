package com.looselytyped.containerpatterns;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = ContainerPatternsApplication.class)
@AutoConfigureMockMvc
public class ContainerPatternsApplicationTests {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void resource() throws Exception {
    this.mockMvc.perform(get("/")).andExpect(content().string(containsString("Greetings from Spring Boot!")));
  }

}
