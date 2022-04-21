package io.github.zrdzn.minecraft.lovelydrop.menu;

import java.util.List;
import java.util.Map.Entry;

public class Menu {

    private final String title;
    private final int rows;
    private final MenuFiller filler;
    private final Entry<String, String> dropSwitch;
    private final List<MenuItem> items;

    public Menu(String title, int rows, MenuFiller filler, Entry<String, String> dropSwitch, List<MenuItem> items) {
        this.title = title;
        this.rows = rows;
        this.filler = filler;
        this.dropSwitch = dropSwitch;
        this.items = items;
    }

    public String getTitle() {
        return this.title;
    }

    public int getRows() {
        return this.rows;
    }

    public MenuFiller getFiller() {
        return this.filler;
    }

    public Entry<String, String> getDropSwitch() {
        return this.dropSwitch;
    }

    public List<MenuItem> getItems() {
        return this.items;
    }

}
