package net.algelier.servermanagement.server;

import joptsimple.OptionSet;
import net.algelier.servermanagement.ServerManagement;

import java.io.IOException;

public class ServerManagementServer extends ServerManagement {

    public ServerManagementServer(OptionSet option) throws IOException {
        super(option);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {

    }
}
