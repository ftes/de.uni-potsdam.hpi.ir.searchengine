package de.hpi.krestel.mySearchEngine;

import de.hpi.krestel.mySearchEngine.search.SnippetGenerator;

public class SnippetGeneratorTest {

	public static void main(String[] args) {
		String text = "Der Film ''[[Supernova (2000)|Supernova]]'' ist der erste Post-Smithee-Film, "
					+ "dort führte ein gewisser ''Thomas Lee'' alias [[Walter Hill]] die Regie. Zu den Regisseuren, die das Pseudonym benutzt haben, gehören: "
					+ "* [[Don Siegel]] und [[Robert Totten]] (für ''[[Frank Patch – Deine Stunden sind gezählt]]''), "
					+ "* [[David Lynch]] (für die dreistündige Fernsehfassung von ''[[Der Wüstenplanet (Film)|Der Wüstenplanet]]''), "
					+ "== Weblinks == "
					+ "* {{IMDb Name|ID=0000647|NAME=Alan Smithee}} test44 {{IMDb Name|ID=0000647|NAME=Alan Smithee}}"
					+ "* [http://www.abc.net.au/rn/arts/atoday/stories/s353584.htm Artikel über Smithee von ABC Online (englisch)]";
		
		
//		String text2 = "In einer neuen Trilogie setzt er sich mit unterschiedlichen Kulturen auseinander: "
//						+ "* ''[[Sinn und Sinnlichkeit (1995)|Sinn und Sinnlichkeit]]'' ist die Verfilmung eines Romans der englischen Schriftstellerin [[Jane Austen]] "
//						+ "* ''[[Der Eissturm]]'' spielt in den USA der 1970er Jahre "
//						+ "* ''[[Ride with the Devil]]'' ist im [[Sezessionskrieg|amerikanischen Bürgerkrieg]] angesiedelt";
		String query = "dort";
//		String query2 = "roman";
		SnippetGenerator sg = new SnippetGenerator(text, query);
		System.out.println(sg.generate());
	}

}
