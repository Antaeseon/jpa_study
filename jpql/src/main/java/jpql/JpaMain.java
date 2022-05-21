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
			// for (int i = 0; i < 100; i++) {
			// 	Member member = new Member();
			// 	member.setUsername("member" + i);
			// 	member.setAge(i);
			// 	em.persist(member);
			//
			// }

			Team team = new Team();
			team.setName("teamA");
			em.persist(team);

			Member member = new Member();
			member.setUsername("관리자");
			member.setAge(10);
			member.setType(MemberType.ADMIN);

			member.changeTeam(team);

			em.persist(member);

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

			// List<Member> resultList = em.createQuery("select m from Member  m order by m.age desc", Member.class)
			// 	.setFirstResult(1)
			// 	.setMaxResults(10)
			// 	.getResultList();
			//
			// System.out.println("resultList.size() = " + resultList.size());
			// for (Member member1 : resultList) {
			// 	System.out.println("member1 = " + member1);
			// }

			// Member findMember = result.get(0);
			// findMember.setAge(30);

			// String query = "select m from Member m left join m.team t";
			// List<Member> result = em.createQuery(query, Member.class).getResultList();
			// String query = "select m from Member m,Team t where m.username = t.name";
			// List<Member> result = em.createQuery(query, Member.class).getResultList();

			// String query = "select m from Member m left join m.team  t on t.name ='teamA'";
			// List<Member> result = em.createQuery(query, Member.class).getResultList();

			// String query = "select m from Member m join Team t on m.username = t.name";
			// List<Member> result = em.createQuery(query, Member.class).getResultList();
			// String query = "select (select avg(m1.age) from Member m1) as avg  from Member m join Team t on m.username = t.name";
			// List<Member> result = em.createQuery(query, Member.class).getResultList();

/*			String query = "select m.username, 'HELLO',TRUE from Member m "
				+ "where m.type = :userType";
			List<Object[]> result = em.createQuery(query).setParameter("userType",MemberType.ADMIN).getResultList();

			for (Object[] objects : result) {
				System.out.println("objects[0] = " + objects[0]);
				System.out.println("objects[1] = " + objects[1]);
				System.out.println("objects[2] = " + objects[2]);
			}*/

			// String query =
			// 	"select " +
			// 		"case when m.age <=10 then '학생요금'"
			// 		+ " when m.age>=60 then '경로요금'"
			// 		+ " else '일반요금' "
			// 		+ "end "
			// 		+ "from Member m";
			//
			// List<String> result = em.createQuery(query, String.class).getResultList();



			// for (String s : result) {
			// 	System.out.println("s = " + s);
			// }

			// String query = "select coalesce(m.username, '이름 없는 회원') as username from Member m";
			// List<String> result = em.createQuery(query, String.class).getResultList();


			String query = "select nullif(m.username,'관리자') from Member m";
			List<String> result = em.createQuery(query, String.class).getResultList();

			for (String s : result) {
				System.out.println("s = " + s);
			}
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