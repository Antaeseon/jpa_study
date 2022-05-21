package jpql;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class JpaMain {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

		EntityManager em = emf.createEntityManager();

		EntityTransaction tx = em.getTransaction();
		//단순 데이터 조회는 트랜잭션 없어도 된다
		tx.begin();

		try {
			for (int i = 0; i < 100; i++) {
				Member member = new Member();
				member.setUsername("member" + i);
				member.setAge(i);
				em.persist(member);

			}

			em.flush();
			em.clear();

			// List<Address> result = em.createQuery("select o.address from Order o", Address.class)
			// 	.getResultList();

			// List resultList = em.createQuery("select distinct m.username, m.age from Member m")
			// 	.getResultList();
			//
			// Object o = resultList.get(0);
			// Object[] result = (Object[])o;
			// System.out.println("result = " + result[0]);
			//
			// List<Object[]> resultList = em.createQuery("select distinct m.username, m.age from Member m")
			// 	.getResultList();
			//
			// Object[] result = resultList.get(0);
			// System.out.println("result = " + result[0]);
			// System.out.println("result = " + result[1]);



			// List<MemberDTO> result = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m",MemberDTO.class)
			// 	.getResultList();
			//
			// MemberDTO memberDTO = result.get(0);
			// System.out.println(memberDTO.getAge());

			List<Member> resultList = em.createQuery("select m from Member  m order by m.age desc", Member.class)
				.setFirstResult(1)
				.setMaxResults(10)
				.getResultList();

			System.out.println("resultList.size() = " + resultList.size());
			for (Member member1 : resultList) {
				System.out.println("member1 = " + member1);
			}

			// Member findMember = result.get(0);
			// findMember.setAge(30);

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}

		//리소스 릴리즈 반드시 필요
		emf.close();

	}
}