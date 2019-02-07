package be.jtb.vds.documentmover;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import be.jtb.vds.documentmover.utils.MyParser;

public class MyParserTest {

	@Test
	public void testEvaluate() {
		MyParser parser = new MyParser();
		parser.evaluate("2018_SENDER_example.pdf");
		assertEquals("example", parser.getDescription());
		assertEquals("2018", parser.getDtg());
		assertEquals("SENDER", parser.getSender());
		assertEquals("pdf", parser.getExtension());
				
		parser.evaluate("2018254545_SENDER_example-abc.pdf");
		assertEquals("2018254545_SENDER_example-abc", parser.getDescription());
		assertEquals("pdf", parser.getExtension());
		assertNull(parser.getSender());
		assertNull(parser.getDtg());
		
		
		parser.evaluate("20180101_SENDER-5_example bis.pdf");
		assertEquals("SENDER-5_example bis", parser.getDescription());
		assertEquals("pdf", parser.getExtension());
		assertNull(parser.getSender());
		assertEquals("20180101", parser.getDtg());
		
		parser.evaluate("2018_SENDER5.pdf");
		assertNull(parser.getDescription());
		assertEquals("SENDER5", parser.getSender());
		assertEquals("pdf", parser.getExtension());
		assertEquals("2018", parser.getDtg());
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
