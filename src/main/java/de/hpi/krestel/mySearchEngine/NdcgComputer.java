package de.hpi.krestel.mySearchEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NdcgComputer {
	public static Double computeNdcg(ArrayList<String> goldRanking, ArrayList<String> myRanking, int at) {
		cleanup_Ranking(myRanking);
		
		Map<String, Integer> gainMap = createGainMap(goldRanking);
		
		Double gold_dcg = calculateDCG(goldRanking, gainMap, at);
		Double my_dcg = calculateDCG(myRanking, gainMap, at);
		
		return my_dcg / gold_dcg;
	}
	
	private static void cleanup_Ranking(ArrayList<String> myRanking) {
		for (int i = 0; i < myRanking.size(); i++) {
			String result = myRanking.get(i);
			result = result.split("\n")[0];
			myRanking.set(i, result);
		}
	}

	private static Double calculateDCG(ArrayList<String> ranking, Map<String, Integer> gainMap, int at) {
		Double dcg = 0.0;
		
		for (int i = 0; i < ranking.size() && i < at; i++) {
			int gain = 0;
			if (gainMap.containsKey(ranking.get(i))) {
				gain = gainMap.get(ranking.get(i));
			}
			
			dcg += (Math.pow(2.0, gain) - 1) / Math.log(i + 2);
		}
		
		return dcg;
	}

	private static Map<String, Integer> createGainMap(ArrayList<String> goldRanking) {
		Map<String, Integer> gainMap = new HashMap<String, Integer>();
		
		for (int i = 0; i < goldRanking.size(); i++) {
			gainMap.put(goldRanking.get(i), getGainAt(i));
		}
		
		return gainMap;
	}

	private static int getGainAt(int rank) {
		return 1 + (int) Math.floor(10 * Math.pow(0.5, 0.1 * rank));
	}
}
