package com.buren.playlog;

import com.buren.playlog.dto.*;
import com.buren.playlog.repository.ReviewRepository;
import com.buren.playlog.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PlaylogApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private UserRepository userRepository;

	private final ObjectMapper objectMapper = new ObjectMapper()
			.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

//    @Test
//	void contextLoads() {
//	}

	public String RegisterLogin() throws Exception{
		if(userRepository.findByUsername("funburak").isEmpty()){
			UserRequestDTO register = new UserRequestDTO("buraqdmrky@gmail.com", "funburak", "12345");
			mockMvc.perform(post("/api/v1/auth/register")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(register)));
		}
		LoginRequestDTO login = new LoginRequestDTO("funburak","12345");
		String token = mockMvc.perform(post("/api/v1/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(login)))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		return objectMapper.readTree(token).get("accessToken").asText();
	}

	@Test
	void getUserTest() throws Exception {
		String token = RegisterLogin();
		Long id = userRepository.findByUsername("funburak").get().getId();
		MvcResult result = mockMvc.perform(get("/api/v1/users/"+id)
				.header("Authorization","Bearer "+token)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		String responseBody = result.getResponse().getContentAsString();

		assertEquals(userRepository.findByUsername("funburak").get().getUsername(),objectMapper.readTree(responseBody).get("username").asText());
		assertEquals(userRepository.findByUsername("funburak").get().getEmail(),objectMapper.readTree(responseBody).get("email").asText());
	}

	@Test
	void deleteUserTest() throws Exception {
		String token = RegisterLogin();
		Long id = userRepository.findByUsername("funburak").get().getId();
		mockMvc.perform(delete("/api/v1/users/"+id)
				.header("Authorization","Bearer "+token)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent())
				.andReturn();

		assertFalse(userRepository.findById(id).get().isActive());
	}

	@Test
	void postReviewTest() throws Exception {
		ReviewRequestDTO reviewRequestDTO = new ReviewRequestDTO(5,"Witcher 3 is the best game I have ever played. I recommend this game to everyone", 4L);
		String token = RegisterLogin();
		MvcResult result = mockMvc.perform(post("/api/v1/reviews")
				.header("Authorization","Bearer "+token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(reviewRequestDTO)))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andReturn();

		String location = result.getResponse().getHeader("Location");
		String createdId = location.substring(location.lastIndexOf("/") + 1);

		assertEquals(reviewRequestDTO.rating(),reviewRepository.findById(Long.valueOf(createdId)).get().getRating());
		assertEquals("Witcher 3 is the best game I have ever played. I recommend this game to everyone",reviewRepository.findById(Long.valueOf(createdId)).get().getComment());
		assertNotNull(location);
	}

	@Test
	void getAllReviewsTest() throws Exception{
		String token = RegisterLogin();
		ReviewRequestDTO reviewRequestDTO1 = new ReviewRequestDTO(5,"Witcher 3 is the best game I have ever played. I recommend this game to everyone", 1L);
		mockMvc.perform(post("/api/v1/reviews")
						.header("Authorization","Bearer "+token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reviewRequestDTO1)))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andReturn();

		ReviewRequestDTO reviewRequestDTO2 = new ReviewRequestDTO(4,"Witcher 2 is decent but not the greatest", 2L);
		mockMvc.perform(post("/api/v1/reviews")
						.header("Authorization","Bearer "+token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reviewRequestDTO2)))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andReturn();

		MvcResult result = mockMvc.perform(get("/api/v1/reviews")
				.header("Authorization","Bearer "+token)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		assertNotNull(result.getResponse().getContentAsString());
	}

	@Test
	void getSpecificReviewTest() throws Exception{
		String token = RegisterLogin();
		ReviewRequestDTO reviewRequestDTO = new ReviewRequestDTO(5,"Witcher 3 is the best game I have ever played. I recommend this game to everyone", 1L);
		MvcResult locationid = mockMvc.perform(post("/api/v1/reviews")
						.header("Authorization","Bearer "+token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reviewRequestDTO)))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andReturn();

		String location = locationid.getResponse().getHeader("Location");
		String createdId = location.substring(location.lastIndexOf("/") + 1);

		MvcResult result = mockMvc.perform(get("/api/v1/reviews/"+createdId)
				.header("Authorization","Bearer "+token)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		String responseBody = result.getResponse().getContentAsString();

		assertEquals(reviewRepository.findById(Long.valueOf(createdId)).get().getComment(),objectMapper.readTree(responseBody).get("comment").asText());
		assertNotNull(reviewRepository.findById(Long.valueOf(createdId)).get().getGame());
		assertNotNull(reviewRepository.findById(Long.valueOf(createdId)).get().getUser());
	}

	@Test
	void updateReviewTest() throws Exception{
		String token = RegisterLogin();
		ReviewRequestDTO reviewRequestDTO = new ReviewRequestDTO(5,"Witcher 3 is the best game I have ever played. I recommend this game to everyone", 1L);
		MvcResult locationid = mockMvc.perform(post("/api/v1/reviews")
						.header("Authorization","Bearer "+token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reviewRequestDTO)))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andReturn();

		String location = locationid.getResponse().getHeader("Location");
		String createdId = location.substring(location.lastIndexOf("/") + 1);

		ReviewUpdateDTO reviewUpdateDTO = new ReviewUpdateDTO(1,"New comment");
		String oldComment = reviewRepository.findById(Long.valueOf(createdId)).get().getComment();
		Integer oldRating = reviewRepository.findById(Long.valueOf(createdId)).get().getRating();
		MvcResult result = mockMvc.perform(put("/api/v1/reviews/"+createdId)
				.header("Authorization","Bearer "+token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(reviewUpdateDTO)))
				.andExpect(status().isOk())
				.andReturn();

		String responseBody = result.getResponse().getContentAsString();

		assertNotEquals(oldComment,objectMapper.readTree(responseBody).get("comment").asText());
		assertNotEquals(oldRating,objectMapper.readTree(responseBody).get("rating").intValue());
	}

	@Test
	void getPopularGameTest() throws Exception {
		String token = RegisterLogin();
		MvcResult result = mockMvc.perform(get("/api/v1/games/popular")
				.header("Authorization","Bearer "+token)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		assertNotNull(result.getResponse().getContentAsString());
	}

	@Test
	void getSpecificGameTest() throws Exception {
		String token = RegisterLogin();
		MvcResult result = mockMvc.perform(get("/api/v1/games/1")
				.header("Authorization","Bearer "+token)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		String responseBody = result.getResponse().getContentAsString();
		String gameName = objectMapper.readTree(responseBody).get("name").asText();

		assertNotNull(gameName);
	}

}
