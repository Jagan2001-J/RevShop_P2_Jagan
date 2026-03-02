package com.rev.app.repository;

import com.rev.app.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class IUserRepositoryTest {

    @Autowired
    private IUserRepository userRepository;

    @Test
    public void testSaveUser() {
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .role(User.Role.BUYER)
                .build();

        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindByEmail() {
        User user = User.builder()
                .name("Find Me")
                .email("findme@example.com")
                .password("password")
                .role(User.Role.SELLER)
                .build();
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("findme@example.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Find Me");
    }

    @Test
    public void testFindByEmail_NotFound() {
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");
        assertThat(foundUser).isNotPresent();
    }
}
