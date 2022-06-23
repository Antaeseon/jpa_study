package study.querydsl.repository;

import static org.springframework.util.StringUtils.*;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.dto.QMemberTeamDto;
import study.querydsl.entity.Member;

public class MemberRepositoryCustomImpl /*extends QuerydslRepositorySupport*/ implements MemberRepositoryCustom {




	private final JPAQueryFactory queryFactory;

	public MemberRepositoryCustomImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	// public MemberRepositoryCustomImpl() {
	// 	super(Member.class);
	// }
	/**
	 * 위의 builder보다 이를 선호
	 */
	public List<MemberTeamDto> search(MemberSearchCondition condition) {

		// EntityManager em = getEntityManager();
		//QuerydslRepositorySupport 이용 쿼리
/*		from(member)
			.leftJoin(member.team,team)
			.where(
				usernameEq(condition.getUsername()),
				teamNameEq(condition.getTeamName()),
				ageGoe(condition.getAgeGoe()),
				ageLoe(condition.getAgeLoe())
			)
			.select(new QMemberTeamDto(
				member.id.as("memberId"),
				member.username,
				member.age,
				team.id.as("teamId"),
				team.name.as("teamName"))
			)
			.fetch();*/



		return queryFactory
			.select(new QMemberTeamDto(
				member.id.as("memberId"),
				member.username,
				member.age,
				team.id.as("teamId"),
				team.name.as("teamName")
			))
			.from(member)
			.leftJoin(member.team, team)
			.where(
				usernameEq(condition.getUsername()),
				teamNameEq(condition.getTeamName()),
				ageGoe(condition.getAgeGoe()),
				ageLoe(condition.getAgeLoe())
			)
			.fetch();

	}

	@Override
	public Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable) {




		QueryResults<MemberTeamDto> results = queryFactory
			.select(new QMemberTeamDto(
				member.id.as("memberId"),
				member.username,
				member.age,
				team.id.as("teamId"),
				team.name.as("teamName")
			))
			.from(member)
			.leftJoin(member.team, team)
			.where(
				usernameEq(condition.getUsername()),
				teamNameEq(condition.getTeamName()),
				ageGoe(condition.getAgeGoe()),
				ageLoe(condition.getAgeLoe())
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetchResults();

		List<MemberTeamDto> content = results.getResults();
		long total = results.getTotal();
		return new PageImpl<>(content, pageable, total);

	}

	// public Page<MemberTeamDto> searchPageSimple2(MemberSearchCondition condition, Pageable pageable) {
	//
	// 	JPQLQuery<MemberTeamDto> jpaQuery = from(member)
	// 		.leftJoin(member.team, team)
	// 		.where(
	// 			usernameEq(condition.getUsername()),
	// 			teamNameEq(condition.getTeamName()),
	// 			ageGoe(condition.getAgeGoe()),
	// 			ageLoe(condition.getAgeLoe())
	// 		)
	// 		.select(new QMemberTeamDto(
	// 			member.id.as("memberId"),
	// 			member.username,
	// 			member.age,
	// 			team.id.as("teamId"),
	// 			team.name.as("teamName")
	// 		));
	//
	// 	/*
	// 		치명적인 단점!
	// 		Sort가 안된다. ....
	// 	 */
	// 	JPQLQuery<MemberTeamDto> query = getQuerydsl().applyPagination(pageable, jpaQuery);
	//
	// 	query.fetch();
	// }



	@Override
	public Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable) {
		List<MemberTeamDto> content = queryFactory
			.select(new QMemberTeamDto(
				member.id.as("memberId"),
				member.username,
				member.age,
				team.id.as("teamId"),
				team.name.as("teamName")
			))
			.from(member)
			.leftJoin(member.team, team)
			.where(
				usernameEq(condition.getUsername()),
				teamNameEq(condition.getTeamName()),
				ageGoe(condition.getAgeGoe()),
				ageLoe(condition.getAgeLoe())
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Member> countQuery = queryFactory
			.select(member)
			.from(member)
			.leftJoin(member.team, team)
			.where(
				usernameEq(condition.getUsername()),
				teamNameEq(condition.getTeamName()),
				ageGoe(condition.getAgeGoe()),
				ageLoe(condition.getAgeLoe())
			);

		// return new PageImpl<>(content, pageable, total);
		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount); // countquery를 호출 안할수도 있다. 조건 만족했을 경우!!
	}

	private BooleanExpression usernameEq(String username) {
		return hasText(username) ? member.username.eq(username) : null;
	}

	private BooleanExpression teamNameEq(String teamName) {
		return hasText(teamName) ? member.team.name.eq(teamName) : null;

	}

	private BooleanExpression ageGoe(Integer ageGoe) {
		return ageGoe != null ? member.age.goe(ageGoe) : null;
	}

	private BooleanExpression ageLoe(Integer ageLoe) {
		return ageLoe != null ? member.age.loe(ageLoe) : null;

	}

}
