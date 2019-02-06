package test;

import static org.junit.Assert.*;

import org.junit.Test;

import be.vds.documentmover.utils.MyParser;

public class MyParserTest {

	@Test
	public void testEvaluate() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetExtension() {
		MyParser parser = new MyParser();
		parser.evaluate("example.pdf");
		assertEquals("pdf", parser.getExtension());
		parser.evaluate("example.doc");
		assertEquals("doc", parser.getExtension());
		parser.evaluate("example.pdf.tiff");
		assertEquals("tiff", parser.getExtension());
		parser.evaluate("example");
		assertNull(parser.getExtension());
	}
	
	@Test
	public void testFileName() {
		MyParser parser = new MyParser();
		parser.evaluate("example.pdf");
		assertEquals("example.pdf", parser.getFileName());
	}


	@Test
	public void testGetDTG() {
		MyParser parser = new MyParser();
		parser.evaluate("2018_SENDER_example.pdf");
		assertEquals("2018", parser.getDtg());
		parser.evaluate("20180101_SENDER_example.pdf");
		assertEquals("20180101", parser.getDtg());
		parser.evaluate("SENDER_example.pdf");
		assertNull(parser.getDtg());
	}
	
	@Test
	public void testGetSender() {
		MyParser parser = new MyParser();
		parser.evaluate("2018_SENDER_example.pdf");
		assertEquals("SENDER", parser.getSender());
		parser.evaluate("20180101_SENDER.pdf");
		assertEquals("SENDER", parser.getSender());
		parser.evaluate("20180101_This is not a sender.pdf");
		assertNull(parser.getSender());
	}
	
	@Test
	public void testGetDescription() {
		MyParser parser = new MyParser();
		parser.evaluate("2018_SENDER_example.pdf");
		assertEquals("example", parser.getDescription());
		parser.evaluate("2018_SENDER_example-abc.pdf");
		assertEquals("example-abc", parser.getDescription());
		parser.evaluate("2018_SENDER_example bis.pdf");
		assertEquals("example bis", parser.getDescription());
		parser.evaluate("2018_SENDER.pdf");
		assertNull(parser.getDescription());
	}

}
