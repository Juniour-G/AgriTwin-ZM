 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agritwinzm2;

/**
 *
 * @author Guess Who
 */
public class Tomato {
    double production;
    double storageCapacity;
    double spoilageRate;
    double pricePerTon;

    public Tomato(double production, double storageCapacity, double spoilageRate, double pricePerTon) {
        this.production = production;
        this.storageCapacity = storageCapacity;
        this.spoilageRate = spoilageRate;
        this.pricePerTon = pricePerTon;
    }

    public double calculateSpoilage() {
        return production * (spoilageRate / 100);
    }

    public double availableTomatoes() {
        return production - calculateSpoilage();
    }

    public double calculateRevenue() {
        return availableTomatoes() * pricePerTon;
    }
}
