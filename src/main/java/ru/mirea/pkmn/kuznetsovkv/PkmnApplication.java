package ru.mirea.pkmn.kuznetsovkv;

import ru.mirea.pkmn.Card;

public class PkmnApplication {
    public static void main(String[] args){
        CardImport importer = new CardImport();
        Card card = importer.importCard("C:\\Users\\Kirill\\IdeaProjects\\Pkmn\\src\\main\\resources\\my_card.txt");

        CardExport exporter = new CardExport();
        exporter.exportCard(card);

        card = importer.importCardByte("C:\\Users\\Kirill\\IdeaProjects\\Pkmn\\Magmortar.crd");
        System.out.println(card.toString());
    }
}