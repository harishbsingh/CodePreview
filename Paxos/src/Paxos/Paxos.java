package Paxos;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;


public class Paxos implements Runnable {


	Thread t;
	Network network;

	Queue<String> proposerNoSet;
	long MAX = 100000;

	int distinguishedProposer;

	public Paxos(Network network) {
		this.network=network;

		for(int i=0;i<network.numProposers();i++)
		{
			Channel channel = network.getChannel(i);
			if(channel.isDistinguished())
			{
				distinguishedProposer = i;
				break;
			}
		}

		proposerNoSet = new LinkedList<String>();
		for(int i=0;i<MAX;i++)
			proposerNoSet.add(Integer.toString(i));



	}

	/** This should start your Paxos implementation and return immediately. */
	public void runPaxos() {

		for(int i=0;i<network.numProposers()+network.numAcceptors()+network.numLearners();i++)
		{
			t = new Thread(this);
			t.setName(Integer.toString(i));
			t.start();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		long time = System.currentTimeMillis();
		boolean running = true;
		String SEPARATOR = "#";		
		boolean proposerFlag = true;

		int index = Integer.parseInt(Thread.currentThread().getName());
		Channel channel = network.getChannel(Integer.parseInt(Thread.currentThread().getName()));

		if(index>=0 && index<network.numProposers())
		{

			/*Proposer*/
			while(true)
			{
				if(channel.isDistinguished())
				{
					Date date = new Date();
					String proposerNo = "";
					//					String proposerNo = Integer.toString(date.getYear())+Integer.toString(date.getMonth())+Integer.toString(date.getDate())+Long.toString(date.getTime());
					// Long.toString(System.currentTimeMillis());
					synchronized(proposerNoSet)
					{
						if(proposerNoSet.isEmpty())
						{
							for(long i=MAX;i<2*MAX;i++)
								proposerNoSet.add(Long.toString(i));
							MAX = 2*MAX;
						}
						if(!channel.isDistinguished())
						{
							String msg = "";
							while((msg=channel.receiveMessage())!=null);
							continue;
						}
							
						proposerNo = proposerNoSet.remove();
					}

					String promiseRequest = "promise" + SEPARATOR +  proposerNo + SEPARATOR + Thread.currentThread().getName();

					boolean q = false;
					for(int i = network.numProposers(); i < network.numProposers() + network.numAcceptors(); i++ )
					{
						if(!channel.isDistinguished())
						{
							q = true;
							break;
						}
						channel.sendMessage(i, promiseRequest);
					}
					if(q)
						break;


					String message = "";
					ArrayList<String> messages = new ArrayList<String>();
					SortedSet<String> set = new TreeSet<String>();

					while((message = channel.receiveMessage()) == null);


					if(message.equals("quit"))
					{
						running = false;
						String discardMsg = "";
						while((discardMsg = channel.receiveMessage()) != null);
						break;

					}
					else /*collect all messages from acceptor and process them*/
					{
						messages.add(message);
						Set duplicateAcceptorMsg = new HashSet();
						long startTime = System.currentTimeMillis();
						int count = 0;
						while(true)
						{
							if((message = channel.receiveMessage()) != null) /*collecting all messages*/
							{
								if(!duplicateAcceptorMsg.contains(message)){
									duplicateAcceptorMsg.add(message);
									messages.add(message);	
								}

							}
							if(messages.size()== network.numAcceptors())
								break;
							else
							{
								if(count == 5){
									break;
								}
								Set<Integer> check = new HashSet<Integer>();
								if(System.currentTimeMillis() - startTime > 200){
									count++;
									for(String s : messages){
										String [] arr = s.split(SEPARATOR);
										int ID = Integer.parseInt(arr[3]);
										check.add(ID);
									}
									for(int i = network.numProposers(); i < network.numProposers() + network.numAcceptors(); i++){
										if(!check.contains(i)){
											if(!channel.isDistinguished())
											{
												q = true;
												break;							
											}
											channel.sendMessage(i, promiseRequest);
										}
									}
									if(q)
										break;
									startTime = System.currentTimeMillis();
									continue;
								}
							}
						}
						if(q){
							continue;
						}


						for(int i=0;i<messages.size();i++)
						{
							if(messages.get(i).contains("promiseaccept"))
							{
								if(!channel.isDistinguished())
								{
									q = true;
									break;
								}
								set.add(messages.get(i)+ SEPARATOR + Integer.toString(i));
							}
						}
						if(q)
							continue;
						if(messages.get(0).substring(0, 7).equals("promise"))
						{				
							/*Check majority and send accept*/
							//		System.out.println("First "+set.first() + " Last " + set.last());
							if(set.size()> (network.numAcceptors()/2) )
							{
								/*Check highest proposer no*/
								Iterator itr = set.iterator();
								String acceptorMessage = set.last();//(String) itr.next();
								StringTokenizer st = new StringTokenizer(acceptorMessage, SEPARATOR);
								st.nextToken();
								String highestProposerNo = st.nextToken();
								String value = st.nextToken();
								//System.out.println("Value of proposer " + Thread.currentThread().getName() + " " + value);
								/*Construct message*/
								String learnerMessage = "";
								if(value.equals("-1"))
								{
									message = "acceptrequest"+SEPARATOR+proposerNo+SEPARATOR+Integer.toString((channel.getInitialValue()))+SEPARATOR+Thread.currentThread().getName();
									learnerMessage = "p#"+Integer.toString((channel.getInitialValue()));
								}
								else
								{
									message = "acceptrequest"+SEPARATOR+proposerNo+SEPARATOR+value+SEPARATOR+Thread.currentThread().getName();
									learnerMessage = "p#"+value;
								}

								//				k++;
								boolean qFlag = false;
								for(int i=network.numProposers();i<network.numProposers()+network.numAcceptors();i++)
								{
									if(!channel.isDistinguished()){

										q = true;
										break;
									}
									channel.sendMessage(i, message);
								}
					
								if(q)
									continue;

								String acknak = "";
								long starttime = System.currentTimeMillis();
								Set<String> acknakset = new HashSet();
								Set<Integer> check = new HashSet<Integer>();
								count = 0;
								while(acknakset.size()!=network.numAcceptors())
								{
									if(!channel.isDistinguished())
									{
										q = true;
										break;
									}
									if((acknak=channel.receiveMessage())!=null)
									{
										if(acknak.substring(0, 9).equals("acceptack"))
										{
											acknakset.add(acknak);
										}
									}



									if(System.currentTimeMillis() - startTime > 200)
									{
										if(count == 5){
											break;
										}
										count++;
										for(String s : messages)
										{
											String [] arr = s.split(SEPARATOR);
											int ID = Integer.parseInt(arr[3]);
											check.add(ID);
										}
										for(int i = network.numProposers(); i < network.numProposers() + network.numAcceptors(); i++)
										{
											if(!check.contains(i))
											{
												if(!channel.isDistinguished())
												{
													q = true;
													break;
												}

												channel.sendMessage(i, message);
											}
										}

										if(q)
											break;
										startTime = System.currentTimeMillis();
										continue;
									}
								}
								if(q)
									continue;

							}
						}
					}
				}
				else
				{
					String msg = "";
					while((msg = channel.receiveMessage()) != null);
				}
			}
		}
		else if(index>=network.numProposers() && index<(network.numProposers()+network.numAcceptors()))
		{
			/*ACCEPPTORS*/
			String LastPromisedProposalNo = "100";
			int LastAcceptedValue = -1;
			Set set = new HashSet();
			while(true){

				boolean q = true;
				String message = "";
				while( (message =channel.receiveMessage()) != null )
				{
					if(set.contains(message)){
						continue;
					}
					set.add(message);
					//System.out.println("Acceptor Receives" );
					StringTokenizer stok = new StringTokenizer(message, SEPARATOR );
					String PromiseOrAccept = stok.nextToken();
					//		System.out.println("Acceptor Receives" + PromiseOrAccept);
					//if the incoming message is a Promise Request
					if(message.equals("quit")){
						q = false;
						String discardMsg = ""; 
						while((discardMsg = channel.receiveMessage()) != null);
						running = false;
						break;

					}
					if(PromiseOrAccept.equals("promise")){

						String proposalNo = stok.nextToken();
						int ID = Integer.parseInt(stok.nextToken());
						//			System.out.println("Promise Request Recieved from proposer " + ID);
						//if the Acceptor is has already promised to someone before the current incoming request
						if(!LastPromisedProposalNo.equals("100")){
							if(proposalNo.compareToIgnoreCase(LastPromisedProposalNo) >= 0){

								//			System.out.println("Sending Promies Accepted to Proposer " + ID);
								String prepareReply = "promiseaccept"  + SEPARATOR + LastPromisedProposalNo  + SEPARATOR + Integer.toString(LastAcceptedValue)+SEPARATOR + Thread.currentThread().getName();

								channel.sendMessage(ID, prepareReply);
								LastPromisedProposalNo = proposalNo;
							}
							else{
								String prepareReply = "promisereject"  + SEPARATOR + LastPromisedProposalNo  + SEPARATOR + Integer.toString(LastAcceptedValue)+SEPARATOR + Thread.currentThread().getName();
								channel.sendMessage(ID, prepareReply);
							}
						}
						//if the Acceptor is receiving a Promise Request for the first time.
						else{
							String prepareReply = "promiseaccept"  + SEPARATOR + LastPromisedProposalNo  + SEPARATOR + Integer.toString(LastAcceptedValue)+SEPARATOR + Thread.currentThread().getName();
							channel.sendMessage(ID, prepareReply);
							LastPromisedProposalNo = proposalNo;
						}					

					}//end of If(promise)

					//If the incoming request is an Accept Request
					else if(PromiseOrAccept.equalsIgnoreCase("acceptrequest")){

						//					System.out.println("Accepter Recieives ACCEPT REQUEST" + message);
						String proposalNo = stok.nextToken();
						int value = Integer.parseInt(stok.nextToken());
						int ID = Integer.parseInt(stok.nextToken());
						//if the incoming request has Proposal Number Greater than Last Promised Proposal Number
						String toLearner = "";
						//						System.out.println(proposalNo + "    " + LastPromisedProposalNo);
						if(proposalNo.compareTo(LastPromisedProposalNo) >= 0){
							//							LastAcceptedValue = value;
							//				System.out.println("Sending accept ack to learners &  proposers");
							String acceptReply = "acceptack"  + SEPARATOR + LastPromisedProposalNo  + SEPARATOR +  Integer.toString(LastAcceptedValue)+SEPARATOR + Thread.currentThread().getName();
							channel.sendMessage(ID, acceptReply);

							LastAcceptedValue = value;

							for(int i = network.numProposers() + network.numAcceptors(); i < network.numProposers() + network.numAcceptors() + network.numLearners(); i++){
								toLearner = proposalNo  + SEPARATOR + Integer.toString(LastAcceptedValue)+SEPARATOR + Thread.currentThread().getName();
								channel.sendMessage(i, toLearner);
							}

							Set<String> messages = new HashSet<String>();
							String fromLearnerOrProposer = null;
							boolean quitFlag = true;

							Set duplicateAcceptorMsg = new HashSet();
							long startTime = System.currentTimeMillis();
							int count = 0;
							while(true){

								if((fromLearnerOrProposer = channel.receiveMessage()) != null) /*collecting all messages*/
								{
									if(fromLearnerOrProposer.substring(0, 7).equals("learner"))
									{
										if(!duplicateAcceptorMsg.contains(fromLearnerOrProposer)){
											duplicateAcceptorMsg.add(fromLearnerOrProposer);
											messages.add(fromLearnerOrProposer);	
										}
									}else{
										channel.sendMessage(Integer.parseInt((Thread.currentThread().getName())), fromLearnerOrProposer);

									}


								}
								if(messages.size()== network.numLearners())
									break;
								else
								{
									if(count == 5){
										break;
									}
									Set<Integer> check = new HashSet<Integer>();
									if(System.currentTimeMillis() - startTime > 100){
										count++;
										for(String s : messages){
											String [] arr = s.split(SEPARATOR);
											int LearnerID = Integer.parseInt(arr[1]);
											check.add(LearnerID);
										}
										for(int i = network.numProposers() + network.numAcceptors(); i < network.numProposers() + network.numAcceptors() + network.numLearners(); i++){
											if(!check.contains(i)){
												channel.sendMessage(i, toLearner);
											}
										}
										startTime = System.currentTimeMillis();
										continue;
									}
								}

							}
							//send the accepted value to 

						}
						else
						{
							String acceptReply = "acceptreject"  + SEPARATOR + LastPromisedProposalNo  + SEPARATOR +  Integer.toString(LastAcceptedValue)+SEPARATOR + Thread.currentThread().getName();
							channel.sendMessage(ID, acceptReply);

						}					

					}

				}
				if( q == false){
					q = true;
					break;
				}
			}
		}
		else if(index>=(network.numProposers()+network.numAcceptors) && index<(network.numProposers()+network.numAcceptors()+network.numLearners()))
		{

			while(true) 
			{
				ArrayList<String> messages = new ArrayList<String>();
				String message = "";
				if((message = channel.receiveMessage())!=null)
				{

						messages.add(message);
						String []msg = message.split(SEPARATOR);
						int id = Integer.parseInt(msg[2]);
						channel.sendMessage(id, "learnerack"+SEPARATOR+Thread.currentThread().getName());

						while(messages.size()!=network.numAcceptors()){
							if((message=channel.receiveMessage())!=null)
							{
								if(!messages.contains(message)){
									messages.add(message);
								}
							}
						}

						HashMap<String, String> map = new HashMap<String, String>();
						HashMap<String, Integer> count = new HashMap<String, Integer>();

						for(int i=0;i<messages.size();i++)
						{
							String temp = messages.get(i);
							StringTokenizer st = new StringTokenizer(temp, "#");
							String proposalNo = st.nextToken();
							String value = st.nextToken();
							if(!map.containsKey(proposalNo))
							{
								map.put(proposalNo, value);
								count.put(proposalNo, 1);
							}
							else
							{
								int cnt = count.get(proposalNo);
								count.put(proposalNo, cnt+1);
							}
						}

						String pr = "0";
						String v = "";
						int c = 0;
						Iterator itr = count.keySet().iterator();
						while(itr.hasNext())
						{
							String pNo = (String) itr.next();
							String val = map.get(pNo);
							int cnt = count.get(pNo);
							if(cnt>c)
							{
								pr = pNo;
								v = val;
								c = cnt;
							}
						}

						if(c > network.numAcceptors()/2){
							channel.decide(Integer.parseInt(v));
							
						}
				}
			}
		}	
	}
}