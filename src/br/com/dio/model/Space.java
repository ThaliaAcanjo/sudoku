package br.com.dio.model;

public class Space {
    private Integer actual;
    private final int expected;
    private final boolean fixed;

    public Space(int expected, boolean fixed) {
        //this.actual = actual;
        this.expected = expected;
        this.fixed = fixed;
        if (fixed) {
            this.actual = expected;
        }
    }

    public void cleanSpace() {
        setActual(null);
    }

    public Integer getActual() {
        return actual;
    }

    public void setActual(Integer actual) {
        if (fixed)  return;        
        this.actual = actual;
    }

    public int getExpected() {
        return expected;
    }

    public boolean isFixed() {
        return fixed;
    }

    
}
