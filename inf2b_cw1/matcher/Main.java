package matcher;

public class Main {

	public static void main(String args[]) {
        Matcher.getRuntimes(10, 100, "matcherTimes");
        System.out.println(Matcher.KMPMatcherCheck("ababbababa"));
        Matcher.KMPMatcherTest(20,4);
		Matcher.getRatios(10, 100, "matcherRatios");
        Matcher.plotRuntimes(0.750986, 0.045151, "matcherTimes");
	}
}
