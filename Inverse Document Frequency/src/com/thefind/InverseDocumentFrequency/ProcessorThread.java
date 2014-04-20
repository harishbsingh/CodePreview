package com.thefind.InverseDocumentFrequency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class responsible for calculation of IDF
 * 
 * @author harishs
 * 
 */
public class ProcessorThread extends Thread {

	private final Logger				logger	= Logger.getLogger(ProcessorThread.class.getName());

	private final BlockingQueue<String>	q;

	private final Map<String, MetaData>	wordMap	= new HashMap<String, MetaData>();

	public ProcessorThread(BlockingQueue<String> q) {
		logger.setLevel(Level.ALL);
		this.q = q;
	}

	@Override
	public void run() {
		try {
			logger.info("Processor Threat Started: Start Processing Files");
			String words = q.take();
			int count = 0;
			logger.info("Populating the Term Document Frequency Map.");
			Queue<String> postprocessingQueue = new LinkedList<String>();

			while (words != null) {
				if (words.equalsIgnoreCase(IdfConstants.DOCUMENT_END_MARKER))
					break;
				count++;
				postprocessingQueue.add(words);
				putInWordMap(words, count);
				words = q.take();
			}
			logger.info("Term Document Frequency Map Ready.");

			logger.info("Calculating the IDFs of all the terms.");
			Map<String, Double> idfMap = new HashMap<String, Double>();

			for (Map.Entry<String, MetaData> entry : wordMap.entrySet()) {
				MetaData obj = entry.getValue();
				double idf = (count / obj.noOfDocs);
				if (idf != 0) {
					idf = Math.log10(new Double(idf));
					idfMap.put(entry.getKey(), idf);
				}

			}
			logger.info("IDF calculation done.");

			logger.info("Post Processing the Data to Write it into a file.");

			PostProcessor poPoObj = new PostProcessor();
			poPoObj.postProcess(idfMap, postprocessingQueue);

		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, "Error during Data processing:" + e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * Method which populated the Map of Terms to {@link MetaData}
	 * 
	 * @param words
	 *            String words in the document
	 * @param docId
	 *            Document Id, which, for now, is the Line Number
	 */
	private void putInWordMap(String words, int docId) {
		words = words.trim();
		String[] arr = words.split(" ");
		Set<String> set = new HashSet<String>();

		for (int i = 0; i < arr.length; i++) {

			if (arr[i].length() > 2 && StringUtils.containsOnlyLetter(arr[i]) && !set.contains(arr[i])) {
				MetaData obj = wordMap.get(arr[i]);

				if (obj == null) {
					obj = new MetaData(1, Integer.toString(docId));
				} else {
					obj.noOfDocs = obj.noOfDocs + 1;
					obj.docIds.add(Integer.toString(docId));
				}
				wordMap.put(arr[i], obj);
				set.add(arr[i]);
			}

		}

	}

	/**
	 * Instance of this object stores the No of documents to which a word
	 * belongs and their document ids
	 * 
	 * @author harishs
	 * 
	 */
	public class MetaData {
		int				noOfDocs;

		// For now, using line number as Doc Id.
		List<String>	docIds	= new LinkedList<String>();

		MetaData(int noOfDocs, String docId) {
			this.noOfDocs = noOfDocs;
			docIds.add(docId);
		}
	}

}
