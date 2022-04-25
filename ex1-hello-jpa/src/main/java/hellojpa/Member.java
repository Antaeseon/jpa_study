package hellojpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;

@Entity
public class Member extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "MEMBER_ID")
	private Long id;

	@Column(name = "USER_NAME")
	private String username;

	@ManyToOne
	@JoinColumn(name = "TEAM_ID", insertable = false, updatable = false) // 읽기 전용으로 처리를 해버린다.
	private Team team;

	@OneToOne
	@JoinColumn(name = "LOCKER_ID")
	private Locker locker;


	// @ManyToMany
	// @JoinTable(name = "MEMBER_PRODUCT")
	// private List<Product> products = new ArrayList<>();

	// @OneToMany(mappedBy = "member")
	// private List<MemberProduct> memberProducts = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	// public Team getTeam() {
	// 	return team;
	// }

}