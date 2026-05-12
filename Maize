/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agritwinzm2;

/**
 *
 * @author Guess Who
 */
public class Maize {
    double production;
    double storageCapacity;
    double moisture;
    double pricePerTon;
    double lossPercent;

    public Maize(double production, double storageCapacity, double moisture, double pricePerTon, double lossPercent) {
        this.production = production;
        this.storageCapacity = storageCapacity;
        this.moisture = moisture;
        this.pricePerTon = pricePerTon;
        this.lossPercent = lossPercent;
    }

    public double calculateLoss() {
        return production * (lossPercent / 100);
    }

    public double availableMaize() {
        return production - calculateLoss();
    }

    public double calculateRevenue() {
        return availableMaize() * pricePerTon;
    }
}


