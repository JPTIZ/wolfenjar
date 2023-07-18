public enum TextAlign {
    LEFT(0),
    CENTER(1),
    RIGHT(2);

    private int align;

    TextAlign(int align) {
        this.align = align;
    }

    public int get() {
        return this.align;
    }
}
