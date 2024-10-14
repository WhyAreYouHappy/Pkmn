package ru.mirea.kuznetsovkv.pkmn;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CardExport {
    public void exportCard(Card card) {
        String cardpath = card.getName() + ".crd";

        try (FileOutputStream outputFile = new FileOutputStream(cardpath);
            ObjectOutputStream outputObject = new ObjectOutputStream(outputFile)) {
            outputObject.writeObject(card);
            System.out.println("Карта экпортрована в:" + cardpath);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
