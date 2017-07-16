package matcher;

import java.util.*;
import java.io.*;

import javax.swing.JFrame;
import javax.swing.RowFilter;

@SuppressWarnings("unused")
public class Matcher {

	private static Queue matchNaively(String text, String pattern) {
		NaiveMatcher matcher = new NaiveMatcher(text, pattern);
		matcher.search();
		return matcher.getMatchIndices();
	}

	private static long getNaiveMatchRuntime(String text, String pattern) {
		NaiveMatcher matcher = new NaiveMatcher(text, pattern);
		long start = System.nanoTime();
		matcher.search();
		long stop = System.nanoTime();
		return (stop - start) / 1000;
	}

	private static Queue matchKMP(String text, String pattern) {
		NaiveMatcher matcher = new NaiveMatcher(text, pattern);
		// matcher.buildPrefixFunction();
		matcher.search();
		return matcher.getMatchIndices();
	}

	public static long getKMPMatchRuntime(String text, String pattern) {
		StudentClass matcher = new StudentClass(text, pattern);
		long start = System.nanoTime();
		matcher.buildPrefixFunction();
		// long start = System.nanoTime();
		matcher.search();
		long stop = System.nanoTime();
		return (stop - start) / 1000;
	}

	private static Queue matchStudent(String text, String pattern) {
		StudentClass matcher = new StudentClass(text, pattern);
		matcher.buildPrefixFunction();
		matcher.search();
		return matcher.getMatchIndices();
	}

	private static long getStudentMatchRuntime(String text, String pattern) {
		StudentClass matcher = new StudentClass(text, pattern);
		long start = System.nanoTime();
		matcher.buildPrefixFunction();
		matcher.search();
		long stop = System.nanoTime();
		return (stop - start) / 1000;
	}

	public static String prefFuncToString(int[] preffunc) {
		String result = "[";
		int contentSize = preffunc.length;
		for (int index = 0; index < contentSize; index++) {
			if (index != 0)
				result += ", ";
			result += preffunc[index];
		}
		result += "]";
		return result;
	}

	public static int[] buildPrefixFunctionNaively(String pattern) {
		NaiveKMPMatcher matcher = new NaiveKMPMatcher(pattern);
		matcher.buildPrefixFunction();
		return matcher.getPrefixFunction();
	}

	public static String buildPrefixFunction(String pattern) {
		StudentClass matcher = new StudentClass("", pattern);
		matcher.buildPrefixFunction();
		int[] preffunc = matcher.getPrefixFunction();
		return prefFuncToString(preffunc);
	}

	private static Boolean checkPrefFunc(int[] solution, int[] answer) {
		if (solution.length != answer.length)
			return false;
		for (int i = 0; i < solution.length; i++) {
			if (solution[i] != answer[i])
				return false;
		}
		return true;
	}

	private static Boolean checkMatches(Queue solution, Queue answer) {
		return solution.equals(answer);
	}

	public static Boolean KMPMatcherCheck(String pattern) {
		NaiveKMPMatcher matcher = new NaiveKMPMatcher(pattern);
		matcher.buildPrefixFunction();
		int[] solution = matcher.getPrefixFunction();
		StudentClass student = new StudentClass("", pattern);
		student.buildPrefixFunction();
		int[] answer = student.getPrefixFunction();
		return checkPrefFunc(solution, answer);
	}

	public static void KMPMatcherTest(int noRandomPatterns, int patternLen) {
		Random randomizer = new Random();
		boolean isSuccessful = true;
		for (int i = 0; i < noRandomPatterns; i++) {
			int exId = randomizer.nextInt(ExampleData.examples.length);
			String text = ExampleData.examples[exId];
			int sentlen = text.length();
			int begpos = randomizer.nextInt(sentlen - patternLen);
			String pattern = text.substring(begpos, begpos + patternLen);
			System.out.println("Pattern " + (exId + 1) + ": " + pattern);
			Queue solution = matchKMP(text, pattern);
			System.out.println("Solution: " + solution.toString());
			Queue answer = matchStudent(text, pattern);
			System.out.println("Given answer: " + answer.toString());
			if (checkMatches(solution, answer)) {
				System.out.println("Correct answer");
				System.out.println();
			} else {
				System.out.println("--- INCORRECT ANSWER ---");
				System.out.println();
				System.out.println("Matcher test unsuccessful");
				isSuccessful = false;
				break;
			}
		}
		if (isSuccessful) {
			System.out.println("Matcher test successful");
		}
	}

	private static Vector<Vector<String>> genTestPatterns(String text, int noPatterns, int sizeExp) {
		String testStr = text;
		Vector<Vector<String>> result = new Vector<Vector<String>>();
		for (int i = 1; i < sizeExp; i++) {
			int size = (int) Math.pow(10, i);
			Vector<String> row = new Vector<String>();
			while (testStr.length() < size + noPatterns)
				testStr += text;
			for (int j = 0; j < noPatterns; j++)
				row.add(testStr.substring(j, j + size));
			result.add(row);
		}
		return result;
	}

