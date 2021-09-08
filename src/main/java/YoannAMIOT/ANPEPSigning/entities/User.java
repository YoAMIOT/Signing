package YoannAMIOT.ANPEPSigning.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity

public class User implements Serializable, UserDetails{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(unique = true ,nullable = false)
	private String email;

	@Column(nullable = false)
	private String firstName;
	
	@Column(nullable = false)
	private String lastName;

	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private int responsability;
	
	@Column(nullable = true)
	private int idClass;
	
	@Column(nullable = true)
	@ManyToMany
	@JoinTable(name = "classrooms_students", joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "id_classroom", referencedColumnName = "id"))
	private List<Classroom> Classrooms = new ArrayList<Classroom>();
	
	@OneToMany(targetEntity = History.class)
	private List<History> Histories = new ArrayList<History>();
	
	@OneToMany(targetEntity = Classroom.class)
	private List<Classroom> Classroom = new ArrayList<Classroom>();
	
	@Transient
	private List<History> absentHistories = new ArrayList<History>();
	
	
	
	//TO STRING//
	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", password=" + password + ", responsability=" + responsability + ", idClass=" + idClass + "]";
	}
	
	
	
	//CONSTRUCTORS//
	public User() {
		super();
	}
	

	public User(int id, String email, String firstName, String lastName, String password, int responsability, int idClass, List<YoannAMIOT.ANPEPSigning.entities.Classroom> classrooms, List<History> histories, List<YoannAMIOT.ANPEPSigning.entities.Classroom> classroom) {
		super();
		this.id = id;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.responsability = responsability;
		this.idClass = idClass;
		Classrooms = classrooms;
		Histories = histories;
		Classroom = classroom;
	}

	
	
	//GETTERS AND SETTERS//
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getResponsability() {
		return responsability;
	}
	
	public void setResponsability(int responsability) {
		this.responsability = responsability;
	}
	
	public int getIdClass() {
		return idClass;
	}
	
	public void setIdClass(int idClass) {
		this.idClass = idClass;
	}
	
	public List<Classroom> getClassrooms() {
		return Classrooms;
	}
	
	public void setClassrooms(List<Classroom> classrooms) {
		Classrooms = classrooms;
	}
	
	public List<History> getHistories() {
		return Histories;
	}
	
	public void setHistories(List<History> histories) {
		Histories = histories;
	}
	
	public List<Classroom> getClassroom() {
		return Classroom;
	}
	
	public void setClassroom(List<Classroom> classroom) {
		Classroom = classroom;
	}

	public List<History> getAbsentHistories() {
		return absentHistories;
	}

	public void setAbsentHistories(List<History> absentHistories) {
		this.absentHistories = absentHistories;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
}
