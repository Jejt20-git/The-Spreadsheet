public class NumericContent extends Content {
    public final Double number;
    public NumericContent(Double number) { this.number = number; }
    @Override public String raw() { return number == null ? "" : number.toString(); }
    @Override public String toString() { return raw(); }
}