	private static String genTestText(String text, int size) {
		String result = text;
		while (result.length() < size)
			result += text;
		return result.substring(0, size);
	}

	private static int findMaxSizeExp(int length) {
		int i = 0;
		int halfLen = (length + 1) / 2;
		while (Math.pow(10, i) <= halfLen)
			i++;
		return i;
	}

	private static float[] recordNaiveRuntimes(String searchText, Vector<Vector<String>> testPatterns) {
		float[] result = new float[testPatterns.size()];
		for (int i = 0; i < testPatterns.size(); i++) {
			Vector<String> row = testPatterns.get(i);
			long oldruntime = -1;
			int datasize = row.size();
			for (int j = 0; j < row.size(); j++) {
				long newruntime = getNaiveMatchRuntime(searchText, row.get(j));
				if (oldruntime < 0 || oldruntime < newruntime)
					oldruntime = newruntime;
			}
			result[i] = (float) oldruntime;
		}
		return result;
	}

	private static float[] recordKMPRuntimes(String searchText, Vector<Vector<String>> testPatterns) {
		float[] result = new float[testPatterns.size()];
		Vector<Float> test = new Vector<Float>();
		for (int i = 0; i < testPatterns.size(); i++) {
			Vector<String> row = testPatterns.get(i);
			long oldruntime = -1;
			int datasize = row.size();
			for (int j = 0; j < row.size(); j++) {
				// long newruntime = getStudentMatchRuntime(searchText, row.get(j));
				long newruntime = getKMPMatchRuntime(searchText, row.get(j));
				if (oldruntime < 0 || oldruntime < newruntime)
					oldruntime = newruntime;
			}
			result[i] = (float) oldruntime;
			test.add((float) oldruntime);
		}
		return result;
	}

	private static float recordNaiveRatio(String searchText, Vector<Vector<String>> testPatterns) {
		long oldruntime = -1;
		for (int i = 0; i < testPatterns.size(); i++) {
			Vector<String> row = testPatterns.get(i);
			int datasize = row.size();
			for (int j = 0; j < row.size(); j++) {
				long newruntime = getNaiveMatchRuntime(searchText, row.get(j));
				if (oldruntime < 0 || oldruntime < newruntime)
					oldruntime = newruntime;
			}
		}
		return (float) oldruntime / searchText.length();
	}

	private static float recordKMPRatio(String searchText, Vector<Vector<String>> testPatterns) {
		long oldruntime = -1;
		for (int i = 0; i < testPatterns.size(); i++) {
			Vector<String> row = testPatterns.get(i);
			int datasize = row.size();
			for (int j = 0; j < row.size(); j++) {
				long newruntime = getKMPMatchRuntime(searchText, row.get(j));
				if (oldruntime < 0 || oldruntime < newruntime)
					oldruntime = newruntime;
			}
		}
		return (float) oldruntime / searchText.length();
	}

	private static void writeRuntimeEntries(int textLen, Vector<Vector<String>> patterns, float[] naiveRuntimes, float[] kmpRuntimes, BufferedWriter output) throws IOException {
		for (int i = 0; i < patterns.size(); i++) {
			int patLen = patterns.get(i).get(0).length();
			output.write(
				new Formatter(new StringBuilder()).format(
					"%10d\t%10d\t%10.0f\t%10.0f\n",
					patLen, textLen,
					naiveRuntimes[i], kmpRuntimes[i]
				).toString()
			);
			output.flush();
		}
	}

	private static void writeRatioEntry(int textLen, float kmpRatio, BufferedWriter output) throws IOException {
		output.write(
			new Formatter(new StringBuilder()).format(
				"%20d\t%10.6f\n", textLen, kmpRatio
			).toString()
		);
	}

	private static void writeRatioSummary(float[] ratios, BufferedWriter output) throws IOException {
		Arrays.sort(ratios);
		output.write("Sorted ratios are: ");
		float total = 0;
		for (int i = 0; i < ratios.length; i++) {
			if (i != 0)
				output.write(", ");
			if (i % 5 == 0)
				output.newLine();
			output.write(new Formatter(new StringBuilder()).format(
					"%10.6f",
					ratios[i]
				).toString());
			total = total + ratios[i];
		}
		output.newLine();
		output.write(new Formatter(new StringBuilder()).format(
				"Maximum stable ratio is: %10.6f\n",
				ratios[ratios.length - 1]
			).toString());
		output.write(new Formatter(new StringBuilder()).format(
				"Average ratio is: %10.6f\n",
				(total / ratios.length)
			).toString());
	}

