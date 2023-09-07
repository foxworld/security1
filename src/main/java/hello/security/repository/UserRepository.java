package hello.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.security.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
