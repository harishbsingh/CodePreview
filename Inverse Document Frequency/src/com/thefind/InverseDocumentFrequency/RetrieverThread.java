package com.thefind.InverseDocumentFrequency;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class responsible for reading Data.
 * 
 * @author harishs
 * 
 */
public class RetrieverThread extends Thread {

	private final Logger				logger	= Logger.getLogger(RetrieverThread.class.getName());

	private final BlockingQueue<String>	q;
	private final String						filePath;

	private BufferedReader				br;

	public RetrieverThread(BlockingQueue<String> q, String filePath) {
		this.q = q;
		this.filePath = filePath;
		logger.setLevel(Level.ALL);
		try {
			br = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "Error while reading the Input File.");
			throw new RuntimeException();
		}

	}

	@Override
	public void run() {
		doChecks();
		try {
			logger.info("Retriever Thread Started: Reading in progress.");
			String str = br.readLine();
			while (str != null) {
				if (str.equals("")) {
					str = br.readLine();
					continue;
				}
				try {
					q.put(str.toLowerCase());
				} catch (InterruptedException e) {
					logger.info("Record Ignored" + str);
				}
				str = br.readLine();
			}
			q.put(IdfConstants.DOCUMENT_END_MARKER);

		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "Input File not found.");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error while reading the Input File.");
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error while reading the Input File.");
			}
		}
		logger.info("Retriever Thread: Finished Reading.");

	}

	/**
	 * This method is responsible for doing all the checks before starting the
	 * data retrieval. we should add as many checks herre, as possible.
	 */
	private void doChecks() {
		if (filePath == null) {
			logger.log(Level.SEVERE, "INPUT NULL");
			throw new RuntimeException("No Input Spcified");
		}
		File file = new File(filePath);
		if (!file.exists() && !file.isDirectory()) {
			logger.info("File is Not a Directory or File does not exists.");
			throw new RuntimeException("No Input Spcified");
		}

	}

}
