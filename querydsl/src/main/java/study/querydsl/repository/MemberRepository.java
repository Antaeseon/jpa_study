package study.querydsl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import study.querydsl.entity.Member;

/*
Entity 검색이나 핵심 비지니스로직은 Member Repository!
다른거는 그냥 MemberQueryRepository같은 것을 class로 만들어서 사용하는 것도 하나의 방법이다!!
 */
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom,
	QuerydslPredicateExecutor<Member> {

	List<Member> findByUsername(String username);
}
