package YoannAMIOT.ANPEPSigning.repositories;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import YoannAMIOT.ANPEPSigning.entities.Classroom;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {
	//CHECK IF THERE'S ANY CLASSROOM WHERE THIS TEACHER IS THE MAIN TEACHER//
	@Query(value = "SELECT IF (COUNT(*) > 0, 'true', 'false') FROM classroom c WHERE c.teacher_id = ?1", nativeQuery = true)
	boolean existsClassroomWithThisMainTeach(int teacherId);
		
	//GET ALL THE CLASSROOM WHERE THIS TEACHER IS THE MAIN TEACHER//
	@Query(value = "SELECT * FROM classroom c WHERE c.teacher_id = ?1", nativeQuery = true)
	List<Classroom> findByTeacherId(int teacherId);
	
	
	
	//GET THE CLASSROOM BY ID//
	@Query(value = "SELECT * FROM classroom c WHERE id = ?1", nativeQuery = true)
	Classroom findById(int classroomId);
	
	
	
	//CHECK IF THERE'S ANY STUDENT IN THIS CLASSROOM//
	@Query(value = "SELECT IF (COUNT(*) > 0, 'true', 'false') FROM classrooms_students c WHERE c.id_classroom = ?1", nativeQuery = true)
	boolean existsStudentInThisClassroom(int classroomId);
	
	//GET THE STUDENTS ID BY THE CLASSROOM ID//
	@Query(value = "SELECT id_user FROM classrooms_students WHERE id_classroom = ?1", nativeQuery = true)
	List<Integer> findStudentsIdByClassroomId(int classroomId);
	
	
	
	//CHECK IF THERE'S ANY EXISTING CLASSROOM//
	@Query(value = "SELECT IF (COUNT(*) > 0, 'true', 'false') FROM classroom", nativeQuery = true)
	boolean existsAnyClassroom();
	
	
	
	//UPDATE OF THE CLASSROOM//
	@Transactional
	@Modifying
	@Query(value = "UPDATE classroom c SET c.name = ?1, c.start_date = ?2, c.end_date = ?3, c.teacher_id = ?4 WHERE c.id = ?5", nativeQuery = true)
	void updateClassroom(String name, Date startDate, Date endDate, int teacherId, int id);
	
	
	
	//ADD THE STUDENT TO THE CLASSROOM//
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO classrooms_students (id_user, id_classroom) VALUES (?1 , ?2)", nativeQuery = true)
	void addUserToClassroom(int idStudent, int idClassroom);
	
	//REMOVE THE STUDENT FROM THE CLASSROOM//
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM classrooms_students WHERE id_user = ?1 AND id_classroom = ?2", nativeQuery = true)
	void removeUserFromClassroom(int idStudent, int idClassroom);
}
