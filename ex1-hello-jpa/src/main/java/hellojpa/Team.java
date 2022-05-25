package hellojpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.BatchSize;

import com.sun.xml.internal.rngom.parse.host.Base;

@Entity
public class Team extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "TEAM_ID")
	private Long id;

	private String name;

	// @OneToMany(cascade = CascadeType.ALL) // 팀으로 매핑이 되어 있다는 것을 알린다.
	// @JoinColumn(name = "TEAM_ID") // 주인이 되는 형식인데 잘 사용하지 않는다.
	@OneToMany(mappedBy = "team")
	private List<Member> members = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}

	// public void addMember(Member member) {
	// 	member.setTeam(this);
	// 	members.add(member);
	//
	// }
	//
	// @Override
	// public String toString() {
	// 	return "Team{" +
	// 		"id=" + id +
	// 		", name='" + name + '\'' +
	// 		", members=" + members +
	// 		'}';
	// }
}
