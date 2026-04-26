package com.buren.playlog;

import com.buren.playlog.dto.*;
import com.buren.playlog.repository.BlacklistTokenRepository;
import com.buren.playlog.repository.GameRepository;
import com.buren.playlog.repository.ReviewRepository;
import com.buren.playlog.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${api.root}")
	private String root;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private BlacklistTokenRepository blacklistTokenRepository;

	private final ObjectMapper objectMapper = new ObjectMapper()
			.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

	public String registerLogin() throws Exception{
			UserRequestDTO register = new UserRequestDTO("sample@gmail.com", "sample", "12345");
			mockMvc.perform(post(root+"/auth/register")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(register)))
					.andExpect(status().isCreated())
					.andReturn();
		LoginRequestDTO login = new LoginRequestDTO("sample","12345");
		String token = mockMvc.perform(post(root+"/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(login)))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		return objectMapper.readTree(token).get("accessToken").asText();
	}

	@Test
	void getUserTest() throws Exception {
		String token = registerLogin();
		Long id = userRepository.findByUsername("sample").get().getId();
		MvcResult result = mockMvc.perform(get(root+"/users/"+id)
				.header("Authorization","Bearer "+token)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		String responseBody = result.getResponse().getContentAsString();

		assertEquals(userRepository.findByUsername("sample").get().getUsername(),objectMapper.readTree(responseBody).get("username").asText());
		assertEquals(userRepository.findByUsername("sample").get().getEmail(),objectMapper.readTree(responseBody).get("email").asText());
	}

	@Test
	void deleteUserTest() throws Exception {
		String token = registerLogin();
		if(userRepository.findByUsername("sample").isPresent()) {
			Long id = userRepository.findByUsername("sample").get().getId();
			mockMvc.perform(delete(root + "/users/" + id)
							.header("Authorization", "Bearer " + token)
							.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isNoContent())
					.andReturn();

			assertFalse(userRepository.findById(id).get().isActive());
		}
	}

	@Test
	void postReviewTest() throws Exception {
		ReviewRequestDTO reviewRequestDTO = new ReviewRequestDTO(5,"Witcher 3 is the best game I have ever played. I recommend this game to everyone", 3328L);
		String token = registerLogin();
		MvcResult result = mockMvc.perform(post(root+"/reviews")
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
		assertEquals(reviewRequestDTO.gameId(),gameRepository.findById(Long.valueOf(createdId)).get().getRawgId());
		assertNotNull(location);
	}

	@Test
	void getAllReviewsTest() throws Exception{
		String token = registerLogin();
		ReviewRequestDTO reviewRequestDTO1 = new ReviewRequestDTO(5,"Witcher 3 is the best game I have ever played. I recommend this game to everyone", 3328L);
		MvcResult result1 = mockMvc.perform(post(root+"/reviews")
						.header("Authorization","Bearer "+token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reviewRequestDTO1)))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andReturn();

		ReviewRequestDTO reviewRequestDTO2 = new ReviewRequestDTO(4,"GTA 5 is really good game.", 3498L);
		MvcResult result2 = mockMvc.perform(post(root+"/reviews")
						.header("Authorization","Bearer "+token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reviewRequestDTO2)))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andReturn();

		MvcResult result = mockMvc.perform(get(root+"/reviews")
				.header("Authorization","Bearer "+token)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		String location1 = result1.getResponse().getHeader("Location");
		String createdId1 = location1.substring(location1.lastIndexOf("/") + 1);

		String location2 = result2.getResponse().getHeader("Location");
		String createdId2 = location2.substring(location2.lastIndexOf("/") + 1);


		assertNotNull(result.getResponse().getContentAsString());
		assertEquals(reviewRequestDTO1.gameId(),gameRepository.findById(Long.valueOf(createdId1)).get().getRawgId());
		assertEquals(reviewRequestDTO2.gameId(),gameRepository.findById(Long.valueOf(createdId2)).get().getRawgId());
	}

	@Test
	void getSpecificReviewTest() throws Exception{
		String token = registerLogin();
		ReviewRequestDTO reviewRequestDTO = new ReviewRequestDTO(5,"Witcher 3 is the best game I have ever played. I recommend this game to everyone", 3328L);
		MvcResult locationid = mockMvc.perform(post(root+"/reviews")
						.header("Authorization","Bearer "+token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reviewRequestDTO)))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andReturn();

		String location = locationid.getResponse().getHeader("Location");
		String createdId = location.substring(location.lastIndexOf("/") + 1);

		MvcResult result = mockMvc.perform(get(root+"/reviews/"+createdId)
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
		String token = registerLogin();
		ReviewRequestDTO reviewRequestDTO = new ReviewRequestDTO(5,"Witcher 3 is the best game I have ever played. I recommend this game to everyone", 3328L);
		MvcResult locationId = mockMvc.perform(post(root+"/reviews")
						.header("Authorization","Bearer "+token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reviewRequestDTO)))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andReturn();

		String location = locationId.getResponse().getHeader("Location");
		String createdId = location.substring(location.lastIndexOf("/") + 1);

		ReviewUpdateDTO reviewUpdateDTO = new ReviewUpdateDTO(1,"New comment");
		String oldComment = reviewRepository.findById(Long.valueOf(createdId)).get().getComment();
		Integer oldRating = reviewRepository.findById(Long.valueOf(createdId)).get().getRating();
		MvcResult result = mockMvc.perform(put(root+"/reviews/"+createdId)
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
		String token = registerLogin();
		MvcResult result = mockMvc.perform(get(root+"/games/popular")
				.header("Authorization","Bearer "+token)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		assertNotNull(result.getResponse().getContentAsString());
	}

	@Test
	void getSpecificGameTest() throws Exception {
		String token = registerLogin();
		Long targetRawgId = 3328L;
		MvcResult result = mockMvc.perform(get(root+"/games/"+targetRawgId)
				.header("Authorization","Bearer "+token)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		String responseBody = result.getResponse().getContentAsString();
		String gameName = objectMapper.readTree(responseBody).get("name").asText();

		assertNotNull(gameName);
		assertEquals(targetRawgId,gameRepository.findByRawgId(targetRawgId).get().getRawgId());
	}

	@Test
	void logoutUserTest() throws Exception {
		String token = registerLogin();
		LogoutRequestDTO logoutRequestDTO = new LogoutRequestDTO(token);
		mockMvc.perform(post(root+"/auth/logout")
				.header("Authorization","Bearer "+token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(logoutRequestDTO)))
				.andExpect(status().isNoContent())
				.andReturn();

		assertEquals(token,blacklistTokenRepository.findByToken(token).get().getToken());
	}

}
