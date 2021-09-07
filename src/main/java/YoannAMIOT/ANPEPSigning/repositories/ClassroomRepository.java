package YoannAMIOT.ANPEPSigning.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import YoannAMIOT.ANPEPSigning.entities.Classroom;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {
//TEACHER//
	//CHECK IF THERE'S ANY CLASSROOM WHERE THIS TEACHER IS THE MAIN TEACHER//
	@Query(value = "SELECT IF (COUNT(*) > 0, 'true', 'false') FROM classroom c WHERE c.teacher_id = ?1", nativeQuery = true)
	boolean existsClassroomWithThisMainTeach(int teacherId);
		
	//GET ALL THE CLASSROOM WHERE THIS TEACHER IS THE MAIN TEACHER//
	@Query(value = "SELECT * FROM classroom c WHERE c.teacher_id = ?1", nativeQuery = true)
	List<Classroom> findByTeacherId(int studentId);
}
