package util.afd;

import java.io.File;

public interface AFDEventHandler {

    void onHeaderName(byte[] headerNameBytes);

    File onHeaderEnd();

    void onHeaderValue(byte[] headerValueBytes);

    void onPartData(byte[] partDataBytes);

    void onNewPart();
}
