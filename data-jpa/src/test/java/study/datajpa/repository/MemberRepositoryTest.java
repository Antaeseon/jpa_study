package study.datajpa.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;
	@Autowired
	TeamRepository teamRepository;

	@PersistenceContext
	EntityManager em;



	@Test
	public void testMember() {
		System.out.println("memberRepository.getClass() = " + memberRepository.getClass());
		Member member = new Member("memberA");
		Member savedMember = memberRepository.save(member);
		Member findMember = memberRepository.findById(savedMember.getId()).get();

		assertThat(findMember.getId()).isEqualTo(member.getId());
		assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
		assertThat(findMember).isEqualTo(member);

	}

	@Test
	public void basicCRUD() {
		Member member1 = new Member("member1");
		Member member2 = new Member("member2");

		memberRepository.save(member1);
		memberRepository.save(member2);

		//단건 조회 겸
		Member findMember1 = memberRepository.findById(member1.getId()).get();
		Member findMember2 = memberRepository.findById(member2.getId()).get();
		assertThat(findMember1).isEqualTo(member1);
		assertThat(findMember2).isEqualTo(member2);



		// 리스트 조회 검증
		List<Member> all = memberRepository.findAll();
		assertThat(all.size()).isEqualTo(2);

		//카운트 검증
		long count = memberRepository.count();
		assertThat(count).isEqualTo(2);

		//삭제 검증
		memberRepository.delete(member1);
		memberRepository.delete(member2);

		long deletedCount = memberRepository.count();
		assertThat(deletedCount).isEqualTo(0);
	}

	@Test
	public void findByUsernameAndAgeGreaterThan() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("AAA", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

		assertThat(result.get(0).getUsername()).isEqualTo("AAA");
		assertThat(result.get(0).getAge()).isEqualTo(20);
		assertThat(result.size()).isEqualTo(1);
	}

	@Test
	public void findHelloBy() {
		List<Member> helloBy = memberRepository.findTop3HelloBy();
	}

	@Test
	public void testNamedQuery() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> result = memberRepository.findByUsername("AAA");
		Member findMember = result.get(0);
		assertThat(findMember).isEqualTo(m1);

	}

	@Test
	public void testQuery() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> result = memberRepository.findUser("AAA",10);
		assertThat(result.get(0)).isEqualTo(m1);

	}

	@Test
	public void findUsernameList() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<String> usernameList = memberRepository.findUsernameList();
		for (String s : usernameList) {
			System.out.println("s = " + s);
		}
	}

	@Test
	public void findMemberDto() {
		Team team = new Team("teamA");
		teamRepository.save(team);

		Member m1 = new Member("AAA", 10);
		m1.setTeam(team);
		memberRepository.save(m1);

		List<MemberDto> memberDto = memberRepository.findMemberDto();
		for (MemberDto dto : memberDto) {
			System.out.println("dto = " + dto);
		}
	}


	@Test
	public void findByNames() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));

		for (Member member : result) {
			System.out.println("member = " + member);
		}
	}

	@Test
	public void returnType() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		// Member aaa = memberRepository.findMemberByUsername("AAA");

		Optional<Member> aaa = memberRepository.findOptionalByUsername("AAA");
		System.out.println("aaa = " + aaa);
	}


	@Test
	public void paging() {

		//given
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 10));
		memberRepository.save(new Member("member3", 10));
		memberRepository.save(new Member("member4", 10));
		memberRepository.save(new Member("member5", 10));

		int age=10;
		//부모 인터페이스에 Pageable 존재
		PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

		//offset이 0이면 굳이 쿼리로 시작 지점이 나가지 않는다!

		//when
		//totalQuery까지 알아서 다 날려준다!!
		//totalElements를 그래서 얻는 것이 가능하다.
		// Page<Member> page = memberRepository.findByAge(age, pageRequest);

		//Slice는 total을 가져오지 않는다.
		//Slice는 Page에서 카운트 쿼리에 많은 비용이 발생하는 경우에 Slice를 사용
		Page<Member> page = memberRepository.findByAge(age, pageRequest);

		//쉽게 변환
		//API로 바로 반환하는 것이 좋은 점 있다!!
		Page<MemberDto> toMap = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));


		//then
		List<Member> content = page.getContent();
		// long totalElements = page.getTotalElements();


		assertThat(content.size()).isEqualTo(3);
		assertThat(page.getTotalElements()).isEqualTo(5);
		assertThat(page.getNumber()).isEqualTo(0);
		assertThat(page.getTotalPages()).isEqualTo(2);
		assertThat(page.isFirst()).isTrue();
		assertThat(page.hasNext()).isTrue();
	}

	/*
	bulkUpdate는 조심할 필요!
	영속성 컨텍스트를 무시하고 바로 db에 쿼리를 날리기 때문에 위험하다
	 */
	@Test
	public void bulkUpdate() {
		//given
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 19));
		memberRepository.save(new Member("member3", 20));
		memberRepository.save(new Member("member4", 21));
		memberRepository.save(new Member("member5", 40));

		//when
		int resultCount = memberRepository.bulkAgePlus(20);
		//@Modifying(clearAutomatically = true) 있으면 안해줘도 된다!!
		// em.clear();



		List<Member> result = memberRepository.findByUsername("member5");
		Member findMember = result.get(0);
		findMember.setAge(findMember.getAge()-1);
		System.out.println("findMember = " + findMember);
		//then
		assertThat(resultCount).isEqualTo(3);

	}

	@Test
	public void findMemberLazy() {
		//given
		//member1 -> teamA
		//member2 -> teamB

		Team teamA = new Team("teamA");
		Team teamB = new Team("teamB");
		teamRepository.save(teamA);
		teamRepository.save(teamB);

		Member member1 = new Member("member1", 10, teamA);
		Member member2 = new Member("member2", 10, teamB);

		memberRepository.save(member1);
		memberRepository.save(member2);

		em.flush();
		em.clear();

		//when N + 1
		//select Member 1
		// List<Member> members = memberRepository.findMemberFetchJoin();

		List<Member> members = memberRepository.findEntityGraphByUsername("member1");

		for (Member member : members) {
			System.out.println("member = " + member.getUsername());
			System.out.println("member.teamClass = " + member.getTeam().getClass()); //class study.datajpa.entity.Team$HibernateProxy$ChbRj7ru
			System.out.println("member.team = " + member.getTeam().getName());
		}
	}

	@Test
	public void queryHint() {
		Member member1 = new Member("member1", 10);
		memberRepository.save(member1);
		em.flush();
		em.clear();

		//when
		// Member findMember = memberRepository.findById(member1.getId()).get();
		Member findMember = memberRepository.findReadOnlyByUsername("member1"); //read only는 변경 감지를 하지 안흔다.
		findMember.setUsername("member2"); //변화 없다 ( query 생성되지 않음 )

		em.flush();
	}

	@Test
	public void lock() {
		Member member1 = new Member("member1", 10);
		memberRepository.save(member1);
		em.flush();
		em.clear();


		//for update문이 붙게 된다.

		//when
		// Member findMember = memberRepository.findById(member1.getId()).get();

		List<Member> result = memberRepository.findLockByUsername("member1");
	}



	@Test
	public void callCustom() {
		List<Member> result = memberRepository.findMemberCustom();
	}

	@Test
	public void specBasic() {
		//given
		Team team = new Team("teamA");
		em.persist(team);

		Member m1 = new Member("m1", 0, team);
		Member m2 = new Member("m2", 0, team);

		em.persist(m1);
		em.persist(m2);

		em.flush();
		em.clear();

		//when
		Specification<Member> spec = MemberSpec.username("m1").and(MemberSpec.teamName("team"));
		List<Member> result = memberRepository.findAll(spec);

		Assertions.assertThat(result.size()).isEqualTo(1);
	}

	@Test
	public void queryByExample() {
		//given
		// Team team = new Team("teamA");
		// em.persist(team);


/*
		Member m1 = new Member("m1", 0, team);
		Member m2 = new Member("m2", 0, team);

		em.persist(m1);
		em.persist(m2);

		em.flush();
		em.clear();
*/

		//when
		//Probe
		Member member = new Member("m1");
		Team team = new Team("teamA");
		member.setTeam(team);
		ExampleMatcher matcher = ExampleMatcher.matching()
			.withIgnorePaths("age");

		Example<Member> example = Example.of(member,matcher);



		List<Member> result = memberRepository.findAll(example);

		Assertions.assertThat(result.get(0).getUsername()).isEqualTo("m1");

	}

	@Test
	public void projections() {
		//given
		Team team = new Team("teamA");
		em.persist(team);


		Member m1 = new Member("m1", 0, team);
		Member m2 = new Member("m2", 0, team);

		em.persist(m1);
		em.persist(m2);

		em.flush();
		em.clear();

		//when
		List<NestedClosedProjections> result = memberRepository.findProjectionsByUsername("m1",NestedClosedProjections.class);

		for (NestedClosedProjections nestedClosedProjections : result) {
			String username = nestedClosedProjections.getUsername();
			System.out.println("username = " + username);
			String teamname = nestedClosedProjections.getTeam().getName();
			System.out.println("team = " + teamname);
		}
	}

	@Test
	public void nativeQuery(){
		//given
		Team team = new Team("teamA");
		em.persist(team);


		Member m1 = new Member("m1", 0, team);
		Member m2 = new Member("m2", 0, team);

		em.persist(m1);
		em.persist(m2);

		em.flush();
		em.clear();

		//when
		Member result = memberRepository.findByNativeQuery("m1");
		System.out.println("result = " + result);

	}

	@Test
	public void nativeQueryProjection(){
		//given
		Team team = new Team("teamA");
		em.persist(team);


		Member m1 = new Member("m1", 0, team);
		Member m2 = new Member("m2", 0, team);

		em.persist(m1);
		em.persist(m2);

		em.flush();
		em.clear();

		//when
		Page<MemberProjection> result = memberRepository.findByNativeProjection(PageRequest.of(0, 10));
		List<MemberProjection> content = result.getContent();
		for (MemberProjection memberProjection : content) {
			System.out.println("memberProjection.getUsername() = " + memberProjection.getUsername());
			System.out.println("memberProjection = " + memberProjection.getTeamname());
		}

	}

}