package hangman;

public class Pair<F, S>{
	private F firstValue;
	private S secondValue;
    
    public void makePair(F firstValue, S secondValue) {
    	this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public F getFirstValue() {
        return firstValue;
    }
    public S getSecondValue() {
        return secondValue;
    }
}
