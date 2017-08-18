package com.theprogrammingturkey.progressiontweaks.network;

import com.theprogrammingturkey.gobblecore.network.INetworkHandler;
import com.theprogrammingturkey.gobblecore.network.NetworkLoader;

import net.minecraftforge.fml.relauncher.Side;

public class ProgressionPackets implements INetworkHandler
{
	@Override
	public void registerPacket(NetworkLoader loader)
	{
		loader.registerPacket(PacketUdateFirePit.Handler.class, PacketUdateFirePit.class, Side.CLIENT);
	}
}
