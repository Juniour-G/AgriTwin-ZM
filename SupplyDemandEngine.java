/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agritwinzm2;

/**
 *
 * @author Guess Who
 */
public class SupplyDemandEngine {
    public static String checkSupply(double supply, double demand) {
        if (supply > demand) {
            return "Surplus";
        }
        else if (supply < demand) {
            return "Shortage";
        }
        else {
            return "Balance";
        }
    }
}