	public static void getRuntimes(int noPatterns, int textSizeCoeff, String outputPath) {
		try {
			System.out.println(">> Now measuring the runtimes.");
			BufferedWriter ofhdl = new BufferedWriter(new FileWriter(outputPath));
			String text = ExampleData.longDnaSeq3;
			String testText = "";
			String searchText = "";
			ofhdl.write("//  PatLen\t   TextLen\t  Naive-RT\t    KMP-RT\n");
			for (int i = 1; i <= textSizeCoeff; i++) {
				int size = 10000 * i;
				System.out.print("   >> Running with text of " + size + " characters ... ");
				System.out.flush();
				while (testText.length() <= size)
					testText += text;
				searchText = testText.substring(0, size);
				int sizeExp = findMaxSizeExp(searchText.length());
				Vector<Vector<String>> patterns = genTestPatterns(searchText, noPatterns, sizeExp);
				float[] naiveRuntimes = recordNaiveRuntimes(searchText, patterns);
				float[] kmpRuntimes = recordKMPRuntimes(searchText, patterns);
				writeRuntimeEntries(searchText.length(), patterns, naiveRuntimes, kmpRuntimes, ofhdl);
				System.out.println("done");
				System.out.flush();
			}
			ofhdl.close();
			System.out.println(">> Runtime measurement done.\n");
			System.out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void getRatios(int noPatterns, int textSizeCoeff, String outputPath) {
		try {
			System.out.println(">> Now measuring the ratios.");
			BufferedWriter ofhdl = new BufferedWriter(new FileWriter(outputPath));
			String text = ExampleData.longDnaSeq3;
			String testText = "";
			String searchText = "";
			ofhdl.write("//            PatLen\t     Ratio\n");
			float[] ratios = new float[textSizeCoeff];
			for (int i = 1; i <= textSizeCoeff; i++) {
				int size = 10000 * i;
				System.out.print("   >> Running with text of " + size + " characters ... ");
				System.out.flush();
				while (testText.length() <= size)
					testText += text;
				searchText = testText.substring(0, size);
				int sizeExp = findMaxSizeExp(searchText.length());
				Vector<Vector<String>> patterns = genTestPatterns(searchText, noPatterns, sizeExp);
				float kmpRatio = recordKMPRatio(searchText, patterns);
				writeRatioEntry(searchText.length(), kmpRatio, ofhdl);
				ratios[i - 1] = kmpRatio;
				System.out.println("done");
				System.out.flush();
			}
			writeRatioSummary(ratios, ofhdl);
			ofhdl.close();
			System.out.println(">> Ratio measurement done.\n");
			System.out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void plotRuntimes(double maxRatio, double avgRatio, String inputRuntimesPath) {
		try {
			BufferedReader ifhdl = new BufferedReader(new FileReader(inputRuntimesPath));
			Hashtable<Integer, Integer> maxTbl = new Hashtable<Integer, Integer>();
			try {
				while (true) {
					String line = ifhdl.readLine();
					if (line == null) break;
					line = line.trim();
					if (line.startsWith("//")) continue;
					String[] tokens = line.split("[ \t]+");
					if (tokens.length == 0) continue;
					if (tokens.length != 4) throw new IllegalArgumentException();
					int patLen = Integer.parseInt(tokens[0]);
					int textLen = Integer.parseInt(tokens[1]);
					int naiveRT = Integer.parseInt(tokens[2]);
					int kmpRT = Integer.parseInt(tokens[3]);
					if (!maxTbl.containsKey(textLen))
						maxTbl.put(textLen, Integer.MAX_VALUE);
					if (maxTbl.get(textLen) > kmpRT) {
						maxTbl.remove(textLen);
						maxTbl.put(textLen, kmpRT);
					}
				}
			} catch (IOException e) {
			}
			double[][] data = new double[maxTbl.size()][2];
			Integer[] keys = new Integer[maxTbl.size()];
			keys = maxTbl.keySet().toArray(keys);
			Arrays.sort(keys);
			int textLen = 0;
			int kmpRT = 0;
			for (int i = 0; i < keys.length; i++) {
				textLen = keys[i];
				kmpRT = maxTbl.get(textLen);
				data[i][0] = textLen;
				data[i][1] = ((double) kmpRT) / 1000;
			}
			JFrame f = new JFrame();
		    f.setTitle("KMP Runtime Plot");
		    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    GraphingData gd = new GraphingData(1.0, data, 1, 10, maxRatio, avgRatio, "plot.jpg");
		    f.add(gd);
		    f.setSize(800,800);
		    f.setLocation(200,200);
		    f.setVisible(true);
		    gd.save();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.err.println(">> Illegal file format: Graph plotter has terminated unexpectedly.");
		}
	}

}
