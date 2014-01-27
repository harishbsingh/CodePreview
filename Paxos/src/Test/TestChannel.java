package Test;

import java.util.ArrayList;
import java.util.Random;

import Paxos.*;


public class TestChannel extends Channel{

	TestNetwork testnetwork;
	int testindex;
	static int testCase;
	public static boolean flag = true;
	int proposer = 0;
	static long time =  System.currentTimeMillis();
	
	ArrayList<String> stackTrace = new ArrayList<String>();

		
	

	/** Send the message message to process destination. */


	public void sendMessage(int destination, String message) {
		
		if(flag == false){			
			//			throw new Error("End of Test Case");
			Thread.currentThread().stop();
		}
		synchronized(testnetwork.testqueues[destination]) {

			if(testCase==1)
				testnetwork.testqueues[destination].add(message);

			else if(testCase==2)
			{
				if(testindex>=testnetwork.testnumProposers() && testindex<testnetwork.testnumProposers()+testnetwork.numAcceptors())
				{
					Random r = new Random();
					int end = r.nextInt(5);
					stackTrace.add("Acceptor " + testindex + " sends to Process " + destination + " message: " + message + " " + (end+1) + " times");
					for(int i=1;i<=end+1;i++)
						testnetwork.testqueues[destination].add(message);
				}
				else
					testnetwork.testqueues[destination].add(message);
			}

			else if (testCase==3)
			{
				if(testindex>=testnetwork.testnumProposers() && testindex<testnetwork.testnumProposers()+testnetwork.numAcceptors())
				{
					stackTrace.add("Message contents send by Process " + Thread.currentThread().getName() + " to Process " + destination + ": " + message);
					Random r = new Random();
					int end = r.nextInt(3);
					if(end==0)
						stackTrace.add("Message: "+ message + " sent from Acceptor " + testindex + " to Process " + destination + " is lost");
					for(int i=0;i<end;i++)
						testnetwork.testqueues[destination].add(message);
				}
				else
					testnetwork.testqueues[destination].add(message);
			}

			else if(testCase == 7)
			{
				Random r = new Random();
				int numberOfProposersInvolved = r.nextInt(testnetwork.testnumProposers());
				if(testindex < testnetwork.testnumProposers()){
					if(numberOfProposersInvolved != 0 ){
						if(numberOfProposersInvolved == 1){
							int n = r.nextInt(testnetwork.testnumProposers());
							if( testindex == n){
								int n1 = r.nextInt(5);
								stackTrace.add("Proposer " + testindex + " sends to Process " + destination + " message: " + message + " " + (n1+1) + " times");
								if(testindex < testnetwork.testnumProposers()){
									for(int i = 0; i < n1; i++){
										testnetwork.testqueues[destination].add(message);
									}
								} 
								else {
									testnetwork.testqueues[destination].add(message);
								}
							}
						} 
						else {
							int n1 = r.nextInt(5);
							stackTrace.add("Proposer " + testindex + " sends to Process " + destination + " message: " + message + " " + (n1+1) + " times");
							if(testindex < testnetwork.testnumProposers()){

								for(int i = 0; i < n1; i++){


									testnetwork.testqueues[destination].add(message);
								}
							}
							else {
								testnetwork.testqueues[destination].add(message);
							}
						}

					}
					else{
						if(testindex < testnetwork.testnumProposers()){
							testnetwork.testqueues[destination].add(message);
						}
					}
				}
				else{

					testnetwork.testqueues[destination].add(message);
				}
			}
			
			else if(testCase == 10){
				if(testindex >= testnetwork.testnumProposers && testindex < testnetwork.testnumProposers() + testnetwork.numAcceptors()){
					if(destination >= testnetwork.testnumProposers() + testnetwork.numAcceptors() ){
						stackTrace.add("Acceptor "+ testindex +" message: " + message + " to learner " + destination + " is lost");
					}
					else
						testnetwork.testqueues[destination].add(message);
				}
				else{
					testnetwork.testqueues[destination].add(message);						
				}
			}
//Check
			else if(testCase == 11)
			{	
				if(testindex >= testnetwork.testnumProposers() && testindex<testnetwork.testnumProposers()+testnetwork.numAcceptors())
				{
					if((System.currentTimeMillis()-time) >50 )
					{		
						testnetwork.testqueues[destination].add(message);
					}
				}
				else
					testnetwork.testqueues[destination].add(message);
			}

			else if (testCase == 13)
			{
				if(testindex>=testnetwork.testnumProposers()+testnetwork.numAcceptors()/2 && testindex<testnetwork.testnumProposers()+testnetwork.numAcceptors())
					stackTrace.add("Message: "+ message+"  by Accceptor "+destination+ " dropped");
				
				if(testindex >= testnetwork.testnumProposers() && testindex<testnetwork.testnumProposers()+testnetwork.numAcceptors()/2)
				{		
					testnetwork.testqueues[destination].add(message);
				}
				else
					testnetwork.testqueues[destination].add(message);	
			}

			else if (testCase == 14)
			{
				if(testindex>testnetwork.testnumProposers()+testnetwork.numAcceptors()-2 && testindex<testnetwork.testnumProposers()+testnetwork.numAcceptors())
					stackTrace.add("Acceptor "+testindex + " not working");
				
				if(testindex >= testnetwork.testnumProposers() && testindex<=testnetwork.testnumProposers()+testnetwork.numAcceptors()-2)
				{		
					testnetwork.testqueues[destination].add(message);
				}
				else
					testnetwork.testqueues[destination].add(message);	
			}

			else if (testCase == 17)
			{
				if(testindex>testnetwork.testnumProposers() && testindex<testnetwork.testnumProposers()+testnetwork.numAcceptors() || testindex>testnetwork.testnumProposers()+testnetwork.numAcceptors() && testindex<testnetwork.testnumProposers()+testnetwork.numAcceptors()+testnetwork.numLearners())
					stackTrace.add("Process "+testindex+ " not working");
				if(testindex==testnetwork.testnumProposers() || testindex==testnetwork.testnumProposers()+testnetwork.numAcceptors())
				{		
					testnetwork.testqueues[destination].add(message);
				}
				else
					testnetwork.testqueues[destination].add(message);	
			}

			else if (testCase == 19 || testCase==20)
			{
				if(testindex>=testnetwork.testnumProposers() && testindex<testnetwork.testnumProposers()+testnetwork.numAcceptors())
				{		
					if(destination>=testnetwork.testnumProposers()+testnetwork.numAcceptors() && destination<testnetwork.testnumProposers()+testnetwork.numAcceptors()+testnetwork.numLearners())
					{
						stackTrace.add("Acceptor "+ testindex +" Message: "+message+" to Learner "+destination+ " is dropped" );
					}
					else
						testnetwork.testqueues[destination].add(message);

				}
				else
					testnetwork.testqueues[destination].add(message);	
			}
			
			else
				testnetwork.testqueues[destination].add(message);

		}
	}

