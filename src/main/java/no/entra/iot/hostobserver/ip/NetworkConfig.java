package no.entra.iot.hostobserver.ip;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static java.net.NetworkInterface.getNetworkInterfaces;

public class NetworkConfig {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(NetworkConfig.class);

    public static Map<String,InetAddress> findPublicIpAddresses() {
        Map<String,InetAddress> cardNameAndIp = new HashMap<>();
        try {
            for (Enumeration ifaces = getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // Iterate all IP addresses assigned to each card...
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {

                            String cardName = iface.getName();
                            log.trace("networkCardName: {}, ip: {} ", cardName, inetAddr.getHostAddress() );
                            cardNameAndIp.put(cardName, inetAddr);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.info("Could not find IpAddress. Reason: {}", e.getMessage());
        }
        return cardNameAndIp;
    }
}
