package modules.CA.RDCA;

import imgui.ImFloat;

public class RDCAData { //Jackson JSON Library relies on getters and setters
    public ImFloat diffusionRateA = new ImFloat();
    public ImFloat diffusionRateB = new ImFloat();
    public ImFloat feedRate = new ImFloat();
    public ImFloat killRate = new ImFloat();
    public ImFloat dt = new ImFloat(1);

    public int[][] dimensionRange = new int[2][2];
    public float[][] neighborhoodA = new float[3][3];
    public float[][] neighborhoodB = new float[3][3];

    public float[] chemicalAColor = new float[3];
    public float[] chemicalBColor = new float[3];



    public float getDiffusionRateA() {
        return diffusionRateA.get();
    }

    public void setDiffusionRateA(float diffusionRateA) {
        this.diffusionRateA.set(diffusionRateA);
    }

    public float getDiffusionRateB() {
        return diffusionRateB.get();
    }

    public void setDiffusionRateB(float diffusionRateB) {
        this.diffusionRateB.set(diffusionRateB);
    }

    public float getFeedRate() {
        return feedRate.get();
    }

    public void setFeedRate(float feedRate) {
        this.feedRate.set(feedRate);
    }

    public float getKillRate() {
        return killRate.get();
    }

    public void setKillRate(float killRate) {
        this.killRate.set(killRate);
    }

    public float getDt() {
        return dt.get();
    }

    public void setDt(float dt) {
        this.dt.set(dt);
    }

    public int[][] getDimensionRange() {
        return dimensionRange;
    }

    public void setDimensionRange(int[][] dimensionRange) {
        this.dimensionRange = dimensionRange;
    }

    public float[][] getNeighborhoodA() {
        return neighborhoodA;
    }

    public void setNeighborhoodA(float[][] neighborhoodA) {
        this.neighborhoodA = neighborhoodA;
    }

    public float[][] getNeighborhoodB() {
        return neighborhoodB;
    }

    public void setNeighborhoodB(float[][] neighborhoodB) {
        this.neighborhoodB = neighborhoodB;
    }

    public float[] getChemicalAColor() {
        return chemicalAColor;
    }

    public void setChemicalAColor(float[] chemicalAColor) {
        this.chemicalAColor = chemicalAColor;
    }

    public float[] getChemicalBColor() {
        return chemicalBColor;
    }

    public void setChemicalBColor(float[] chemicalBColor) {
        this.chemicalBColor = chemicalBColor;
    }
}
