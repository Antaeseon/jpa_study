package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.Hibernate;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        //단순 데이터 조회는 트랜잭션 없어도 된다
        tx.begin();

        try {


            //비영속
//            Member member = new Member();
//            member.setId(101L);
//            member.setName("HelloJPA");
//
//            //영속 상태
//            System.out.println("=== BEFORE ===");
//            em.persist(member);
//            System.out.println("=== AFTER ===");
//
//            Member findMember = em.find(Member.class, 101L);
//
//            System.out.println("findMember.id = " + findMember.getId());
//            System.out.println("findMember.name = " + findMember.getName());


            /**
             * 같은것을 두번 조회할 때에는 1차캐시에서 가져온다
             */
//            Member findMember1 = em.find(Member.class, 101L);
//            Member findMember2 = em.find(Member.class, 101L);
//
//            System.out.println("result = " + (findMember1 == findMember2));

//            Member member1 = new Member(150L, "A");
//            Member member2 = new Member(160L, "B");
//            em.persist(member1);
//            em.persist(member2);
//
//            System.out.println("===================");
//            tx.commit();


//            Member member = em.find(Member.class, 150L);
//            member.setName("ZZZZZ");
//
//            if (member.getName().equals("ZZZZZ")) {
//                em.persist(member);
//            }
//
//            System.out.println("==========");


//            Member member = new Member(200L, "member200");
//            em.persist(member);
//
//            em.flush();
//
//            System.out.println("===========");

            /**
             * 준영속 상태
             */
//            Member member = em.find(Member.class, 150L);
//            member.setName("AAAAA");
//
//            em.clear(); // 1차 캐시를 삭제해버림
//
//            Member member2 = em.find(Member.class, 150L);
//
//            System.out.println("===========");

/*            Member member1 = new Member();
            member1.setUsername("A");
            Member member2 = new Member();
            member2.setUsername("B");

            Member member3 = new Member();
            member3.setUsername("C");

            System.out.println("====================");
            em.persist(member1);
            em.persist(member2);
            em.persist(member3);
            //s
            System.out.println("member.id : "+ member1.getId() );
            System.out.println("member.id : "+ member2.getId() );
            System.out.println("member.id : "+ member3.getId() );
            System.out.println("====================");*/

            //

            // Team team = new Team();
            // team.setName("TeamA");
            // em.persist(team);
            //
            // Member member = new Member();
            // member.setUsername("member1");
            // member.changeTeam(team);
            // em.persist(member);
            //
            // team.addMember(member);

            // team.getMembers().add(member);

            // em.flush();
            // em.clear();

            // Team findTeam = em.find(Team.class, team.getId());  //1차 캐시
            // List<Member> members = findTeam.getMembers();
            // System.out.println("===========");
            // System.out.println("findTeam = " + findTeam);
            // System.out.println("===========");

            // Member findMember = em.find(Member.class, member.getId());
            //
            // List<Member> members = findMember.getTeam().getMembers();
            //
            // for (Member m : members) {
            //     System.out.println("m = " + m.getUsername());
            // }


            /*
            가장 많이 하는 실수 !! 매우 중요
             */

            // Member member = saveMember(em);
            //
            // Team team = new Team();
            // team.setName("teamA");
            // team.getMembers().add(member);
            // em.persist(team);

            /*
               inheritance test
             */

/*
            Movie movie = new Movie();
            movie.setActor("aaaa");
            movie.setDirector("bbbb");
            movie.setName("바람과 함께 사라지다");
            movie.setPrice(10000);

            em.persist(movie);

            em.flush();
            em.clear();

            Movie findMovie = em.find(Movie.class, movie.getId());
*/

/*
            Member member = new Member();
            member.setUsername("kim");
            member.setCreatedBy("kim");
            member.setCreatedDate(LocalDateTime.now());

            em.persist(member);
*/


            // Team teamA = new Team();
            // teamA.setName("TeamA");
            // em.persist(teamA);
            //
            //
            // Team teamB = new Team();
            // teamB.setName("TeamA");
            // em.persist(teamB);
            //
            //
            // Member member1 = new Member();
            // member1.setUsername("member1");
            // member1.setTeam(teamA);
            // em.persist(member1);
            //
            // Member member2 = new Member();
            // member2.setUsername("member2");
            // member2.setTeam(teamB);
            // em.persist(member2);
            //
            //
            // em.flush();
            // em.clear();
            //
            // // Member m = em.find(Member.class, member1.getId());
            //
            // List<Member> members = em.createQuery("select m from Member m join fetch m.team", Member.class)
            //     .getResultList();



            //SQL : select * from Member;
            //SQL : select from Team where TEAM_ID = xxx


            // System.out.println("refMember = " + m.getTeam().getClass());
            //
            // System.out.println("===================");
            // System.out.println("teamName = " + m.getTeam().getName());
            // System.out.println("===================");

            /*
                CASCADE
             */


            Child child1 = new Child();
            Child child2 = new Child();
            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);
            em.persist(parent);
            // em.persist(child1);
            // em.persist(child2);
            em.flush();
            em.clear();

            Parent findParent = em.find(Parent.class, parent.getId());
            findParent.getChildList().remove(0);

            em.remove(findParent);

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


    private static void printMember(Member member) {
        System.out.println("member.getUsername() = " + member.getUsername());
    }

    private static void printMemberAndTeam(Member member) {
        String username = member.getUsername();
        System.out.println("username = " + username);

        Team team = member.getTeam();
        System.out.println("team = " + team.getName());
    }

    private static Member saveMember(EntityManager em) {
        Member member = new Member();
        member.setUsername("member1");
        em.persist(member);
        return member;
    }
}
