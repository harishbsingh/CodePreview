package com.thefind.InverseDocumentFrequency;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IdfFinder {
	private final static Logger	logger	= Logger.getLogger(IdfFinder.class.getName());

	public IdfFinder() {
		logger.setLevel(Level.ALL);
	}

	/**
	 * Given an Input file, output the top 5 words in terms of IDF for each
	 * document, and where the IDF of the words is between 2.0 and 3.5.
	 * 
	 * @param args
	 *            Specifies the input path
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		logger.info("Idf Finder started");
		if (args == null || args.length == 0) {
			throw new RuntimeException("Input Path not specified");
		}
		start(args[0]);
	}

	/**
	 * There are 3 Threads in total. 1. Retrieving. 2. Processing. 3. Writing.
	 * The method starts the first two threads.
	 * 
	 * @param filePath
	 *            String specifies the input file path.
	 * @throws InterruptedException
	 */
	private static void start(String filePath) throws InterruptedException {

		long startTime = System.currentTimeMillis();
		BlockingQueue<String> fileQueue = new LinkedBlockingQueue<String>(1000);
		RetrieverThread retriever = new RetrieverThread(fileQueue, filePath);
		ProcessorThread processor = new ProcessorThread(fileQueue);
		retriever.start();
		processor.start();
		retriever.join();
		processor.join();
		logger.info("Time Taken : " + (System.currentTimeMillis() - startTime) + " msecs.");
	}

}
