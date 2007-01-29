package antlr.build;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Adapted from JavaWorld article by Michael Daconta
 */
class StreamScarfer extends Thread {

    InputStream is;
    String type;
    Tool tool;

    StreamScarfer(InputStream is, String type, Tool tool) {
        this.is = is;
        this.type = type;
        this.tool = tool;
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (type == null || "stdout".equals(type)) {
                    tool.stdout(line);
                } else {
                    tool.stderr(line);
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
