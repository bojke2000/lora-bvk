package com.bojan.lora;

import com.bojan.lora.service.MqttService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoraApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private MqttService mqttService;

	@Test
	void contextLoads() {
	}

	@Test
	void getAllLoraMtsReturnsEmptyList() throws Exception {
		mockMvc.perform(get("/api/loramts"))
			.andExpect(status().isOk())
			.andExpect(content().json("[]"));
	}

	@Test
	void createLoraMtsPublishesNormalizedPayload() throws Exception {
		String requestBody = "{\"DevEUI_uplink\":{\"DevEUI\":\"A840411D1134ABCD\",\"FPort\":1,\"payload_hex\":\"01020304\"}}";

		mockMvc.perform(post("/api/loramts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isOk())
			.andExpect(content().string(""));

		ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		verify(mqttService).publish(payloadCaptor.capture());

		JsonNode publishedJson = objectMapper.readTree(payloadCaptor.getValue());
		assertEquals("a840411d1134abcd", publishedJson.get("devEUI").asText());
		assertEquals(1, publishedJson.get("fPort").asInt());

		String expectedData = Base64.getEncoder()
			.encodeToString("01020304".getBytes(StandardCharsets.UTF_8));
		assertEquals(expectedData, publishedJson.get("data").asText());
	}

	@Test
	void createLoraMtsWithoutDevEUIUplinkDoesNotPublish() throws Exception {
		mockMvc.perform(post("/api/loramts")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}"))
			.andExpect(status().isOk())
			.andExpect(content().string(""));

		verify(mqttService, never()).publish(anyString());
	}

	@Test
	void createLoraMtsWithoutDevEUIDoesNotPublish() throws Exception {
		String requestBody = "{\"DevEUI_uplink\":{\"FPort\":1,\"payload_hex\":\"01020304\"}}";

		mockMvc.perform(post("/api/loramts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isOk())
			.andExpect(content().string(""));

		verify(mqttService, never()).publish(anyString());
	}

}