	/** Receive a message. */

	public String receiveMessage() {

		if(flag == false){			
			//			throw new Error("End of Test Case");
			Thread.currentThread().stop();
		}

		synchronized(testnetwork.testqueues[testindex]) {

			if( testCase == 8){
				if(testindex < testnetwork.testnumProposers()){
					Random r = new Random();
					int n = r.nextInt(2); //0 ==> dont drop any message, 1 ==> drop 
					if(n == 0){
						// Dont drop any message
						if (!testnetwork.testqueues[testindex].isEmpty())
							return testnetwork.testqueues[testindex].remove();
						else
							return null;
					}
					else{
						//DROP
						if (!testnetwork.testqueues[testindex].isEmpty())
						{
							int size = testnetwork.testqueues[testindex].size();
							int n1 = r.nextInt(2); //0==> drop one message, 1==> dont drop
							if(n1 == 0){
								//drop 1 message
								stackTrace.add("Proposer "+ testindex+" message: "+testnetwork.testqueues[testindex] + " dropped");
								testnetwork.testqueues[testindex].remove();
								if (!testnetwork.testqueues[testindex].isEmpty())
									return testnetwork.testqueues[testindex].remove();
								else
									return null;
							}

							else{
								//Dont Drop
								if (!testnetwork.testqueues[testindex].isEmpty())
									return testnetwork.testqueues[testindex].remove();
								else
									return null;
							}

						}
						else
							return null;
					}
				}
				else{
					if (!testnetwork.testqueues[testindex].isEmpty())
						return testnetwork.testqueues[testindex].remove();
					else
						return null;
				}
			}

			
			else if(testCase == 9)
			{
				if(testindex < testnetwork.testnumProposers()){
					Random r = new Random();
					int n = r.nextInt(2); //0 ==> dont drop any message, 1 ==> drop 
					if(n == 0){
						// Dont drop any message
						if (!testnetwork.testqueues[testindex].isEmpty())
							return testnetwork.testqueues[testindex].remove();
						else
							return null;
					}
					else
					{
						for(int i=0;i<testnetwork.testqueues[testindex].size();i++)
							stackTrace.add("Proposer "+testindex + " message: "+testnetwork.testqueues[testindex].get(i)+ " dropped");
						testnetwork.testqueues[testindex].clear();
						return null;
					}
				}
				else{
					if (!testnetwork.testqueues[testindex].isEmpty())
						return testnetwork.testqueues[testindex].remove();
					else
						return null;
				}
			}

			else if(testCase == 12){

				if(testindex >= testnetwork.testnumProposers() && testindex < testnetwork.testnumProposers() + testnetwork.numAcceptors()){
					if(testnetwork.testqueues[testindex].size()>1){
						stackTrace.add("Swapping acceptor "+testindex+ " message: "+testnetwork.testqueues[testindex].get(1)+" with "+testnetwork.testqueues[testindex].get(0));
						String temp = testnetwork.testqueues[testindex].remove(1);
						testnetwork.testqueues[testindex].add(0, temp);
						//	System.out.println("Acceptor receives message: " + testnetwork.testqueues[testindex].get(0) + "before message: " + testnetwork.testqueues[testindex].get(1));
					}
					if (!testnetwork.testqueues[testindex].isEmpty())
						return testnetwork.testqueues[testindex].remove();
					else
						return null;
				}
				else{
					if (!testnetwork.testqueues[testindex].isEmpty())
						return testnetwork.testqueues[testindex].remove();
					else
						return null;
				}
			}
			else if(testCase == 15){

				if(testindex < testnetwork.testnumProposers()){
					if(testnetwork.testqueues[testindex].size()>1){
						stackTrace.add("Swapping proposer "+testindex+ " message: "+testnetwork.testqueues[testindex].get(1)+" with "+testnetwork.testqueues[testindex].get(0));			
						String temp = testnetwork.testqueues[testindex].remove(1);
						testnetwork.testqueues[testindex].add(0, temp);
						
					}
					if (!testnetwork.testqueues[testindex].isEmpty())
						return testnetwork.testqueues[testindex].remove();
					else
						return null;
				}
				else{
					if (!testnetwork.testqueues[testindex].isEmpty())
						return testnetwork.testqueues[testindex].remove();
					else
						return null;
				}
			}
			else if(testCase == 16){

				if(testnetwork.testqueues[testindex].size()>1){
					stackTrace.add("Swapping process "+testindex+ " message: "+testnetwork.testqueues[testindex].get(1)+" with "+testnetwork.testqueues[testindex].get(0));
					String temp = testnetwork.testqueues[testindex].remove(1);
					testnetwork.testqueues[testindex].add(0, temp);

					if (!testnetwork.testqueues[testindex].isEmpty())
						return testnetwork.testqueues[testindex].remove();
					else
						return null;
				}
				else{
					if (!testnetwork.testqueues[testindex].isEmpty())
						return testnetwork.testqueues[testindex].remove();
					else
						return null;
				}
			}
			
			else if(testCase == 21)
			{
				if(testindex == testnetwork.testnumProposers())
				{ //1st acceptor REORDERS MESSAGES
					if(testnetwork.testqueues[testindex].size()>1)
					{
						stackTrace.add("Swapping acceptor "+testindex+ " message: "+testnetwork.testqueues[testindex].get(1)+" with "+testnetwork.testqueues[testindex].get(0));
						String temp = testnetwork.testqueues[testindex].remove(1);
						testnetwork.testqueues[testindex].add(0, temp);
				//		System.out.println("receives message: " + testnetwork.testqueues[testindex].get(0) + "before message: " + testnetwork.testqueues[testindex].get(1));

						if (!testnetwork.testqueues[testindex].isEmpty())
							return testnetwork.testqueues[testindex].remove();
						else
							return null;
					}
					else
					{
						if (!testnetwork.testqueues[testindex].isEmpty())
							return testnetwork.testqueues[testindex].remove();
						else
							return null;
					}
				}
				else if( testindex == testnetwork.numAcceptors() + 1 || testindex == testnetwork.testnumProposers() - 1 || testindex == testnetwork.testnumProposers() + testnetwork.numAcceptors())
				{
					Random r = new Random();
					int n = r.nextInt(2); //0 ==> dont drop any message, 1 ==> drop 
					if(n == 0){
						// Dont drop any message
						if (!testnetwork.testqueues[testindex].isEmpty())
							return testnetwork.testqueues[testindex].remove();
						else
							return null;
					}
					else{
						for(int i=0;i<testnetwork.testqueues[testindex].size();i++)
							stackTrace.add("Message "+testnetwork.testqueues[testindex].get(i)+" from process "+testindex+ " dropped");
						testnetwork.testqueues[testindex].clear();
						return null;
					}
					
				}
				else	
				{
					if (!testnetwork.testqueues[testindex].isEmpty())
						return testnetwork.testqueues[testindex].remove();
					else
						return null;
				}
				}
			
			else	
			{
				if (!testnetwork.testqueues[testindex].isEmpty())
					return testnetwork.testqueues[testindex].remove();
				else
					return null;
			}
		}
	}

