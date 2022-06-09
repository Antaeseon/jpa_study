package study.datajpa.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import study.datajpa.dto.MemberDto;
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


	//로딩 시점에 오류가 발생하는 엄청난 장점!!!
	@Query("select m from Member m where m.username = :username and m.age = :age")
	List<Member> findUser(@Param("username") String username, @Param("age") int age);

	@Query("select m.username From Member m")
	List<String> findUsernameList();

	@Query("select new study.datajpa.dto.MemberDto(m.id,m.username,t.name) from Member m join m.team t")
	List<MemberDto> findMemberDto();

	@Query("select m from Member m where m.username in :names")
	List<Member> findByNames(@Param("names") Collection<String> names);

	List<Member> findListByUsername(String username);
	Member findMemberByUsername(String username);
	Optional<Member> findOptionalByUsername(String username);

	// @Query(value = "select m from Member m left join m.team t",countQuery = "select count(m.username) from Member m")
	Page<Member> findByAge(int age, Pageable pageable);

	@Modifying(clearAutomatically = true) //반드시 넣어 주어야 한다!! executeUpdate를 하는 역할
	@Query(value = "update Member m set m.age = m.age +1 where m.age>= :age")
	int bulkAgePlus(@Param("age") int age);


}
