 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agritwinzm2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Guess Who
 */
public class MainApp {
 
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("=== AgriTwin-ZM Simulation System ===");

        // MAIZE INPUT
        System.out.print("Enter Maize Production (tons): ");
        double maizeProduction = input.nextDouble();

        System.out.print("Enter Maize Storage Capacity: ");
        double maizeStorage = input.nextDouble();

        System.out.print("Enter Maize Moisture Level: ");
        double maizeMoisture = input.nextDouble();

        System.out.print("Enter Maize Price Per Ton: ");
        double maizePrice = input.nextDouble();

        System.out.print("Enter Maize Loss Percent: ");
        double maizeLoss = input.nextDouble();

        // TOMATO INPUT
        System.out.print("Enter Tomato Production (tons): ");
        double tomatoProduction = input.nextDouble();

        System.out.print("Enter Tomato Storage Capacity: ");
        double tomatoStorage = input.nextDouble();

        System.out.print("Enter Tomato Spoilage Rate (%): ");
        double tomatoSpoilage = input.nextDouble();

        System.out.print("Enter Tomato Price Per Ton: ");
        double tomatoPrice = input.nextDouble();

        // DEMAND
        System.out.print("Enter Maize Demand: ");
        double maizeDemand = input.nextDouble();

        System.out.print("Enter Tomato Demand: ");
        double tomatoDemand = input.nextDouble();

        // OBJECT CREATION
        Maize maize = new Maize(maizeProduction, maizeStorage, maizeMoisture, maizePrice, maizeLoss);
        Tomato tomato = new Tomato(tomatoProduction, tomatoStorage, tomatoSpoilage, tomatoPrice);

        double maizeAvailable = maize.availableMaize();
        double tomatoAvailable = tomato.availableTomatoes();

        double maizeRevenue = maize.calculateRevenue();
        double tomatoRevenue = tomato.calculateRevenue();

        String maizeStatus = SupplyDemandEngine.checkSupply(maizeAvailable, maizeDemand);
        String tomatoStatus = SupplyDemandEngine.checkSupply(tomatoAvailable, tomatoDemand);

        // DISPLAY RESULTS
        System.out.println("\n=== Simulation Results ===");

        System.out.println("Available Maize: " + maizeAvailable);
        System.out.println("Maize Market Status: " + maizeStatus);
        System.out.println("Maize Revenue: " + maizeRevenue);

        System.out.println();

        System.out.println("Available Tomatoes: " + tomatoAvailable);
        System.out.println("Tomato Market Status: " + tomatoStatus);
        System.out.println("Tomato Revenue: " + tomatoRevenue);

        // SAVE TO FILE
        try {
            FileWriter writer = new FileWriter("simulation_report.txt");

            writer.write("AgriTwin-ZM Simulation Report\n\n");

            writer.write("Maize Available: " + maizeAvailable + "\n");
            writer.write("\"Maize Status: " + maizeStatus + "\n");
            writer.write("Maize Revenue: " + maizeRevenue + "\n\n");

            writer.write("Tomato Available: " + tomatoAvailable + "\n");
            writer.write("Tomato Status: " + tomatoStatus + "\n");
            writer.write("Tomato Revenue: " + tomatoRevenue + "\n");

            writer.close();

            System.out.println("\nSimulation report saved to file.");

        } catch (IOException e) {
            System.out.println("Error saving file.");
        }

        input.close();
    }
}

