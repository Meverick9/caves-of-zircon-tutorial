package com.example.cavesofzircon;

import com.example.cavesofzircon.view.StartView;
import org.hexworks.zircon.api.SwingApplications;

public class Main {

    public static void main(String[] args) {
        var grid = SwingApplications.startTileGrid(GameConfig.buildAppConfig());
        new StartView(grid).dock();
    }
}
