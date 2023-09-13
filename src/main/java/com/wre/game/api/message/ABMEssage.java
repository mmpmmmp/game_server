package com.wre.game.api.message;

public class ABMEssage {
    private int weight;
    private String name;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DefaultGroup{" +
                "weight=" + weight +
                ", name='" + name + '\'' +
                '}';
    }




}
