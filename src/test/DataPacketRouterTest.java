package test;

import main.networking.DataPacketRouter;
import main.networking.core.DataPacket;
import org.junit.jupiter.api.Test;

import static main.networking.core.DataPacket.TYPE_AUTH;
import static main.networking.core.DataPacket.TYPE_OP;
import static org.junit.jupiter.api.Assertions.*;

class DataPacketRouterTest {
    class ReceiveAll implements DataPacketRouter.Receiver {
        public boolean called = false;

        @Override
        public boolean shouldReceivePacket(DataPacket dp) {
            return true;
        }

        @Override
        public void receivePacket(DataPacket dp) {
            called = true;
        }
    }

    class ReceiveOps implements DataPacketRouter.Receiver{
        public boolean called = false;

        @Override
        public boolean shouldReceivePacket(DataPacket dp) {
            return dp.type == TYPE_OP;
        }

        @Override
        public void receivePacket(DataPacket dp) {
            called = true;
        }

    }

    @Test
    void routingWorks(){
        DataPacketRouter router = new DataPacketRouter();
        ReceiveAll ra = new ReceiveAll();
        ReceiveOps ro = new ReceiveOps();

        router.registerReceiver(ra);
        router.registerReceiver(ro);
        router.routeDataPacket(new DataPacket("Hello world!".getBytes(), TYPE_AUTH, null));

        assertTrue(ra.called);
        assertFalse(ro.called);

        ra.called = false;
        ro.called = false;

        router.routeDataPacket(new DataPacket("Hello world!".getBytes(), TYPE_OP, null));
        assertTrue(ra.called);
        assertTrue(ro.called);
    }


}