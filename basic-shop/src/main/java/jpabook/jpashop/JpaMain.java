package jpabook.jpashop;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;

public class JpaMain {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

		EntityManager em = emf.createEntityManager();

		EntityTransaction tx = em.getTransaction();
		//단순 데이터 조회는 트랜잭션 없어도 된다
		tx.begin();

		try {

			Order order = em.find(Order.class, 1L);
			Long memberId = order.getMemberId();

			Member member = em.find(Member.class, memberId);

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}

		//리소스 릴리즈 반드시 필요
		emf.close();

	}
}
