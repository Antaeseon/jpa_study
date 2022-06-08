package study.datajpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import study.datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member,Long> {

	List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

	List<Member> findTop3HelloBy();

	//없어도 잘 동작
	//네임드쿼리 있는지 먼저 확인하고 그 이후 없으면 자동 쿼리 생성
	//네임드쿼리는 애플리케이션 로딩 시점에 문법 오류를 알려주는 엄청난 장점이 존재
	//em.createquery를 이용하면 이런 문제를 잡지 못한다.
	// @Query(name = "Member.findByUsername")
	List<Member> findByUsername(@Param("username") String username); //namedparameter 넘어갈 때에는 반드시 @Param 설정 필요

}
