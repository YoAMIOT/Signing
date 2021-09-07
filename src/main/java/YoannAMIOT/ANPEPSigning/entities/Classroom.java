package YoannAMIOT.ANPEPSigning.entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity

public class Classroom {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private Date StartDate;
	
	@Column(nullable = false)
	private Date EndDate;
	
	@Column
	@ManyToMany
	@JoinTable(name = "classrooms_students", joinColumns = @JoinColumn(name = "id_classroom", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "id_user", referencedColumnName = "id"))
	private List<User> Students = new ArrayList<User>();
	
	@ManyToOne(targetEntity = User.class)
	private User Teacher;


	
	//TO STRING//
	@Override
	public String toString() {
		return "Classroom [id=" + id + ", name=" + name + ", StartDate=" + StartDate + ", EndDate=" + EndDate
				+ ", Students=" + Students + ", Teacher=" + Teacher + "]";
	}
	
	

	//CONSTRUCTORS//
	public Classroom() {
		super();
	}
	
	public Classroom(int id, String name, Date startDate, Date endDate, List<User> students, User teacher) {
		super();
		this.id = id;
		this.name = name;
		StartDate = startDate;
		EndDate = endDate;
		Students = students;
		Teacher = teacher;
	}
	
	
	
	//GETTERS AND SETTERS//
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Date getStartDate() {
		return StartDate;
	}
	
	public void setStartDate(Date startDate) {
		StartDate = startDate;
	}
	
	public Date getEndDate() {
		return EndDate;
	}
	
	public void setEndDate(Date endDate) {
		EndDate = endDate;
	}
	
	public List<User> getStudents() {
		return Students;
	}
	
	public void setStudents(List<User> students) {
		Students = students;
	}
	
	public User getTeacher() {
		return Teacher;
	}
	
	public void setTeacher(User teacher) {
		Teacher = teacher;
	}
}