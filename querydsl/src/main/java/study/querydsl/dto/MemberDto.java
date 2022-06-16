package study.querydsl.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDto {
	private String username;
	private int age;

	//라이브러리 의존성이 없었지만 querydsl에 의존성을 가지게 되는 문제점이 있다...
	//querydsl 라이브러리를 빼면 문제 발생 가능성 존재
	//여러 layer에 걸쳐서 돌아다니게 되는 문제점 발생

	@QueryProjection
	public MemberDto(String username, int age) {
		this.username = username;
		this.age = age;
	}

}
