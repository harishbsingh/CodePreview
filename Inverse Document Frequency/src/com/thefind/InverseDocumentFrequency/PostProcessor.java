package com.thefind.InverseDocumentFrequency;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class responsible for finding the Top 5 words per file and presenting the
 * final output in a new File.
 * 
 * @author harishs
 * 
 */
public class PostProcessor {
	private final Logger				logger	= Logger.getLogger(PostProcessor.class.getName());
	private final BlockingQueue<String>	queue	= new LinkedBlockingQueue<String>();

	PostProcessor() {
	}

	/**
	 * calculates the top 5 per file. Also outputs into a new file.
	 * 
	 * @param postprocessingQueue
	 * @param idfMap
	 */
	public void postProcess(Map<String, Double> idfMap, Queue<String> postprocessingQueue) {
		process(idfMap, postprocessingQueue);
	}

	/**
	 * Starts the OutPut File writer Thread. Also, processes the the data to
	 * compute the top 5 words per file.
	 * 
	 * @param postprocessingQueue
	 * @param idfMap
	 */
	public void process(Map<String, Double> wordsMap, Queue<String> postprocessingQueue) {
		logger.info("Finding the top 5 for every document");
		FileWriterThread writer = new FileWriterThread(queue);
		writer.start();

		try {
			// use min heap to find the top 5
			PriorityQueue<MinHeap> heap = new PriorityQueue<MinHeap>(5);
			Set<String> set = new HashSet<String>(5);

			for (int j = 0; j < postprocessingQueue.size(); j++) {

				String words = postprocessingQueue.remove();
				if (words == null)
					continue;

				words = words.trim();
				String[] arr = words.split(" ");

				for (int i = 0; i < arr.length; i++) {

					if (arr[i].length() > 2 && StringUtils.containsOnlyLetter(arr[i]) && !set.contains(arr[i])) {
						String word = arr[i];
						Double idf = wordsMap.get(word);
						if (idf == null || idf.isNaN())
							continue;

						if (idf >= 2 && idf <= 3.5) {
							if (heap.size() == 5) {
								// logic for calculating the top 5 using heap
								if (idf > heap.peek().idf) {
									heap.poll();
									set.add(word);
									heap.offer(new MinHeap(word, idf));
								}
							} else {
								set.add(word);
								heap.offer(new MinHeap(word, idf));
							}

						}
					}
				}

				// String to be written in the file
				String s = "";
				while (!heap.isEmpty()) {
					s = heap.poll().word + " " + s;
				}
				queue.add(s);

			}
			queue.add(IdfConstants.DOCUMENT_END_MARKER);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error in the post processing.");
		}
		logger.info("Post Processing Done.");
		try {
			writer.join();
		} catch (InterruptedException e) {
			logger.info("Exception Occured: " + e);
		}
	}

	/**
	 * Class used by the min heap {@link PriorityQueue}
	 * 
	 * @author harishs
	 * 
	 */
	private class MinHeap implements Comparable {
		String	word;
		Double	idf;

		MinHeap(String word, Double idf) {
			this.word = word;
			this.idf = idf;
		}

		@Override
		public int compareTo(Object obj) {
			MinHeap instance = (MinHeap) obj;
			return this.idf.compareTo(instance.idf);
		}
	}

	/**
	 * Output File Writer Thread
	 * 
	 * @author harishs
	 * 
	 */
	private class FileWriterThread extends Thread {

		Logger					logger	= Logger.getLogger(FileWriterThread.class.getName());
		BlockingQueue<String>	q;
		BufferedWriter			bw;

		FileWriterThread(BlockingQueue<String> q) {
			this.q = q;
			File file = new File(IdfConstants.OUTPUT_FILE_PATH);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					throw new RuntimeException("Error while creating the output file:", e);
				}
			}

			FileWriter fw;

			try {
				logger.info("Output file path: " + file.getAbsolutePath());

				fw = new FileWriter(file.getAbsoluteFile());
				bw = new BufferedWriter(fw);
			} catch (IOException e) {
				throw new RuntimeException("Error while creating the File Writer:", e);
			}
		}

		@Override
		public void run() {
			logger.info("Starting the processes of writing in the output file.");
			String s;
			try {
				s = q.take();
				while (s != null) {
					if (s.equals(IdfConstants.DOCUMENT_END_MARKER))
						break;
					bw.write(s);
					bw.newLine();
					s = q.take();
				}
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, "Error closing the output file writer.");
				throw new RuntimeException(e);
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error writing to output file.");
				throw new RuntimeException(e);
			} finally {
				try {
					bw.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, "Error closing the output file writer.");
					throw new RuntimeException(e);
				}
			}

			logger.info("Output FileWriter Comepleted it task. \n Done!!!");
		}
	}
}
