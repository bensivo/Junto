package main.networking.interfaces;

import main.optransform.Operation;

public interface NetworkManagerListener {
    //void onDiffPacketReceived(Diff diff);
    void onOperationRecieved(Operation op); //TOOD: Remove the binding to Operations. Route by DP metadata instead
}
