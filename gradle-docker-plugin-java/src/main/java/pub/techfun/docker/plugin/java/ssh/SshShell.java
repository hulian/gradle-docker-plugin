package pub.techfun.docker.plugin.java.ssh;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.gradle.api.logging.Logger;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author henry
 */
public class SshShell {

    public static String executeCommand(Logger logger,String url, List<String> commands) {
        try (SshClient client = SshClient.setUpDefaultClient()){
            client.start();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (ClientSession session = client.connect(url).verify(5000).getSession()) {
                session.auth().verify();
                var commandLine = String.join(" ", commands);
                try {
                    session.executeRemoteCommand(commandLine, outputStream, outputStream, StandardCharsets.UTF_8);
                } catch (RemoteException ignore) {
                }
            }
            return "console:" + outputStream.toString(StandardCharsets.UTF_8);
        }catch (Exception e){
            logger.error("java ssh shell error:", e);
            throw new RuntimeException(e);
        }
    }

}
