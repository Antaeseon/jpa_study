//package jpabook.jpashop;
//
//
//import org.springframework.stereotype.Repository;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//
//@Repository
//public class MemberRepository {
//
//    @PersistenceContext
//    private EntityManager em;
//
//    //저장을 하고 나면 리턴값은 웬만해서는 멤버를 안하는게 좋다 (command query 분리)
//    public Long save(Member member) {
//        em.persist(member);
//        return member.getId();
//    }
//
//    public Member find(Long id) {
//        return em.find(Member.class, id);
//    }
//
//}
