package upn.proj.sowad.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import upn.proj.sowad.entities.User;

public interface UserRepository extends JpaRepository<User,Long>{

	User findUserByUsername(String username);

	User findUserByEmail(String email);
}
