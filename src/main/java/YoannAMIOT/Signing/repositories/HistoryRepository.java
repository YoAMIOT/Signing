package YoannAMIOT.Signing.repositories;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import YoannAMIOT.Signing.entities.History;

public interface HistoryRepository extends JpaRepository<History, Integer>{
	//CHECK IF THERE'S AN HISTORY FOR THE STUDENT AND DATE//
	@Query(value = "SELECT IF (COUNT(*) > 0, 'true', 'false') FROM history h WHERE h.student_id = ?1 AND h.date = ?2", nativeQuery = true)
	boolean existsByStudentIdAndDate(int studentId, Date date);

	//GET THE HISTORY FOR THE STUDENT AND DATE//
	@Query(value = "SELECT * FROM history h WHERE h.student_id = ?1 AND h.date = ?2", nativeQuery = true)
	History findByStudentIdAndDate(int studentId, Date date);
	
	
	
	//CHECK IF THE STUDENT IS ALREADY COUNTERSIGNED FOR A DATE//
	@Query(value = "SELECT IF (COUNT(*) > 0, 'true', 'false') FROM history h WHERE h.student_id = ?1 AND h.date = ?2 AND (morning_check = 1 OR afternoon_check = 1)", nativeQuery = true)
	boolean existsStudentByCountersigned(int studentId, Date date);
	
	
	
	//CHECK IF THERE'S ANY HISTORY WHERE THE STUDENT WAS ABSENT//
	@Query(value = "SELECT IF (COUNT(*) > 0, 'true', 'false') FROM history h WHERE h.student_id = ?1 AND (morning_check = 0 OR afternoon_check = 0)", nativeQuery = true)
	boolean existsByStudentIdAndNotSigned(int studentId);
	
	//GET ALL THE HISTORIES WHERE THE STUDENT WAS ABSENT BY DATE//
	@Query(value = "SELECT * FROM history h WHERE h.student_id = ?1 AND (h.morning_check = 0 OR h.afternoon_check = 0) AND h.date != ?2 ORDER BY date DESC", nativeQuery = true)
	List<History> findByStudentsAndNotSigned(int studentId, Date date);
	
	
	
	//GET ALL THE HISTORIES WHERE THE STUDENT WAS ABSENT//
	@Query(value = "SELECT * FROM history h WHERE h.student_id = ?1 AND (h.morning_check = 0 OR h.afternoon_check = 0) ORDER BY date DESC", nativeQuery = true)
	List<History> findByStudentsNotSigned(int studentId);
	
	
	
	//SET MORNING SIGN FOR THE STUDENT//
	@Transactional
	@Modifying
	@Query(value = "UPDATE history h SET h.morning_sign = true WHERE h.student_id = ?1 AND h.date = ?2", nativeQuery = true)
	void updateMorningSignByStudent(int studentId, Date date);
	
	//SET AFTERNOON SIGN FOR THE STUDENT//
	@Transactional
	@Modifying
	@Query(value = "UPDATE history h SET h.afternoon_sign = true WHERE h.student_id = ?1 AND h.date = ?2", nativeQuery = true)
	void updateAfternoonSignByStudent(int studentId, Date date);
	
	
	
	//UPDATE STUDENT COUNTERSIGN WITH TEACHER'S CHECK//
	@Transactional
	@Modifying
	@Query(value = "UPDATE history h SET h.morning_check = ?1, h.afternoon_check = ?2 WHERE h.student_id = ?3 AND h.date = ?4", nativeQuery = true)
	void updateStudentHistoryWithTeachCheck(boolean morningCheck, boolean afternoonCheck, int studentId, Date date);
	
	
	
	//CHECK IF A STUDENT HAS ANY EXISTING HISTORY//
	@Query(value = "SELECT IF (COUNT(*) > 0, 'true', 'false') FROM history h WHERE h.student_id = ?1", nativeQuery = true)
	boolean existsAnyHistoryByStudent(int studentId);
	
	
	
	//GET ALL HISTORIES FROM A STUDENT BETWEEN TWO DATES//
	@Query(value="SELECT * FROM history h WHERE h.student_id = ?1 AND date BETWEEN ?2 AND ?3 ORDER BY date ASC", nativeQuery = true)
	List<History> findAllHistoriesBetweenDatesForClassroom(int idStudent, Date startDate, Date endDate);
	
	
	
	//REMOVE HISTORIES FROM A STUDENT//
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM history WHERE student_id = ?1", nativeQuery = true)
	void removeHitsoriesByStudentId(int idStudent);
}
