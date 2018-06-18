package com.capco.digital.engineering.user;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.capco.digital.engineering.AbstractTest;

@ComponentScan({ "com.capco.digital.engineering.user" })
@WebAppConfiguration
public class UserControllerTest extends AbstractTest {

	@InjectMocks
	private UserController controller = new UserController();

	@Mock
	private UserService userService;

	@Mock
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private WebApplicationContext wac;

	MockMvc mockMvc;

	@BeforeTest
	public void before() {
		MockitoAnnotations.initMocks(this);

		// this.mockMvc =
		// MockMvcBuilders.webAppContextSetup(this.wac).dispatchOptions(true).build();

		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

	}

	@Test
	public void testGetUsers() {
		when(userService.findAllUsers()).thenReturn(getUserList());

		ResponseEntity<List<User>> response = controller.getUsers();
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody().size(), 2);
		assertNotNull(response.getBody().get(0).getId());
		assertNotNull(response.getBody().get(0).getFirstname());
		assertNotNull(response.getBody().get(0).getLastname());
	}

	@Test
	public void testPostUser() {
		User user = new User();
		user.setFirstname("Test");
		user.setLastname("Test");

		when(userService.insertUser(user)).thenReturn(user);

		ResponseEntity<User> response = controller.addUser(user);
		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
	}

	private List<User> getUserList() {
		List<User> users = new ArrayList<>();
		User user1 = new User();
		user1.setFirstname("Dave");
		user1.setLastname("Smith");
		user1.setId("1");

		User user2 = new User();
		user2.setFirstname("John");
		user2.setLastname("Smith");
		user2.setId("2");

		users.add(user1);
		users.add(user2);

		return users;
	}

}
