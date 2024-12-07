package com.example.userCRUD;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.userCRUD.user.User;
import com.example.userCRUD.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class UserCRUDApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	void saveUserTest() { // no need to make tests public. sonar warns as well.
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

	@Test
	public void getAllUsersTest() {
		List<User> users = userRepository.findAll();

		Assertions.assertThat(users).isNotNull();
		Assertions.assertThat(users.size()).isGreaterThanOrEqualTo(0);
	}

	@Test
	public void findUserByIdTest() {
		User user = User.builder()
				.id(UUID.randomUUID())
				.firstname("Alice")
				.lastname("Smith")
				.age(25)
				.hobbies(List.of("reading", "cycling"))
				.smoker(false)
				.favoriteFood("sushi")
				.build();

		userRepository.save(user);

		// note this is edgy: usually your DB will assign the ID acc to your GeneratedValue strategy
		// so no guarantee that the id will be user assigned instead of db assigned here:
		Optional<User> foundUser = userRepository.findById(user.getId());
		Assertions.assertThat(foundUser).isPresent();
		Assertions.assertThat(foundUser.get().getFirstname()).isEqualTo("Alice");
	}

	@Test
	public void findUserByFirstnameTest() {
		User user = User.builder()
				.id(UUID.randomUUID())
				.firstname("John")
				.lastname("Doe")
				.age(30)
				.hobbies(List.of("swimming", "running"))
				.smoker(false)
				.favoriteFood("pizza")
				.build();

		userRepository.save(user);

		List<User> users = userRepository.findAllByFirstnameContaining("John");
		Assertions.assertThat(users).isNotEmpty();
		Assertions.assertThat(users.get(0).getLastname()).isEqualTo("Doe");
	}

	@Test
	public void updateUserTest() {
		User user = User.builder()
				.id(UUID.randomUUID())
				.firstname("Mike")
				.lastname("Ross")
				.age(35)
				.hobbies(List.of("traveling"))
				.smoker(false)
				.favoriteFood("burger")
				.build();

		userRepository.save(user);

		User savedUser = userRepository.findById(user.getId()).orElseThrow();
		savedUser.setAge(36);
		savedUser.setFavoriteFood("pasta");
		userRepository.save(savedUser);

		User updatedUser = userRepository.findById(user.getId()).orElseThrow();
		Assertions.assertThat(updatedUser.getAge()).isEqualTo(36);
		Assertions.assertThat(updatedUser.getFavoriteFood()).isEqualTo("pasta");
	}

	@Test
	public void deleteUserTest() {
		User user = User.builder()
				.id(UUID.randomUUID())
				.firstname("Sarah")
				.lastname("Connor")
				.age(40)
				.hobbies(List.of("shooting"))
				.smoker(false)
				.favoriteFood("steak")
				.build();

		userRepository.save(user);
		UUID userId = user.getId();

		userRepository.deleteById(userId);

		Optional<User> deletedUser = userRepository.findById(userId);
		Assertions.assertThat(deletedUser).isEmpty();
	}
}
