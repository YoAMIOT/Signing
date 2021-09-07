package YoannAMIOT.ANPEPSigning.entities;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity

public class History {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(nullable = false)
	private Date date;
	
	@Column(nullable = false, columnDefinition = "boolean default false")
	private boolean morningSign;
	
	@Column(nullable = false, columnDefinition = "boolean default false")
	private boolean afternoonSign;
	
	@Column(nullable = false, columnDefinition = "boolean default false")
	private boolean morningCheck;
	
	@Column(nullable = false, columnDefinition = "boolean default false")
	private boolean afternoonCheck;
	
	@ManyToOne(targetEntity = User.class)
	private User Student;

	
	
	//TO STRING//
	@Override
	public String toString() {
		return "History [id=" + id + ", date=" + date + ", morningSign=" + morningSign + ", afternoonSign="
				+ afternoonSign + ", morningCheck=" + morningCheck + ", afternoonCheck=" + afternoonCheck + ", Student="
				+ Student + "]";
	}

	
	
	//CONSTRUCTORS//
	public History() {
		super();
	}
	
	public History(int id, Date date, boolean morningSign, boolean afternoonSign, boolean morningCheck, boolean afternoonCheck, User student) {
		super();
		this.id = id;
		this.date = date;
		this.morningSign = morningSign;
		this.afternoonSign = afternoonSign;
		this.morningCheck = morningCheck;
		this.afternoonCheck = afternoonCheck;
		Student = student;
	}


	
	//GETTERS AND SETTERS//
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public boolean isMorningSign() {
		return morningSign;
	}
	
	public void setMorningSign(boolean morningSign) {
		this.morningSign = morningSign;
	}
	
	public boolean isAfternoonSign() {
		return afternoonSign;
	}
	
	public void setAfternoonSign(boolean afternoonSign) {
		this.afternoonSign = afternoonSign;
	}
	
	public boolean isMorningCheck() {
		return morningCheck;
	}
	
	public void setMorningCheck(boolean morningCheck) {
		this.morningCheck = morningCheck;
	}
	
	public boolean isAfternoonCheck() {
		return afternoonCheck;
	}
	
	public void setAfternoonCheck(boolean afternoonCheck) {
		this.afternoonCheck = afternoonCheck;
	}
	
	public User getStudent() {
		return Student;
	}
	
	public void setStudent(User student) {
		Student = student;
	}
}
