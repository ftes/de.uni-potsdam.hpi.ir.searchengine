package de.hpi.krestel.mySearchEngine;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Don't change this file!
public abstract class SearchEngine {

	String baseDirectory = "/home/krestel/data/wikipedia-de/";
	String wikiDirectory;
	String directory;
	String logFile;


	public SearchEngine() {

		// Directory to store index and result logs
		this.directory = this.baseDirectory +this.getClass().getSimpleName().toString();
		// new File(this.directory).mkdirs();
		this.logFile = this.directory +"/" +System.currentTimeMillis() +".log";
		// Directory to store wikipedia results
		this.wikiDirectory = this.baseDirectory +"wikiQueryResults/";
		// new File(this.wikiDirectory).mkdirs();

	}

	void indexWrapper(){

		long start = System.currentTimeMillis();
		if(!loadIndex(this.directory)){
			index(this.directory);
			loadIndex(this.directory);
		}
		long time = System.currentTimeMillis() - start;
		log("Index Time: " +time +"ms");
	}


	void searchWrapper(String query, int topK, int prf){

		long start = System.currentTimeMillis();
		ArrayList<String> ranking = search(query, topK, prf);
		long time = System.currentTimeMillis() - start;
		ArrayList<String> goldRanking = getGoldRanking(query);
		Double ndcg = computeNdcg(goldRanking, ranking, topK);
		String output = "\nQuery: " +query +"\t Query Time: " +time +"ms\nRanking: ";
		System.out.println("query: " +query);
		if(ranking!=null){
			Iterator<String> iter = ranking.iterator();
			while(iter.hasNext()){
				String item = iter.next();
				output += item +"\n";
				//		System.out.println(item);
			}
		}
		output += "\nnDCG@" +topK +": " +ndcg;
		log(output);
	}

	ArrayList<String> getGoldRanking(String query) {
		
		//int numResults = 100;
		ArrayList<String> gold;
		String queryTerms = query.replaceAll(" ", "+");
//		try{
//			if (0!=1)
//				throw new Exception(); // don't do this
//			FileInputStream streamIn = new FileInputStream(this.wikiDirectory +queryTerms +".ser");
//			ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
//			gold = (ArrayList<String>) objectinputstream.readObject();
//			return gold;
//		}catch(Exception ex){}

		gold = new ArrayList<String>();
		String url = "http://de.wikipedia.org/w/index.php?title=Spezial%3ASuche&search=" +queryTerms +"&fulltext=Search&profile=default";
		String wikipage = "";	
		try {
			wikipage = (String) new WebFile(url).getContent();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (UnknownServiceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] lines = wikipage.split("\n");
		for(int i=0;i<lines.length;i++){
			//if(lines[i].startsWith("<li><div class='mw-search-results-heading'>")){
			if(lines[i].startsWith("<li>")){
				Pattern p = Pattern.compile("title=\"(.*?)\"");
				Matcher m = p.matcher(lines[i]);
				m.find();
				gold.add(m.group(1));
			}
		}		
//		try {
//			if (0!=1)
//				throw new Exception(); // don't do this
//			FileOutputStream fout = new FileOutputStream(this.wikiDirectory +queryTerms +".ser");
//			ObjectOutputStream oos = new ObjectOutputStream(fout);
//			oos.writeObject(gold);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}catch(Exception ex){}
		return gold;
	}

	synchronized void log(String line) {

		try {
			FileWriter fw = new FileWriter(this.logFile,true);
			BufferedWriter out = new BufferedWriter(fw);
			out.write(line +"\n");
			out.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	

	abstract boolean loadIndex(String directory);

	abstract void index(String directory);

	abstract ArrayList<String> search(String query, int topK, int prf);

	abstract Double computeNdcg(ArrayList<String> goldRanking, ArrayList<String> myRanking, int at);
}
