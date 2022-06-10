package study.datajpa.repository;

import java.util.List;

import javax.persistence.EntityManager;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

/**
 * MemberRepositoryImpl <- 이름을 반드시 맞추어 주어야 한다!!
 * MemberRepositoryCustom는 상관 없다
 */
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

	private final EntityManager em;

	@Override
	public List<Member> findMemberCustom() {
		return em.createQuery("select m from Member m",Member.class)
			.getResultList();
	}
}