	/** Call this function to determine whether a proposer is distinguished. */
	
	public boolean isDistinguished() {	
		if (testindex>=testnetwork.testnumProposers)
			throw new Error("Non-proposers should not be asking whether they are distinguished");
		
		if(flag == false)
		{			
			//			throw new Error("End of Test Case");
			Thread.currentThread().stop();
		}

		if(testCase==4)
		{
			Random r = new Random();
			int currentProposer = r.nextInt(testnetwork.testnumProposers());
			
			if(testindex == currentProposer)
			{
				stackTrace.add("Distinguished propsoser changes to "+currentProposer);
				return true;
			}
		}
		else if(testCase==5 || testCase==20 || testCase==21)
		{
			if((System.currentTimeMillis()-time) < 100)
			{
				if(testindex == proposer)
				{
					stackTrace.add("Distinguished propsoser changes to "+testindex);
					return true;
				}
			}
			else
			{
				time = System.currentTimeMillis();
				proposer = (proposer+1)%testnetwork.testnumProposers();
				if(proposer == testindex)
				{
					stackTrace.add("Distinguished propsoser changes to "+testindex);
					return true;
				}
			}
		}
		else if(testCase == 6)
		{
			if(testindex>=0 && testindex<testnetwork.testnumProposers())
			{
				stackTrace.add("Distinguished propsoser changes to "+testindex);
				return true;
			}
		}

		else
		{
			if (testindex==0)
				return true;
		}

		return false;
	}

	/** Call this function to register a testdecision by a learner. */

	public void decide(int testdecision) {
		if (testindex<(testnetwork.testnumProposers+testnetwork.testnumAcceptors))
			throw new Error("Non-learner should not be deciding a value");

		if (testdecision>=testnetwork.testnumProposers)
			throw new Error("The decided value was not an initial value...");

		
		System.out.println("Value selected by Learner" + testindex + " is :: " +testdecision);

		synchronized(testnetwork) {
			if (testnetwork.testdecision==-1)
				testnetwork.testdecision=testdecision;
			else {
				if (testnetwork.testdecision!=testdecision)
				{
					System.out.println("Disagreement between Learners");
					System.out.println("----------------------------------------------------------");
					System.out.println("Stack Trace of relevant messages:\n");
					for(int i=0;i<stackTrace.size();i++)
						System.out.println(stackTrace.get(i));
					System.out.println("-----------------------------------------------------------");
				}
			}
		}
	}

	/** Call this function to get the initial value for a proposer. */

	public int getInitialValue() {
		if (testindex>=testnetwork.testnumProposers)
			throw new Error("Non-proposers should not be asking for initial value");
		return testindex;
	}

}