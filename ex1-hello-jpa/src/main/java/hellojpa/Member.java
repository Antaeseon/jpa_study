package hellojpa;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
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
import javax.persistence.NamedQuery;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.Lock;

@Entity
@NamedQuery(
	name = "Member.findByUsername",
	query = "select m from Member m where m.username = :username"
)
public class Member extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "MEMBER_ID")
	private Long id;

	@Column(name = "USER_NAME")
	private String username;

	// @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩 이용 프록시
	// // @ManyToOne(fetch = FetchType.EAGER) // 즉시 로딩 ( 실무에서는 사용하면 안된다 )
	// // @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false) // 읽기 전용으로 처리를 해버린다.
	// @JoinColumn(name = "TEAM_ID")
	// private Team team;

	@OneToOne(mappedBy = "member",fetch = FetchType.LAZY)
	// @JoinColumn(name = "LOCKER_ID")
	private Locker locker;

	private int age;

	//기간 Period
	@Embedded
	private Period workPeriod;
	//주소
	@Embedded
	private Address homeAddress;

	// @ElementCollection
	// @CollectionTable(name = "FAVORITE_FOOD",joinColumns =
	// 	@JoinColumn(name = "MEMBER_ID")
	// )
	// @Column(name = "FOOD_NAME")
	// private Set<String> favoriteFoods = new HashSet<>();



	// @ElementCollection
	// @CollectionTable(name = "ADDRESS",
	// 	joinColumns = @JoinColumn(name = "MEMBER_ID")
	// )
	// private List<Address> addressHistory = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "MEMBER_ID")
	private List<AddressEntity> addressHistory = new ArrayList<>();

	// @Embedded
	// @AttributeOverrides({
	// 	@AttributeOverride(name = "city",
	// 		column = @Column(name = "WORK_CITY")),
	// 	@AttributeOverride(name = "street",
	// 		column = @Column(name = "WORK_STREET")),
	// 	@AttributeOverride(name = "zipcode",
	// 		column = @Column(name = "WORK_ZIPCODE"))
	// })
	// private Address workAddress;

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
	//
	// public void setTeam(Team team) {
	// 	this.team = team;
	//
	// 	if(!team.getMembers().contains(this)){
	// 		team.getMembers().add(this);
	// 	}
	// }

	public Period getWorkPeriod() {
		return workPeriod;
	}

	public void setWorkPeriod(Period workPeriod) {
		this.workPeriod = workPeriod;
	}

	public Address getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(Address homeAddress) {
		this.homeAddress = homeAddress;
	}

	// public Set<String> getFavoriteFoods() {
	// 	return favoriteFoods;
	// }
	//
	// public void setFavoriteFoods(Set<String> favoriteFoods) {
	// 	this.favoriteFoods = favoriteFoods;
	// }

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	// public Address getHomeAddress() {
	// 	return homeAddress;
	// }
	//
	// public void setHomeAddress(Address homeAddress) {
	// 	this.homeAddress = homeAddress;
	// }

	// public Team getTeam() {
	// 	return team;
	// }

	public List<AddressEntity> getAddressHistory() {
		return addressHistory;
	}

	public void setAddressHistory(List<AddressEntity> addressHistory) {
		this.addressHistory = addressHistory;
	}

	@Override
	public String toString() {
		return "Member{" +
			"id=" + id +
			", username='" + username + '\'' +
			// ", team=" + team +
			", locker=" + locker +
			", age=" + age +
			", workPeriod=" + workPeriod +
			", homeAddress=" + homeAddress +
			", addressHistory=" + addressHistory +
			'}';
	}
}