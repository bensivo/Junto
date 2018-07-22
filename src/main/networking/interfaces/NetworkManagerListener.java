package main.networking.interfaces;

import main.optransform.Diff;

public interface NetworkManagerListener {
    void onDiffPacketReceived(Diff diff);
}
