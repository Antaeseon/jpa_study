package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        //단순 데이터 조회는 트랜잭션 없어도 된다
        tx.begin();

        try {


            //비영속
            Member member = new Member();
            member.setId(100L);
            member.setName("HelloJPA");

            //영속 상태
            System.out.println("=== BEFORE ===");
            em.persist(member);
            em.detach(member);
            System.out.println("=== AFTER ===");

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
