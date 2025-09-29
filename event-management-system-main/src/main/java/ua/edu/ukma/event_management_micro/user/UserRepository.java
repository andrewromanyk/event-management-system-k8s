package ua.edu.ukma.event_management_micro.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByFirstName(String firstName);

    UserEntity findByUsername(String username);
}