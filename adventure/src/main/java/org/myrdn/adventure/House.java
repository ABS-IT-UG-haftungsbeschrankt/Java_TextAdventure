package org.myrdn.adventure;

public class House {

    private final int xSize;
    private final int ySize;
    private final Generator generator;
    private final int[][] layout;
    private final int[] startPosition;

    public House(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.generator = new Generator(this.xSize, this.ySize);
        this.layout = generator.getLayout();
        this.startPosition = generator.getStartPosition();
    }

    public int[] getStartPosition() {
        return this.startPosition;
    }

    public int[][] getLayout() {
        return this.layout;
    }

    public String getMap() {
        String text = "";
        char[] symbol = {'█','╻','╸','┓','╹','┃','┛','┫','╺','┏','━','┳','┗','┣','┻','╋'};
        for(int[] row : this.getLayout()) {
            for(int item : row) {
                text += symbol[item];
            }
            text += "\n";
        }
        return text;
    }

}
