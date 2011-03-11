package no.ovitas.compass2.lt;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import no.ovitas.compass2.exception.ConfigParameterMissingException;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.lt.factory.LTFactory;

import org.junit.Before;
import org.junit.Test;

public class LuceneLTManagerImplTest {

	private LanguageToolsManager ltImplementation;

	@Before
	public void setUp() throws Exception {
		ltImplementation = LTFactory.getInstance().getLTImplementation();
	}

	@Test
	public void testGetSpellingSuggestion() throws ConfigParameterMissingException, ConfigurationException, IOException {
		String spellingSuggestion = ltImplementation.getSpellingSuggestion("knowladge");
		
		System.out.println(spellingSuggestion);
	}

	@Test
	public void testGetSpellingSuggestions() {
		
	}

	@Test
	public void testGetStem() {
		String stem = ltImplementation.getStem("rabbits");
		
		System.out.println(stem);
	}

	@Test
	public void testGetStems() {
		List<String> arrayList = new ArrayList<String>();
		arrayList.add("cats");
		arrayList.add("rat");
		arrayList.add("dogs");
		arrayList.add("lightly");
		arrayList.add("recommended");
		arrayList.add("ilyennemlesz");
		
		Collection<String> stems = ltImplementation.getStems(arrayList);
		
		for (String stem : stems) {
			System.out.println(stem);			
		}
	}

}
