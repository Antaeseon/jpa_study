package study.datajpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","username","age"}) // team까지 적게 되면 무한루프를 돌 수 있기 때문에 이렇게 설정한다.
public class Member {

	@Id @GeneratedValue
	@Column(name = "member_id")
	private Long id;
	private String username;
	private int age;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id")
	private Team team;

	//Entity는 반드시 default 생성자가 하나 있어야 한다. // 최소 protected
	// Proxy 기술을 쓸 때 private은 접근 불가  => JPA 스펙에 명시
	// protected Member() {
	// }

	public Member(String username) {
		this.username = username;
	}

	public Member(String username, int age, Team team) {
		this.username = username;
		this.age=age;
		if (team != null) {
			changeTeam(team);
		}
	}

	public void changeTeam(Team team) {
		this.team = team;
		team.getMembers().add(this);
	}
}
