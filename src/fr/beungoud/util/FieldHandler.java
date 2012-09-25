package fr.beungoud.util;

import java.util.ArrayList;
import java.util.List;

public class FieldHandler {

	List<String[]> result = new ArrayList<String[]>();

	private static final String DELIM1 = "<field>";
	private static final String DELIM2 = "</field>";

	public void parse(String inputString, int nbFieldsPerLine) {
		boolean finished = false;
		int position = 0;
		while (!finished) {
			String[] Ligne = new String[nbFieldsPerLine];
			for (int i = 0; i < nbFieldsPerLine; i++) {
				int positionDebut = inputString.indexOf(DELIM1, position) + DELIM1.length();
				int positionFin = inputString.indexOf(DELIM2, position);
				if (positionDebut == -1 || positionFin == -1)
				{
					finished = true;
					break;
				} else {
					Ligne[i] = inputString.substring(positionDebut, positionFin);

					position = positionFin + DELIM2.length();
				}
			}
			if (!finished)
				result.add(Ligne);
		}
	}

	public List<String[]> getResult() {
		return result;
	}

}
