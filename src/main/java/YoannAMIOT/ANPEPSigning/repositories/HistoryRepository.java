package YoannAMIOT.ANPEPSigning.repositories;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import YoannAMIOT.ANPEPSigning.entities.History;

public interface HistoryRepository extends JpaRepository<History, Integer>{
//STUDENT//
	//CHECK IF THERE'S AN HISTORY FOR THE STUDENT AND DATE//
	@Query(value = "SELECT IF (COUNT(*) > 0, 'true', 'false') FROM history h WHERE h.student_id = ?1 AND h.date = ?2", nativeQuery = true)
	boolean existsByStudentIdAndDate(int studentId, Date date);

	//GET THE HISTORY FOR THE STUDENT AND DATE//
	@Query(value = "SELECT * FROM history h WHERE h.student_id = ?1 AND h.date = ?2", nativeQuery = true)
	History findByStudentIdAndDate(int studentId, Date date);
	
	
	
	//CHECK IF THERE'S ANY HISTORY WHERE THE STUDENT WAS ABSENT//
	@Query(value = "SELECT IF (COUNT(*) > 0, 'true', 'false') FROM history h WHERE h.student_id = ?1 AND (morning_check = 0 OR afternoon_check = 0)", nativeQuery = true)
	boolean existsByStudentIdAndNotSigned(int studentId);
	
	//GET ALL THE HISTORIES WHERE THE STUDENT WAS ABSENT//
	@Query(value = "SELECT * FROM history h WHERE h.student_id = ?1 AND (h.morning_check = 0 OR h.afternoon_check = 0) AND h.date != ?2 ORDER BY date DESC", nativeQuery = true)
	List<History> findByStudentsAndNotSigned(int studentId, Date date);
	
	
	
	//SET MORNING SIGN FOR THE STUDENT
	@Transactional
	@Modifying
	@Query(value = "UPDATE history h SET h.morning_sign = true WHERE h.student_id = ?1 AND h.date = ?2", nativeQuery = true)
	void updateMorningSignByStudent(int studentId, Date date);
	
	//SET AFTERNOON SIGN FOR THE STUDENT
	@Transactional
	@Modifying
	@Query(value = "UPDATE history h SET h.afternoon_sign = true WHERE h.student_id = ?1 AND h.date = ?2", nativeQuery = true)
	void updateAfternoonSignByStudent(int studentId, Date date);
}
