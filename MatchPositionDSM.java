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
public class MatchPositionDSM extends DependenceScoreModifier {


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
		
		final int numberOfQueryTerms = okToUse.length;
		ArrayList<Double> termdistance=new ArrayList<>();//the  distance list of  all term-term distance.		
		
		//*** 
		//TODO: in this part, write your code that inspects
		//the positions of query terms, to make a proximity function
		//NB: you can cast each Posting to org.terrier.structures.postings.BlockPosting
		//and use the getPositions() method to obtain the positions.
		//***
		
		for(int queryIndex=0;queryIndex<numberOfQueryTerms-1;queryIndex++) {
			if(okToUse[queryIndex]==true) {
				int[] term1=((BlockPosting)ips[queryIndex]).getPositions();
				for(int queryIndex2 =queryIndex+1;queryIndex2<numberOfQueryTerms;queryIndex2++) {
					if(okToUse[queryIndex2]==true) {
						int[] term2=((BlockPosting)ips[queryIndex2]).getPositions();					
						ArrayList<Double> term_termdistance=new ArrayList<>();
						int[] shortterm=term1;
						int[] longterm=term2;
						if(term1.length>term2.length) {
							shortterm=term2;
							longterm=term1;
						}
						int shorttermlength=shortterm.length;
						int longtermlength=longterm.length;
						ArrayList<Integer> ocupiedindex=new ArrayList<>();
						for(int i=0;i<shorttermlength;i++) {
							double oneindexoutput=Double.MAX_VALUE;
							double distance1=Double.MAX_VALUE;
							
							int place=0;
							for(int j=0;j<longtermlength;j++) {
								distance1=Math.abs(shortterm[i]-longterm[j]);
								if(oneindexoutput>distance1&&!ocupiedindex.contains(j)) {	
								oneindexoutput=distance1;
								place=j;
								}
							}
							term_termdistance.add(oneindexoutput);
							ocupiedindex.add(place);						
						}
						double term_termdistance1=0;
						for (double distance : term_termdistance){
							term_termdistance1+=distance;
						}
						termdistance.add(term_termdistance1/term_termdistance.size());
						//System.out.println("the term-term score is"+term_termdistance1/term_termdistance.size());
					}
				}
			}
		}
		double sumscore=0;
		for (double distance : termdistance){
			sumscore+=1/distance;
		}
		return termdistance.size()/sumscore; //return the mean term-term match as the score of the document
	}

	/** You do NOT need to implement this method */
	@Override
	protected double scoreFDSD(int matchingNGrams, int docLength) {
		throw new UnsupportedOperationException();
	}


	@Override
	public String getName() {
		return "ProxFeatureDSM_Match";
	}
	
}
