package com.dto;

public class ChartData {
    private String[] labels;
    private double[] values;

    public ChartData(String[] labels, double[] values) {
        this.labels = labels;
        this.values = values;
    }

    public String[] getLabels() { return labels; }
    public double[] getValues() { return values; }
}
