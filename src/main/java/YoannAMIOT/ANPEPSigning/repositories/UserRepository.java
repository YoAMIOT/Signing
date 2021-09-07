package YoannAMIOT.ANPEPSigning.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import YoannAMIOT.ANPEPSigning.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{
//GLOBAL//
	//GET ONE USER BY IT'S MAIL (FOR LOGIN ONLY)//
	Optional<User> findByEmail(String email);
	
	//GET ONE USER BY IT'S ID//
	@Query(value = "SELECT * FROM user WHERE id = ?1", nativeQuery = true)
	User findStudentById(int userId);
}