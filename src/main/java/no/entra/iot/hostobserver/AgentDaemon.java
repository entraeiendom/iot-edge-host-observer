package no.entra.iot.hostobserver;

import no.entra.iot.hostobserver.azure.DeviceTwinClient;
import no.entra.iot.hostobserver.ip.NetworkConfig;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static no.entra.iot.hostobserver.utils.PropertyReader.findProperty;
import static no.entra.iot.hostobserver.utils.PropertyReader.isEmpty;
import static org.slf4j.LoggerFactory.getLogger;

public class AgentDaemon {
    private static final Logger log = getLogger(AgentDaemon.class);
    public static final String NAME = "iot-edge-host-observer";
    public static final String DEVICE_CONNECTION_STRING = "DEVICE_CONNECTION_STRING";
    private DeviceTwinClient deviceTwinClient;

    public AgentDaemon() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                log.info("Shutting down " + NAME);
                deviceTwinClient.disconnect();
                log.info("DONE.");

            }
        });
    }

    public static void main(String[] args) {
        log.info("Starting " + NAME);
        AgentDaemon agentDaemon = new AgentDaemon();
        String iotHubDeviceConnectionString = findConnectionString(args);
        if (isEmpty(iotHubDeviceConnectionString)) {
            log.error("Missing required property: DEVICE_CONNECTION_STRING, exiting ");
        } else {
            agentDaemon.connectToIoTHub(iotHubDeviceConnectionString);
            agentDaemon.reportIpAdressesEveryNSeconds(1000);
        }
    }

    void connectToIoTHub(String iotHubDeviceConnectionString) {
        try {
            deviceTwinClient = new DeviceTwinClient(iotHubDeviceConnectionString);
            deviceTwinClient.connect();
        } catch (URISyntaxException e) {
            log.info("URI Syntax in {}. Failed to connect to IoTHub. Reason: {}", iotHubDeviceConnectionString, e.getMessage());
        } catch (IOException e) {
            log.info("Failed to connect to IoTHub. Reason: {}", e.getMessage());
        }
    }

    void reportIpAdressesEveryNSeconds(int seconds) {
        Timer t = new Timer();
        ReportIpAddresses mTask = new ReportIpAddresses(deviceTwinClient);

        t.scheduleAtFixedRate(mTask, 0, seconds * 1000);
    }

    static String findConnectionString(String[] args) {
        String deviceConnectionString = findProperty(DEVICE_CONNECTION_STRING);
        if (deviceConnectionString == null) {
            if (args.length > 0) {
                deviceConnectionString = args[0];
            }
        }
        return deviceConnectionString;
    }


    class ReportIpAddresses extends TimerTask {

        private final DeviceTwinClient deviceTwinClient;

        public ReportIpAddresses(DeviceTwinClient deviceTwinClient) {
            this.deviceTwinClient = deviceTwinClient;
        }

        @Override
        public void run() {
            Map<String, InetAddress> networkCardConfig = NetworkConfig.findPublicIpAddresses();
            if (networkCardConfig != null) {
                for (String cardName : networkCardConfig.keySet()) {
                    InetAddress inetAddress = networkCardConfig.get(cardName);
                    if (inetAddress != null) {
                        String hostAddress = inetAddress.getHostAddress();
                        log.info("CardName: {}, Ip: {}", cardName, hostAddress);
                        try {
                            deviceTwinClient.updateProperty("IP-" + cardName, hostAddress);
                        } catch (IOException e) {
                            log.info("Failed to update property on IoT Hub. Reason: {}", e.getMessage());
                        }
                    }
                }
            }
        }

    }
}
