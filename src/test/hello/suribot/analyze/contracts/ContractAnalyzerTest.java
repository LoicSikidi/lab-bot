package test.hello.suribot.analyze.contracts;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import hello.suribot.analyze.contracts.ContractAnalyzer;

public class ContractAnalyzerTest {
	
	@Test
	public void getCalledMethodTest() {
		ContractAnalyzer analyzer = new ContractAnalyzer();
		assertNull(analyzer.getCalledMethod());
	}

	@Test
	public void isChoiceTest() {
		ContractAnalyzer analyzer = new ContractAnalyzer();
		assertFalse(analyzer.isChoice());
	}

}
