package com.badlogic.drop;

import java.util.ArrayList;
import java.util.Scanner;

public class Shop {
    private ArrayList<ItemShop> itemsDisponibles;

    public Shop() {
        itemsDisponibles = new ArrayList<>();
        // Ajouter des items au Shop (nom, coût)
        itemsDisponibles.add(new ItemShop("Health Necklace", 150));
        itemsDisponibles.add(new ItemShop("Stamina ring", 200));
        itemsDisponibles.add(new ItemShop("Green gun", 300));
        itemsDisponibles.add(new ItemShop("Blue gun", 400));
        itemsDisponibles.add(new ItemShop("Purple gun", 600));
        itemsDisponibles.add(new ItemShop("Yellow gun", 800));
        itemsDisponibles.add(new ItemShop("Red gun", 1000));
    }

    public void afficherItems() {
        System.out.println("Welcome to the Shop !");
        for (int i = 0; i < itemsDisponibles.size(); i++) {
            ItemShop item = itemsDisponibles.get(i);
            System.out.println((i + 1) + ". " + item.getName() + " - cost : " + item.getprice() + " points");
        }
    }

    public boolean acheterItem(int index, Player joueur) {
        if (index >= 0 && index < itemsDisponibles.size()) {
            ItemShop item = itemsDisponibles.get(index);
            if (Player.getCoins() >= item.getprice()) {
                //Player.useCoins(item.getprice()); à intégrer proprement
                Player.ajouterItem(item);
                System.out.println("You buy : " + item.getName());
                return true;
            } else {
                System.out.println("Pas assez de points pour cet achat !");
            }
        } else {
            System.out.println("Choix invalide.");
        }
        return false;
    }
}

