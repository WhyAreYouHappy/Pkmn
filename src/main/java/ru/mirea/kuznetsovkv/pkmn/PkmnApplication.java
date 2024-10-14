package ru.mirea.kuznetsovkv.pkmn;

public class PkmnApplication {
    public static void main(String[] args){
        CardImport importer = new CardImport();
        Card card = importer.importCard("C:\\Users\\Kirill\\IdeaProjects\\Pkmn\\src\\main\\resources\\my_card.txt");

        CardExport exporter = new CardExport();
        exporter.exportCard(card);

        card = importer.importCardByte("C:\\Users\\Kirill\\IdeaProjects\\Pkmn\\Morpeko.crd");
        System.out.println(card.toString());
    }
}
