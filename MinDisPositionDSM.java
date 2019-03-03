package uk.ac.gla.dcs.dsms;

import org.terrier.structures.postings.Posting;

import java.util.ArrayList;

import org.terrier.matching.dsms.DependenceScoreModifier;
import org.terrier.structures.postings.BlockPosting;

/** 
 * You should use this sample class to implement a proximity feature in Exercise 2.
 * TODO: Describe the function that your class implements
 * <p>
 * You can add your feature into a learned model by appending DSM:uk.ac.gla.IRcourse.SampleProxFeatureDSM to the features.list file.
 * @author TODO
 */
public class MinDisPositionDSM extends DependenceScoreModifier {


	/** This class is passed the postings of the current document,
	 * and should return a score to represent that document.
	 */
	
	@Override
	protected double calculateDependence(
			Posting[] ips, //postings for this document (these are actually IterablePosting[])
			boolean[] okToUse,  //is each posting on the correct document?
			double[] phraseTermWeights, boolean SD //not needed
		) 
	{
		
		final int numberOfQueryTerms = okToUse.length;//the harmonic min distance among all term-term distance.
		double distance=Double.MAX_VALUE;		
		ArrayList<Double> termmindistance=new ArrayList<>();
		//*** 
		//TODO: in this part, write your code that inspects
		//the positions of query terms, to make a proximity function
		//NB: you can cast each Posting to org.terrier.structures.postings.BlockPosting
		//and use the getPositions() method to obtain the positions.
		//***
		
		for(int queryIndex=0;queryIndex<numberOfQueryTerms;queryIndex++) {
			if(okToUse[queryIndex]==true) {
				int[] term1=((BlockPosting)ips[queryIndex]).getPositions();
				for(int queryIndex2 =queryIndex+1;queryIndex2<numberOfQueryTerms;queryIndex2++) {
					double mindistance=Double.MAX_VALUE;
					if(okToUse[queryIndex2]==true) {
						int[] term2=((BlockPosting)ips[queryIndex2]).getPositions();
						for(int i=0;i<term1.length;i++) {
							for(int j=0;j<term2.length;j++) {
								distance=Math.abs(term1[i]-term2[j]);
								mindistance=(distance<mindistance)?distance:mindistance;
							}
						}
					}
					termmindistance.add(mindistance);
					//System.out.println(mindistance);
				}
			}
		}
		double sumscore=0;
		for (double termd : termmindistance) {
			sumscore+=1/termd;
		}
		return termmindistance.size()/sumscore;
	}

	/** You do NOT need to implement this method */
	@Override
	protected double scoreFDSD(int matchingNGrams, int docLength) {
		throw new UnsupportedOperationException();
	}


	@Override
	public String getName() {
		return "ProxFeatureDSM_MinDis";
	}
	
}
