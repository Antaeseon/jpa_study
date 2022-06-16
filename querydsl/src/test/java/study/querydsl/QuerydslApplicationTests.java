package study.querydsl;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import study.querydsl.dto.MemberDto;
import study.querydsl.entity.Hello;
import study.querydsl.entity.QHello;

@SpringBootTest
@Transactional
// @Rollback(value = false)
class QuerydslApplicationTests {


	@Autowired
	EntityManager em;



	@Test
	void contextLoads() {
		Hello hello = new Hello();
		em.persist(hello);

		JPAQueryFactory query = new JPAQueryFactory(em);
		// QHello qHello = new QHello("h");
		QHello qHello = QHello.hello;

		Hello result = query
			.selectFrom(qHello)
			.fetchOne();

		assertThat(result).isEqualTo(hello);
		assertThat(result.getId()).isEqualTo(hello.getId());

	}

	@Test
	public void findDtoByJPQL() throws Exception {
		List<MemberDto> resultList = em.createQuery(
			"select new study.querydsl.dto.MemberDto(m.username,m.age) from Member m", MemberDto.class)
			.getResultList();
		for (MemberDto memberDto : resultList) {
			System.out.println("memberDto = " + memberDto);
		}
	}



}
