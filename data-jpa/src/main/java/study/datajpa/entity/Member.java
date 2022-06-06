package study.datajpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Member {

	@Id @GeneratedValue
	private Long id;
	private String username;


	//Entity는 반드시 default 생성자가 하나 있어야 한다. // 최소 protected
	// Proxy 기술을 쓸 때 private은 접근 불가  => JPA 스펙에 명시
	protected Member() {
	}

	public Member(String username) {
		this.username = username;
	}
}
