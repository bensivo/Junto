package main.networking;

import main.networking.core.DataPacket;
import main.networking.utils.ByteUtils;

/**
 * Manages a single connection between 2 Junto applications
 * This connection is the entry point of external DataPackets, and the exit point of outgoing DataPackets
 *
 * This connection can be either a server or a client.
 */
public class JuntoConnection {
    private static final int PORT = 5001;

    private NetworkManager networkManager;
    private DataPacketRouter dataPacketRouter;

    public JuntoConnection(){
        dataPacketRouter = new DataPacketRouter();
        dataPacketRouter.registerReceiver(new DataPacketRouter.Receiver() {
            @Override
            public boolean shouldReceivePacket(DataPacket dp) {
                return true;
            }

            @Override
            public void receivePacket(DataPacket dp) {
                try{
                    System.out.println("New Packet!");
                    Object obj = ByteUtils.fromBytes(dp.data);
                    System.out.println(obj.toString());
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public DataPacketRouter getDataPacketRouter(){
        return this.dataPacketRouter;
    }

    public NetworkManager getNetworkManager(){
        return this.networkManager;
    }

    public void switchNetworkManager(int type){
        switch(type){
            case NetworkManager.TYPE_CLIENT:
                networkManager = new Client(dataPacketRouter);
                break;
            case NetworkManager.TYPE_SERVER:
                networkManager = new Server(dataPacketRouter);
                break;
        }
    }
}
