package com.example.userCRUD;

import com.example.userCRUD.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.userCRUD.user.User;

@SpringBootTest
class UserCRUDApplicationTests {

	@Autowired
	private UserRepository userRepository;
	@Test
	public void saveUserTest(){

		User user = User.builder()
				.id(1)
				.firstname("Erik")
				.lastname("Oros")
				.age(28)
				.hobbies(List.of(gambling, hussling))
				.smoker(true)
				.favoriteFood("cigares & coffee")
				;

		userRepository.save(user);
	}

}
