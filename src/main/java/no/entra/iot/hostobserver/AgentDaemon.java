package no.entra.iot.hostobserver;

import no.entra.iot.hostobserver.ip.NetworkConfig;
import org.slf4j.Logger;

import java.net.InetAddress;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static org.slf4j.LoggerFactory.getLogger;

public class AgentDaemon {
    private static final Logger log = getLogger(AgentDaemon.class);
    public static final String NAME = "iot-edge-host-observer";

    //TODO send to Azure IoT
    public static void main(String[] args) {
        log.info("Starting " + NAME);
        AgentDaemon agentDaemon = new AgentDaemon();
        agentDaemon.reportIpAdressesEveryNSeconds(10);
    }

    private void reportIpAdressesEveryNSeconds(int seconds) {
        Timer t = new Timer();
        ReportIpAddresses mTask = new ReportIpAddresses();

        t.scheduleAtFixedRate(mTask, 0, seconds * 1000);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                log.info("Shutting down " + NAME);
            }
        });
    }


    class ReportIpAddresses extends TimerTask {

        public ReportIpAddresses() {
            //Some stuffs
        }

        @Override
        public void run() {
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
}
