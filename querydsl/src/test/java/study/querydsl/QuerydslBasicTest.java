package study.querydsl;

import static com.querydsl.jpa.JPAExpressions.*;
import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam;
import study.querydsl.entity.Team;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

	@Autowired
	EntityManager em;

	JPAQueryFactory queryFactory;

	@BeforeEach
	public void before() {
		//여러 멀티스레드에서 와도 문제 X
		queryFactory = new JPAQueryFactory(em);
		Team teamA = new Team("teamA");
		Team teamB = new Team("teamB");
		em.persist(teamA);
		em.persist(teamB);

		Member member1 = new Member("member1", 10, teamA);
		Member member2 = new Member("member2", 20, teamA);
		Member member3 = new Member("member3", 30, teamB);
		Member member4 = new Member("member4", 40, teamB);
		em.persist(member1);
		em.persist(member2);
		em.persist(member3);
		em.persist(member4);

	}

	@Test
	public void startJPQL() {
		//member1을 찾아라
		String qlString = "select m from Member m "
			+ "where m.username = :username";
		Member findMember = em.createQuery(qlString, Member.class)
			.setParameter("username", "member1")
			.getSingleResult();

		assertThat(findMember.getUsername()).isEqualTo("member1");


	}

	@Test
	public void startQuerydsl() {

		//같은 테이블을 조인할 경우만 이런식으로 선언해서 사용
		// QMember m = new QMember("m");
		// QMember m = QMember.member;


		//static member로 변경 => 이 방식을 권장
		//jpql의 alias는 member1으로 나감
		Member findMember = queryFactory
			.select(member)
			.from(member)
			.where(member.username.eq("member1")) //파라미터 바인딩 처리
			.fetchOne();
		assertThat(findMember.getUsername()).isEqualTo("member1");

	}

	@Test
	public void search() {
		Member findMember = queryFactory
			.selectFrom(member)
			.where(member.username.eq("member1")
				.and(member.age.between(10,30)))
			.fetchOne();

		assertThat(findMember.getUsername()).isEqualTo("member1");

	}

	@Test
	public void searchAndParam() {
		Member findMember = queryFactory
			.selectFrom(member)
			.where(
				member.username.eq("member1"), // ,로 처리하는 것도 and 처리가 된다 => and만 있을 때는 이게 깔끔
				(member.age.between(10,30))
			)
			.fetchOne();

		assertThat(findMember.getUsername()).isEqualTo("member1");

	}

	@Test
	public void resultFetch() {
		// List<Member> fetch = queryFactory
		// 	.selectFrom(member)
		// 	.fetch();
		//
		// Member fetchOne = queryFactory
		// 	.selectFrom(QMember.member)
		// 	.fetchOne();
		//
		// Member fetchFirs = queryFactory
		// 	.selectFrom(QMember.member)
		// 	.fetchFirst();

		// QueryResults<Member> results = queryFactory
		// 	.selectFrom(member)
		// 	.fetchResults();
		//
		// results.getTotal();
		// List<Member> content = results.getResults();

		long total = queryFactory
			.selectFrom(member)
			.fetchCount();
	}

	/**
	 * 회원 정렬 순서
	 * 1. 회원 나이 내림차순(desc)
	 * 2. 회원 이름 올림차순(asc)
	 * 단 2에서 회원 이름이 없으면 마지막에 출력 (nulls last)
	 */
	@Test
	public void sort() {
		em.persist(new Member(null,100));
		em.persist(new Member("member5",100));
		em.persist(new Member("member6",100));

		List<Member> result = queryFactory
			.selectFrom(member)
			.where(member.age.eq(100))
			.orderBy(member.age.desc(), member.username.asc().nullsLast())
			.fetch();

		Member member5 = result.get(0);
		Member member6 = result.get(1);
		Member memberNull = result.get(2);
		assertThat(member5.getUsername()).isEqualTo("member5");
		assertThat(member6.getUsername()).isEqualTo("member6");
		assertThat(memberNull.getUsername()).isNull();

	}

	@Test
	public void paging1() {
		List<Member> result = queryFactory
			.selectFrom(member)
			.orderBy(member.username.desc())
			.offset(1)
			.limit(2)
			.fetch();

		assertThat(result.size()).isEqualTo(2);
	}


	@Test
	public void paging2() {

		//count를 따로 분리해서 사용해야 될 수 있다!
		//paging 단순하면 할 수 있지만 쿼리 count랑 따로 작성하는게 날 수 있다
		QueryResults<Member> queryResults = queryFactory
			.selectFrom(member)
			.orderBy(member.username.desc())
			.offset(1)
			.limit(2)
			.fetchResults();
		assertThat(queryResults.getTotal()).isEqualTo(4);
		assertThat(queryResults.getLimit()).isEqualTo(2);
		assertThat(queryResults.getOffset()).isEqualTo(1);
		assertThat(queryResults.getResults().size()).isEqualTo(2);
	}

	@Test
	public void aggregation() {
		List<Tuple> result = queryFactory
			.select(
				member.count(),
				member.age.sum(),
				member.age.avg(),
				member.age.max(),
				member.age.min()
			)
			.from(member)
			.fetch();

		//실무에서는 주로 dto로 사용
		Tuple tuple = result.get(0);
		assertThat(tuple.get(member.count())).isEqualTo(4);
		assertThat(tuple.get(member.age.sum())).isEqualTo(100);
		assertThat(tuple.get(member.age.avg())).isEqualTo(25);
		assertThat(tuple.get(member.age.max())).isEqualTo(40);
		assertThat(tuple.get(member.age.min())).isEqualTo(10);

	}

	/**
	 * 팀의 이름과 각 팀의 평균 연령을 구해라
	 * @throws Exception
	 */
	@Test
	public void group() throws Exception {
		List<Tuple> result = queryFactory
			.select(team.name, member.age.avg())
			.from(member)
			.join(member.team, team)
			.groupBy(team.name)
			.fetch();

		Tuple teamA = result.get(0);
		Tuple teamB = result.get(1);

		assertThat(teamA.get(team.name)).isEqualTo("teamA");
		assertThat(teamA.get(member.age.avg())).isEqualTo(15);


		assertThat(teamB.get(team.name)).isEqualTo("teamB");
		assertThat(teamB.get(member.age.avg())).isEqualTo(35);

	}

	/**
	 * 팀 A에 소속된 모든 회원
	 */
	@Test
	public void join() {
		List<Member> result = queryFactory
			.selectFrom(member)
			.join(member.team,team)
			.where(team.name.eq("teamA"))
			.fetch();

		assertThat(result)
			.extracting("username")
			.containsExactly("member1", "member2");

	}

	/**
	 * 세타 조인
	 * 회원의 이름이 팀 이름과 같은 회원 조회
	 */
	@Test
	public void theta_join() {
		em.persist(new Member("teamA"));
		em.persist(new Member("teamB"));
		em.persist(new Member("teamC"));

		//서로 연관관계가 없는 테이블에 대해 조인을 할 때 사용
		List<Member> result = queryFactory
			.select(member)
			.from(member, team)
			.where(member.username.eq(team.name))
			.fetch();

		for (Member member1 : result) {
			System.out.println("member1 = " + member1);
		}

		assertThat(result)
			.extracting("username")
			.containsExactly("teamA", "teamB");

	}

	/**
	 * 예) 회원가 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
	 * JPQL: select m, t from Member m left join m.team t on t.name = 'teamA'
	 */
	@Test
	public void join_on_filtering() {
		List<Tuple> result = queryFactory
			.select(member, team)
			.from(member)
			.leftJoin(member.team, team)
			.on(team.name.eq("teamA")) //inner조인이면 사실상 where에서 하는거랑 같다
			// .where(team.name.eq("teamA"))
			.fetch();

		for (Tuple tuple : result) {
			System.out.println("tuple = " + tuple);
		}
	}


	/**
	 * 연관관계가 없는 엔티티 외부 조인
	 * 회원의 이름이 팀 이름과 같은 대상 외부 조인
	 */
	@Test
	public void join_on_no_relation() {
		em.persist(new Member("teamA"));
		em.persist(new Member("teamB"));
		em.persist(new Member("teamC"));

		List<Tuple> result = queryFactory
			.select(member, team)
			.from(member)
			.leftJoin(team).on(member.username.eq(team.name))
			.fetch();

		for (Tuple tuple : result) {
			System.out.println("tuple = " + tuple);
		}
	}


	@PersistenceUnit
	EntityManagerFactory emf;

	@Test
	public void fetchJoinNo() throws Exception{
		em.flush();
		em.clear();

		Member findMember = queryFactory
			.selectFrom(member)
			.where(member.username.eq("member1"))
			.fetchOne();

		boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
		assertThat(loaded).as("페치 조인 미적용").isFalse();
	}


	@Test
	public void fetchJoinUse() throws Exception{
		em.flush();
		em.clear();

		Member findMember = queryFactory
			.selectFrom(member)
			.join(member.team,team).fetchJoin()
			.where(member.username.eq("member1"))
			.fetchOne();

		boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
		assertThat(loaded).as("페치 조인 적용").isTrue();
	}

	/**
	 * 나이가 가장 많은 회원 조회
	 */
	@Test
	public void subQuery() {
		QMember memberSub = new QMember("memberSub");

		List<Member> result = queryFactory
			.selectFrom(member)
			.where(member.age.eq(
				select(memberSub.age.max())
					.from(memberSub)
			))
			.fetch();

		assertThat(result).extracting("age").containsExactly(40);
	}

	/**
	 * 나이가 평균 이상인 회원
	 */
	@Test
	public void subQueryGoe() {
		QMember memberSub = new QMember("memberSub");

		List<Member> result = queryFactory
			.selectFrom(member)
			.where(member.age.goe(
				select(memberSub.age.avg())
					.from(memberSub)
			))
			.fetch();

		assertThat(result).extracting("age").containsExactly(30,40);
	}
	/**
	 * 나이가 평균 이상인 회원
	 */
	@Test
	public void subQueryIn() {
		QMember memberSub = new QMember("memberSub");

		List<Member> result = queryFactory
			.selectFrom(member)
			.where(member.age.in(
				select(memberSub.age)
					.from(memberSub)
					.where(memberSub.age.gt(10))
			))
			.fetch();

		assertThat(result).extracting("age").containsExactly(20, 30, 40);
	}

	@Test
	public void selectSubQuery() {
		QMember memberSub = new QMember("memberSub");

		List<Tuple> result = queryFactory
			.select(member.username,
				select(memberSub.age.avg())
					.from(memberSub))
			.from(member)
			.fetch();

		for (Tuple tuple : result) {
			System.out.println("tuple = " + tuple);
		}
	}

	@Test
	public void basicCase() {
		List<String> result = queryFactory
			.select(member.age
				.when(10).then("열살")
				.when(20).then("스무살")
				.otherwise("기타"))
			.from(member)
			.fetch();

		for (String s : result) {
			System.out.println("s = " + s);
		}
	}

	@Test
	public void complexCase() {
		List<String> result = queryFactory
			.select(new CaseBuilder()
				.when(member.age.between(10, 20)).then("0~20살")
				.when(member.age.between(21, 30)).then("21~30살")
				.otherwise("기타"))
			.from(member)
			.fetch();

		for (String s : result) {
			System.out.println("s = " + s);
		}
	}

	@Test
	public void constant() {
		List<Tuple> result = queryFactory
			.select(member.username, Expressions.constant("A"))
			.from(member)
			.fetch();

		for (Tuple tuple : result) {
			System.out.println("tuple = " + tuple);
		}
	}

	@Test
	public void concat() {
		//{username}_{age}
		List<String> result = queryFactory
			.select(member.username.concat("_").concat(member.age.stringValue()))
			.from(member)
			.where(member.username.eq("member1"))
			.fetch();

		for (String s : result) {
			System.out.println("s = " + s);
		}
	}

	@Test
	public void simpleProjection() throws Exception {
		List<String> result = queryFactory
			.select(member.username)
			.from(member)
			.fetch();
		for (String s : result) {
			System.out.println("s = " + s);
		}
	}


	@Test
	public void tupleProjection() throws Exception {

		//tuple은 repository정도에서만 사용하고 밖으로 던지는 것은 사용하지 말아야 한다.
		//밖으로 나갈 떼는 Dto로 변환 되도록!!!
		List<Tuple> result = queryFactory
			.select(member.username, member.age)
			.from(member)
			.fetch();

		for (Tuple tuple : result) {
			String username = tuple.get(member.username);
			Integer age = tuple.get(member.age);
			System.out.println("username = " + username);
			System.out.println("age = " + age);
		}
	}


}