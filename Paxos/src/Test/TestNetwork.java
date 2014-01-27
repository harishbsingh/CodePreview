package Test;
import Paxos.*;
import java.util.LinkedList;

public class TestNetwork extends Network{

	int testtotalProcesses;
	int testnumProposers;
	int testnumAcceptors;
	int testnumLearners;
	int testdecision=-1;
	  LinkedList<String>[] testqueues;

	  /** Create a network with numProposers proposes, numAcceptors
	   * acceptors, and numLearners learners.*/

	  @SuppressWarnings("unchecked")
	  public TestNetwork(int numProposers, int numAcceptors, int numLearners) {
		  super(numProposers,  numAcceptors,  numLearners);
	    testtotalProcesses=numProposers+numAcceptors+numLearners;
	    testqueues=new LinkedList[testtotalProcesses];
	    for(int i=0;i<testtotalProcesses;i++) {
	      testqueues[i]=new LinkedList<String>();
	    }
	    this.testnumProposers=numProposers;
	    this.testnumAcceptors=numAcceptors;
	    this.testnumLearners=numLearners;
	  }
	  
	  public int testnumAcceptors() {
	    return testnumAcceptors;
	  }

	  public int testnumProposers() {
	    return testnumProposers;
	  }

	  public int testnumLearners() {
	    return testnumLearners;
	  }

	  /** getChannel returns a communication channel for process processID.
	   *
	   *   Process ids:
	   *   0 through numProposers-1 should be Proposers
	   *
	   *   numProposers through numAccepters+numProposes-1 should be Acceptors
	   *
	   *   numAccepters+numProposes through
	   *   numAccepters+numProposes+numLearners-1 should be Learners */
	 
	  public Channel getChannel(int processID) {
	    if (processID<0 || processID>= testtotalProcesses) {
	      throw new Error("Invalid process ID.");
	    }

	    TestChannel c=new TestChannel();
	    c.testindex=processID;
	    c.testnetwork=this;
	    return c;
	  }
	}