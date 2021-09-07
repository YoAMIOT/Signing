package YoannAMIOT.ANPEPSigning.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import YoannAMIOT.ANPEPSigning.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	Optional<User> findByEmail(String email);
}