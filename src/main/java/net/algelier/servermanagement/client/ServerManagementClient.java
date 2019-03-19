package net.algelier.servermanagement.client;

import joptsimple.OptionSet;
import net.algelier.servermanagement.ServerManagement;

import java.io.IOException;

public class ServerManagementClient extends ServerManagement {

    public ServerManagementClient(OptionSet option) throws IOException {
        super(option);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
    }
}
