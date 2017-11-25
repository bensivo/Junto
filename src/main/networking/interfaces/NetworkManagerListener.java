package main.networking.interfaces;

import main.util.Diff;

public interface NetworkManagerListener {
    void onDiffPacketReceived(Diff diff);
}
