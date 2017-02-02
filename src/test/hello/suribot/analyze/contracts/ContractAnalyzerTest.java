package test.hello.suribot.analyze.contracts;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.json.JSONObject;
import org.junit.Test;

import hello.suribot.analyze.IntentsAnalyzer;
import hello.suribot.analyze.contracts.ContractAnalyzer;

public class ContractAnalyzerTest {
	
	@Test
	public void analyzeTest(){
		ContractAnalyzer analyzer = new ContractAnalyzer();
		assertFalse(analyzer.analyze(new JSONObject(), null).optBoolean(IntentsAnalyzer.SUCCESS, false));
		
		analyzer = new ContractAnalyzer();
		assertFalse(analyzer.analyze(new JSONObject(), null).optBoolean(IntentsAnalyzer.SUCCESS, false));
	}

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
