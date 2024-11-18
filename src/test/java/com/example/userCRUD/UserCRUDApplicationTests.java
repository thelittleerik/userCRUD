package com.example.userCRUD;

import com.example.userCRUD.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.userCRUD.user.User;

import java.util.List;
import java.util.UUID;

@SpringBootTest
class UserCRUDApplicationTests {

	@Autowired
	private UserRepository userRepository;
	@Test
	public void saveUserTest(){

		User user = User.builder()
				.id(UUID.randomUUID())
				.firstname("Erik")
				.lastname("Oros")
				.age(28)
				.hobbies(List.of("gambling", "hustling"))
				.smoker(true)
				.favoriteFood("cigarettes & coffee")
				.build();

		userRepository.save(user);

		Assertions.assertThat(user.getId()).isNotNull();
	}

}
