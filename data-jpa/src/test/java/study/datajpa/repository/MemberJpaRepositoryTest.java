package study.datajpa.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import study.datajpa.entity.Member;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

	@Autowired
	MemberJpaRepository memberJpaRepository;


	@Test
	public void testMember() throws Exception {

		Member member = new Member("usernameA");
		Member savedMember = memberJpaRepository.save(member);

		Member findMember = memberJpaRepository.find(savedMember.getId());

		assertThat(findMember.getId()).isEqualTo(member.getId());
		assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
		assertThat(findMember).isEqualTo(member);
	}


	@Test
	public void basicCRUD() {
		Member member1 = new Member("member1");
		Member member2 = new Member("member2");

		memberJpaRepository.save(member1);
		memberJpaRepository.save(member2);

		//단건 조회 겸
		Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
		Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
		assertThat(findMember1).isEqualTo(member1);
		assertThat(findMember2).isEqualTo(member2);



		// 리스트 조회 검증
		List<Member> all = memberJpaRepository.findAll();
		assertThat(all.size()).isEqualTo(2);

		//카운트 검증
		long count = memberJpaRepository.count();
		assertThat(count).isEqualTo(2);

		//삭제 검증
		memberJpaRepository.delete(member1);
		memberJpaRepository.delete(member2);

		long deletedCount = memberJpaRepository.count();
		assertThat(deletedCount).isEqualTo(0);
	}

	@Test
	public void findByUsernameAndAgeGreaterThan() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("AAA", 20);
		memberJpaRepository.save(m1);
		memberJpaRepository.save(m2);

		List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

		assertThat(result.get(0).getUsername()).isEqualTo("AAA");
		assertThat(result.get(0).getAge()).isEqualTo(20);
		assertThat(result.size()).isEqualTo(1);
	}

	@Test
	public void testNamedQuery() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberJpaRepository.save(m1);
		memberJpaRepository.save(m2);

		List<Member> result = memberJpaRepository.findByUsername("AAA");
		Member findMember = result.get(0);
		assertThat(findMember).isEqualTo(m1);

	}


	@Test
	public void paging() {

		//given
		memberJpaRepository.save(new Member("member1", 10));
		memberJpaRepository.save(new Member("member2", 10));
		memberJpaRepository.save(new Member("member3", 10));
		memberJpaRepository.save(new Member("member4", 10));
		memberJpaRepository.save(new Member("member5", 10));

		int age = 10;
		int offset = 0;
		int limit = 3;

		//offset이 0이면 굳이 쿼리로 시작 지점이 나가지 않는다!

		//when
		List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
		long totalCount = memberJpaRepository.totalCount(age);



		//then
		assertThat(members.size()).isEqualTo(3);
		assertThat(totalCount).isEqualTo(5);
	}

	@Test
	public void bulkUpdate() {
		//given
		memberJpaRepository.save(new Member("member1", 10));
		memberJpaRepository.save(new Member("member2", 19));
		memberJpaRepository.save(new Member("member3", 20));
		memberJpaRepository.save(new Member("member4", 21));
		memberJpaRepository.save(new Member("member5", 40));

		//when
		int resultCount = memberJpaRepository.bulkAgePlus(20);

		//then
		assertThat(resultCount).isEqualTo(3);

	}

}