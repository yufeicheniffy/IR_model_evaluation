package uk.ac.gla.dcs.dsms;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.terrier.indexing.IndexTestUtils;
import org.terrier.structures.Index;
import org.terrier.structures.postings.BlockPosting;
import org.terrier.structures.postings.IterablePosting;
import org.terrier.tests.ApplicationSetupBasedTest;
import org.terrier.utility.ApplicationSetup;

public class TestMatchPositionDSM extends ApplicationSetupBasedTest
{
	@Test public void testOneDocTwoTerms() throws Exception {

		//make an index with a single sample document
		ApplicationSetup.setProperty("termpipelines", "");
		Index index = IndexTestUtils.makeIndexBlocks(
				new String[]{"doc1"}, 
				new String[]{"The quick brown fox jumps over the lazy dog"});

		//get posting iterators for two terms 'fox' and 'jumps'
		IterablePosting[] ips = new IterablePosting[2];
		ips[0] = index.getInvertedIndex().getPostings(index.getLexicon().getLexiconEntry("fox"));
		ips[1] = index.getInvertedIndex().getPostings(index.getLexicon().getLexiconEntry("jumps"));
		ips[0].next();
		ips[1].next();
		assertEquals(0, ips[0].getId());
		assertEquals(0, ips[1].getId());
		System.out.println("Positions of term 'fox'="+ Arrays.toString( ((BlockPosting)ips[0]).getPositions()));
		System.out.println("Positions of term 'jumps'="+ Arrays.toString( ((BlockPosting)ips[1]).getPositions()));

		MatchPositionDSM sample = new MatchPositionDSM();
		
		double score = sample.calculateDependence(
            ips, //posting lists
            new boolean[]{true,true},  //is this posting list on the correct document?
            new double[]{1d,1d}, false//doesnt matter
		);
		System.out.println(score);
		assertEquals(1.0d, score,0.0d);
		//TODO: make your assertion about what the score should be
		//assertEquals(XXX, score, 0.0d);
	}
	
	@Test public void testOneDocTwoTerms2() throws Exception {

		//make an index with a single sample document
		ApplicationSetup.setProperty("termpipelines", "");
		Index index = IndexTestUtils.makeIndexBlocks(
				new String[]{"doc1"}, 
				new String[]{"The quick brown fox does not jumps over the lazy dog and the reality is the lazy dog jumps over fox"});

		//get posting iterators for two terms 'fox' and 'jumps'
		IterablePosting[] ips = new IterablePosting[2];
		ips[0] = index.getInvertedIndex().getPostings(index.getLexicon().getLexiconEntry("fox"));
		ips[1] = index.getInvertedIndex().getPostings(index.getLexicon().getLexiconEntry("jumps"));
		ips[0].next();
		ips[1].next();
		assertEquals(0, ips[0].getId());
		assertEquals(0, ips[1].getId());
		System.out.println("Positions of term 'fox'="+ Arrays.toString( ((BlockPosting)ips[0]).getPositions()));
		System.out.println("Positions of term 'jumps'="+ Arrays.toString( ((BlockPosting)ips[1]).getPositions()));

		MatchPositionDSM sample = new MatchPositionDSM();
		
		double score = sample.calculateDependence(
            ips, //posting lists
            new boolean[]{true,true},  //is this posting list on the correct document?
            new double[]{1d,1d}, false//doesnt matter
		);
		System.out.println(score);
		assertEquals(2.5d, score,0.0d);
		//TODO: make your assertion about what the score should be
		//assertEquals(XXX, score, 0.0d);
	}
	
	@Test public void testOneDocFourTerms() throws Exception {

		//make an index with a single sample document
		ApplicationSetup.setProperty("termpipelines", "");
		Index index = IndexTestUtils.makeIndexBlocks(
				new String[]{"doc1"}, 
				new String[]{"The quick brown fox does not jumps over the lazy dog and the reality is the lazy dog jumps over the little fox skr and it shouted rua rua"});

		//get posting iterators for two terms 'fox' and 'jumps' "fox!"
		IterablePosting[] ips = new IterablePosting[4];
		ips[0] = index.getInvertedIndex().getPostings(index.getLexicon().getLexiconEntry("fox"));
		ips[1] = index.getInvertedIndex().getPostings(index.getLexicon().getLexiconEntry("jumps"));
		ips[2] = index.getInvertedIndex().getPostings(index.getLexicon().getLexiconEntry("skr"));
		ips[3] = index.getInvertedIndex().getPostings(index.getLexicon().getLexiconEntry("rua"));
		ips[0].next();
		ips[1].next();
		ips[2].next();
		ips[3].next();
		assertEquals(0, ips[0].getId());
		assertEquals(0, ips[1].getId());
		assertEquals(0, ips[2].getId());
		assertEquals(0, ips[3].getId());
		System.out.println("Positions of term 'fox'="+ Arrays.toString( ((BlockPosting)ips[0]).getPositions()));
		System.out.println("Positions of term 'jumps'="+ Arrays.toString( ((BlockPosting)ips[1]).getPositions()));
		System.out.println("Positions of term 'skr'="+ Arrays.toString( ((BlockPosting)ips[2]).getPositions()));
		System.out.println("Positions of term 'rua'="+ Arrays.toString( ((BlockPosting)ips[3]).getPositions()));
		MatchPositionDSM sample = new MatchPositionDSM();
		
		double score = sample.calculateDependence(
            ips, //posting lists
            new boolean[]{true,true,true,true},  //is this posting list on the correct document?
            new double[]{1d,1d,1d,1d}, false//doesnt matter
		);
		System.out.println(score);
		assertEquals(3.21388900316d, score,0.03d);
		//TODO: make your assertion about what the score should be
		//assertEquals(XXX, score, 0.0d);
	}
	@Test public void testOneDocThreeTerms2() throws Exception {

		//make an index with a single sample document
		ApplicationSetup.setProperty("termpipelines", "");
		Index index = IndexTestUtils.makeIndexBlocks(
				new String[]{"doc1"}, 
				new String[]{"test fox test jumps test skr test fox test jumps test skr"});

		//get posting iterators for two terms 'fox' and 'jumps' "fox!"
		IterablePosting[] ips = new IterablePosting[3];
		ips[0] = index.getInvertedIndex().getPostings(index.getLexicon().getLexiconEntry("fox"));
		ips[1] = index.getInvertedIndex().getPostings(index.getLexicon().getLexiconEntry("jumps"));
		ips[2] = index.getInvertedIndex().getPostings(index.getLexicon().getLexiconEntry("skr"));
		ips[0].next();
		ips[1].next();
		ips[2].next();
		assertEquals(0, ips[0].getId());
		assertEquals(0, ips[1].getId());
		assertEquals(0, ips[2].getId());
		System.out.println("Positions of term 'fox'="+ Arrays.toString( ((BlockPosting)ips[0]).getPositions()));
		System.out.println("Positions of term 'jumps'="+ Arrays.toString( ((BlockPosting)ips[1]).getPositions()));
		System.out.println("Positions of term 'skr'="+ Arrays.toString( ((BlockPosting)ips[2]).getPositions()));

		MatchPositionDSM sample = new MatchPositionDSM();
		
		double score = sample.calculateDependence(
            ips, //posting lists
            new boolean[]{true,true,true},  //is this posting list on the correct document?
            new double[]{1d,1d,1d}, false//doesnt matter
		);
		System.out.println(score);
		assertEquals(3/1.25d, score,0.000005d);
		//TODO: make your assertion about what the score should be
		//assertEquals(XXX, score, 0.0d);
	}
}
