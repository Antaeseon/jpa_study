package hellojpa;

public class ValueMain {

	public static void main(String[] args) {

		Integer a = new Integer(10);
		Integer b = a;

		// int a= 10;
		// int b =a;

		// b=20;

		b=20;

		System.out.println(a.getClass()==b.getClass());

		System.out.println("a = " + a);
		System.out.println("b = " + b);
	}
}
