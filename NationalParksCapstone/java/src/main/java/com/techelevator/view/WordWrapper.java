package com.techelevator.view;

public class WordWrapper {
	public static String wrapMyString(String string) {
		int stringMin = 75;
		int stringMax = 95;
		String str = string;
		int strStart = (str.lastIndexOf("\n")) != -1 ? str.lastIndexOf("\n") : 0;
		int breakSpot = 0;

		if (str.length() - strStart > stringMax) {
			if (str.substring(strStart + stringMin, strStart + stringMax).indexOf(" ") == -1) {
				breakSpot = ((stringMax - stringMin) / 2);
				String hyph1 = str.substring(0, strStart + stringMin + breakSpot) + "-\n";
				String hyph2 = str.substring(strStart + stringMin + breakSpot);
				str = hyph1 + hyph2;
				strStart = str.lastIndexOf("\n");
			} else {
				breakSpot = str.substring(strStart + stringMin, strStart + stringMax).indexOf(" ");
				String str1 = str.substring(0, strStart + stringMin + breakSpot) + "\n";
				String str2 = str.substring(strStart + stringMin + breakSpot + 1);
				str = str1 + str2;
				strStart = str.lastIndexOf("\n");
			}
		} else {
			return str;
		}
		
		if (str.length() - strStart > stringMax) {

			return wrapMyString(str); // Fun with Recursion

		} else {
			return str;
		}
	}
}
