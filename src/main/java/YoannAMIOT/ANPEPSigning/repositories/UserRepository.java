package YoannAMIOT.ANPEPSigning.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import YoannAMIOT.ANPEPSigning.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	//GET ONE USER BY IT'S MAIL (FOR LOGIN ONLY)//
	Optional<User> findByEmail(String email);
	
	
	
	//GET ONE USER BY IT'S ID//
	@Query(value = "SELECT * FROM user WHERE id = ?1", nativeQuery = true)
	User findStudentById(int userId);

	
	
	//CHECK IF THERE'S ANY EXISTING USER WITH THIS RESPONSABILITY//
	@Query(value = "SELECT IF (COUNT(*) > 0, 'true', 'false') FROM user u WHERE u.responsability = ?1", nativeQuery = true)
	boolean existsAnyUserWithResponsability(int responsability);
	
	//GET ALL THE USERS BY RESPONSABILITY
	@Query(value = "SELECT * FROM user u WHERE u.responsability = ?1", nativeQuery = true)
	List<User> findAllUserByResponsability(int responsability);
	
	
	
	//GET ALL THE STUDENTS THAT ARE NOT ALREADY IN THIS CLASSROOM
	@Query(value = "SELECT * FROM user u WHERE u.responsability = 0 AND u.id NOT IN (SELECT id_user FROM classrooms_students WHERE id_classroom = ?1);", nativeQuery = true)
	List<User> findAllStudentNotInClassroom(int classroomId);
	
}