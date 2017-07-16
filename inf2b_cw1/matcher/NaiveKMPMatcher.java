package matcher;

public class NaiveKMPMatcher {

	private String pattern;
	private int patternLen;
	private int[] prefixFunction;

	public NaiveKMPMatcher(String pattern) {
		this.pattern = pattern;
		this.patternLen = pattern.length();
		this.prefixFunction = new int[patternLen + 1];
	}

	public int[] getPrefixFunction() {
		return prefixFunction;
	}

	public boolean matchBothTerminiOfPrefix(int shift, int prefixLen) {
		if (prefixLen >= shift)
			return false;
		int offset1 = 0;
		int offset2 = shift - prefixLen;
		while (offset1 < prefixLen && offset2 < shift) {
			if (pattern.charAt(offset1) != pattern.charAt(offset2))
				return false;
			else {
				offset1++;
				offset2++;
			}
		}
		return true;
	}

	public void buildPrefixFunction() {
		prefixFunction[0] = 0;
		for (int shift = 2; shift <= patternLen; shift++) {
			int prefixLen = shift - 1;
			while (!matchBothTerminiOfPrefix(shift, prefixLen) && prefixLen > 0)
				prefixLen--;
			prefixFunction[shift - 1] = prefixLen;
		}
	}

}