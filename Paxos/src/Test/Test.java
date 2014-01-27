package Test;

import Paxos.*;

public class Test {
	public static void main(String[] inputs) {
		System.out.println("If a value is selected, you get a message: Value selected by Learner <Learner ID> and Value <value  learned>");
		
		
		Test t = new Test();

		try {
			Thread.sleep(500);
			
			System.out.println("\nTEST CASE 1");
			t.testCase4();
			System.out.println("\nTEST CASE 2");
			t.testCase5();
			System.out.println("\nTEST CASE 3");
			t.testCase6();
			System.out.println("\nTEST CASE 4");
			t.testCase20();
			System.out.println("\nTEST CASE 5");
			t.testCase21();
			System.out.println("\nTEST CASE 6");
			t.testCase19();
			System.out.println("\nTEST CASE 7");
			t.testCase17();
			System.out.println("\nTEST CASE 8");
			t.testCase16();
			System.out.println("\nTEST CASE 9");
			t.testCase7();
			System.out.println("\nTEST CASE 10");
			t.testCase10();
			System.out.println("\nTEST CASE 11");
			t.testCase11();
			System.out.println("\nTEST CASE 12");
			t.testCase2();
			System.out.println("\nTEST CASE 13");
			t.testCase8();
			System.out.println("\nTEST CASE 14");
			t.testCase14();
			System.out.println("\nTEST CASE 15");
			t.testCase13();
			System.out.println("\nTEST CASE 16");
			t.testCase3();
			System.out.println("\nTEST CASE 17");
			t.testCase12();
			System.out.println("\nTEST CASE 18");
			t.testCase9();					
			System.out.println("\nTEST CASE 19");
			t.testCase15();
			
		
		

			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	void testCase1() throws InterruptedException {
		TestChannel.testCase = 1;
		TestChannel.flag = true;
		System.out.println("*************Original Test Case by the Professor*******************");
		TestNetwork n1=new TestNetwork(3, 3, 3);
		Paxos p1=new Paxos(n1);
		p1.runPaxos();
		Thread.sleep(10000);
		System.out.println("*************END OF TEST CASE*******************");
		TestChannel.flag = false;
		Thread.sleep(2000);
	}

	void testCase2() throws InterruptedException {
		TestChannel.testCase = 2;
		System.out.println("*************Acceptor sends duplicate messages*******************");
		TestChannel.flag = true;
		TestNetwork n2=new TestNetwork(3, 5, 3);
		Paxos p2=new Paxos(n2);
		p2.runPaxos();
		Thread.sleep(10000);
		System.out.println("*************END OF TEST CASE*******************");
		TestChannel.flag = false;
		Thread.sleep(2000);
	}

	void testCase3() throws InterruptedException {
		TestChannel.testCase = 3;
		System.out.println("*************Acceptor messages gets randomly lost*******************");
		TestChannel.flag = true;
		TestNetwork n3=new TestNetwork(6, 5, 3);
		Paxos p3=new Paxos(n3);
		p3.runPaxos();
		Thread.sleep(10000);
		System.out.println("*************END OF TEST CASE*******************");
		TestChannel.flag = false;
		Thread.sleep(2000);
	}

	void testCase4() throws InterruptedException {
		//Hard Case
		TestChannel.testCase = 4;
		System.out.println("*************Distinguished Proposer changes very rapidly and randomly*******************");
		TestChannel.flag = true;
		TestNetwork n4=new TestNetwork(7, 3, 3);
		Paxos p4=new Paxos(n4);
		p4.runPaxos();
		Thread.sleep(10000);
		System.out.println("*************END OF TEST CASE*******************");
		TestChannel.flag = false;
		Thread.sleep(2000);
	}

	void testCase5() throws InterruptedException {
		TestChannel.time = System.currentTimeMillis();
		TestChannel.testCase = 5;
		System.out.println("*************Distinguished Proposer changes randomly*******************");
		TestChannel.flag = true;
		TestNetwork n5=new TestNetwork(5, 4, 3);
		Paxos p5=new Paxos(n5);
		p5.runPaxos();
		Thread.sleep(10000);
		System.out.println("*************END OF TEST CASE*******************");
		TestChannel.flag = false;		
		Thread.sleep(2000);
	}

	void testCase6() throws InterruptedException {
		TestChannel.testCase = 6;
		System.out.println("*************Multiple Distinguished Proposers*******************");
		TestChannel.flag = true;
		TestNetwork n6=new TestNetwork(7, 5, 3);
		Paxos p6=new Paxos(n6);
		p6.runPaxos();
		Thread.sleep(10000);
		System.out.println("*************END OF TEST CASE*******************");
		TestChannel.flag = false;			
		Thread.sleep(2000);
	}

	void testCase7() throws InterruptedException {
		System.out.println("*************Duplicate Messages*******************");
		//1. Proposer Sending Duplicate.
		TestChannel.flag = true;
		TestChannel.testCase = 7;
		TestNetwork n7=new TestNetwork(6, 6, 6);
		Paxos p7=new Paxos(n7);
		p7.runPaxos();
		Thread.sleep(10000);
		TestChannel.flag = false;
		System.out.println("*************END OF TEST CASE*******************");
		Thread.sleep(2000);
	}

	void testCase8() throws InterruptedException {
		System.out.println("*************Proposer Dropping Messages*******************");
		TestChannel.testCase = 8;
		TestChannel.flag = true;
		TestNetwork n8=new TestNetwork(2, 5, 3);
		Paxos p8=new Paxos(n8);
		p8.runPaxos();
		Thread.sleep(5000);
		TestChannel.flag = false;
		System.out.println("*************END OF TEST CASE*******************");
		Thread.sleep(2000);
	}

	void testCase9() throws InterruptedException {
		System.out.println("*************Proposer Dropping All Messages*******************");
		TestChannel.testCase = 9;
		TestChannel.flag = true;
		TestNetwork n9=new TestNetwork(6, 3, 3);
		Paxos p9=new Paxos(n9);
		p9.runPaxos();
		Thread.sleep(5000);
		TestChannel.flag = false;
		System.out.println("*************END OF TEST CASE*******************");
		Thread.sleep(2000);
	}

	void testCase10() throws InterruptedException {
		System.out.println("*************Acceptors Message to Learners get Dropped and so Learners are not able to find out if some decision has been reached*******************");
		TestChannel.testCase = 10;
		TestChannel.flag = true;
		TestNetwork n10=new TestNetwork(3, 7, 3);
		Paxos p10=new Paxos(n10);
		p10.runPaxos();
		Thread.sleep(5000);
		TestChannel.flag = false;
		System.out.println("*************END OF TEST CASE*******************");
		Thread.sleep(2000);
	}

	void testCase11() throws InterruptedException {
		TestChannel.time = System.currentTimeMillis();
		System.out.println("*************All acceptors stop for first 500 ms*******************");
		TestChannel.testCase = 11;
		TestChannel.flag = true;
		TestNetwork n11=new TestNetwork(3, 7, 3);
		Paxos p11=new Paxos(n11);
		p11.runPaxos();
		Thread.sleep(10000);
		TestChannel.flag = false;
		System.out.println("*************END OF TEST CASE*******************");
		Thread.sleep(2000);
	}

	void testCase12() throws InterruptedException {
		System.out.println("*************Reordering of messages by Acceptor*******************");
		TestChannel.testCase = 12;
		TestChannel.flag = true;
		TestNetwork n12=new TestNetwork(3, 7, 3);
		Paxos p12 = new Paxos(n12);
		p12.runPaxos();
		Thread.sleep(5000);
		TestChannel.flag = false;
		System.out.println("*************END OF TEST CASE*******************");
		Thread.sleep(2000);
	}

	void testCase13() throws InterruptedException {
		System.out.println("*************Exact majority of acceptors******************");
		TestChannel.testCase = 13;
		TestChannel.flag = true;
		TestNetwork n13=new TestNetwork(3, 8, 3);
		Paxos p13=new Paxos(n13);
		p13.runPaxos();
		Thread.sleep(5000);
		TestChannel.flag = false;
		System.out.println("*************END OF TEST CASE*******************");
		Thread.sleep(2000);
	}

	void testCase14() throws InterruptedException {
		System.out.println("*************Only one acceptor working******************");
		TestChannel.testCase = 14;
		TestChannel.flag = true;
		TestNetwork n14=new TestNetwork(3, 3, 3);
		Paxos p14=new Paxos(n14);
		p14.runPaxos();
		Thread.sleep(7000);
		TestChannel.flag = false;
		System.out.println("*************END OF TEST CASE*******************");
		Thread.sleep(2000);
	}

	void testCase15() throws InterruptedException {
		System.out.println("*************Reordering of messages by Proposer*******************");
		TestChannel.testCase = 15;
		TestChannel.flag = true;
		TestNetwork n15=new TestNetwork(5, 3, 3);
		Paxos p15=new Paxos(n15);
		p15.runPaxos();
		Thread.sleep(6000);
		TestChannel.flag = false;
		System.out.println("*************END OF TEST CASE*******************");
		Thread.sleep(2000);
	}

	void testCase16() throws InterruptedException {
		System.out.println("*************Reordering of messages by All*******************");
		TestChannel.testCase = 16;
		TestChannel.flag = true;
		TestNetwork n16=new TestNetwork(5, 5, 5);
		Paxos p16=new Paxos(n16);
		p16.runPaxos();
		Thread.sleep(5000);
		TestChannel.flag = false;
		System.out.println("*************END OF TEST CASE*******************");
		Thread.sleep(2000);
	}

	void testCase17() throws InterruptedException {
		System.out.println("*************One acceptor and one learner working******************");
		TestChannel.testCase = 17;
		TestChannel.flag = true;
		TestNetwork n17=new TestNetwork(3, 3, 3);
		Paxos p17=new Paxos(n17);
		p17.runPaxos();
		Thread.sleep(10000);
		TestChannel.flag = false;		
		System.out.println("*************END OF TEST CASE*******************");
		Thread.sleep(2000);
	}

	void testCase19() throws InterruptedException {
		System.out.println("*************All acceptor messages to learners are dropped. In this case learner should contact proposer to get the decision******************");
		TestChannel.testCase = 19;
		TestChannel.flag = true;
		TestNetwork n19=new TestNetwork(5, 5, 5);
		Paxos p19=new Paxos(n19);
		p19.runPaxos();
		Thread.sleep(10000);
		TestChannel.flag = false;
		System.out.println("*************END OF TEST CASE*******************");
		Thread.sleep(2000);
	}

	void testCase20() throws InterruptedException {
		TestChannel.time = System.currentTimeMillis();
		System.out.println("*************Multiple Distinguished proposers + All acceptor messages to learners are dropped******************");
		TestChannel.testCase = 20;
		TestChannel.flag = true;
		TestNetwork n20=new TestNetwork(7, 7, 3);
		Paxos p20=new Paxos(n20);
		p20.runPaxos();
		Thread.sleep(10000);
		TestChannel.flag = false;
		System.out.println("*************END OF TEST CASE*******************");
		Thread.sleep(2000);
	}

	void testCase21() throws InterruptedException {
		System.out.println("*************Reordering, multiple distinguished and dropping*******************");
		TestChannel.testCase = 21;
		TestChannel.flag = true;
		TestNetwork n21=new TestNetwork(5, 5, 5);
		Paxos p21=new Paxos(n21);
		p21.runPaxos();
		Thread.sleep(10000);
		TestChannel.flag = false;
		System.out.println("*************END OF TEST CASE*******************");
		Thread.sleep(2000);
	}


}