package no.entra.iot.hostobserver;

import no.entra.iot.hostobserver.ip.NetworkConfig;
import org.slf4j.Logger;

import java.net.InetAddress;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class AgentDaemon {
    private static final Logger log = getLogger(AgentDaemon.class);
    public static void main(String[] args) {
        Map<String, InetAddress> networkCardConifg = NetworkConfig.findPublicIpAddresses();
        if (networkCardConifg != null) {
            for (String cardName : networkCardConifg.keySet()) {
                InetAddress inetAddress = networkCardConifg.get(cardName);
                if (inetAddress != null) {
                    log.info("CardName: {}, Ip: {}", cardName, inetAddress.getHostAddress());
                }
            }
        }
    }
}
