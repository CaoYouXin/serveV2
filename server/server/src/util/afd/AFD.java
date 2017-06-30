package util.afd;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AFD {

    public static final int START = 0;
    public static final int BOUNDARY = 1;
    public static final int HEADER_NAME = 2;
    public static final int HEADER_VALUE = 3;
    public static final int PART_DATA = 4;
    public static final int END = 5;

    public static final int LF = 10;
    public static final int CR = 13;
    public static final int SPACE = 32;
    public static final int HYPHEN = 45;
    public static final int COLON = 58;
    public static final int SEMICOLON = 59;
    public static final int QUATATION = 34;
    public static final int EQUAL = 61;
    public static final int A = 97;
    public static final int Z = 122;

    private AFDEventHandler handler;
    private byte[] boundary;
    private int state;
    // 列计数器
    private int index;
    private boolean boundaryEndFlag;
    private boolean headerEndFlag;
    private boolean headerNameEndFlag;
    private boolean headerValueEndFlag;
    private boolean partDataEndFlag;
    private boolean allEndFlag;
    private BBuf byteBuffer;
    private BBuf partDataBuffer;
    private BufferedOutputStream bufferedOutputStream;

    public AFD(AFDEventHandler handler, byte[] boundary) {
        this.handler = handler;
        this.boundary = boundary;
        this.state = START;
        this.byteBuffer = new BBuf(8 * 1024);
        this.partDataBuffer = new BBuf(8 * 1024);
    }

    public void processRead(byte read) throws IOException {
        switch (this.state) {
            case START:
                this.index = 2;
                this.state = BOUNDARY;
            case BOUNDARY:
                if (this.boundaryEndFlag) {
                    this.boundaryEndFlag = false;
                    if (read == LF) {
                        this.index = 0;
                        this.state = HEADER_NAME;
                        break;
                    } else {
                        throw new RuntimeException("request parse error, near header, expect LF but get [" + read + "].");
                    }
                }

                if (this.index < this.boundary.length) {
                    if (this.boundary[this.index] != read) {
                        throw new RuntimeException("request parse error, near boundary. expect "
                                + this.boundary[this.index] + " , but get " + read + " ");
                    }
                    this.index++;
                } else if (read == CR) {
                    this.handler.onNewPart();
                    this.boundaryEndFlag = true;
                } else {
                    throw new RuntimeException("request parse error, near boundary.");
                }
                break;
            case HEADER_NAME:
                if (this.headerEndFlag) {
                    this.headerEndFlag = false;
                    if (read == LF) {
                        this.index = 0;
                        this.state = PART_DATA;
                        break;
                    } else {
                        throw new RuntimeException("request parse error, near header, expect LF but get [" + read + "].");
                    }
                }

                if (this.headerNameEndFlag) {
                    this.headerNameEndFlag = false;
                    if (read == SPACE) {
                        this.state = HEADER_VALUE;
                        break;
                    } else {
                        throw new RuntimeException("request parse error, near header, expect SPACE but get [" + read + "].");
                    }
                }

                if (read == CR) {
                    File file = this.handler.onHeaderEnd();
                    if (null != file) {
                        this.bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
                    }
                    this.headerEndFlag = true;
                } else if (read == COLON) {
                    this.handler.onHeaderName(this.byteBuffer.flush());
                    this.headerNameEndFlag = true;
                } else {
                    this.byteBuffer.append(read);
                }
                break;
            case HEADER_VALUE:
                if (this.headerValueEndFlag) {
                    this.headerValueEndFlag = false;
                    if (read == LF) {
                        this.state = HEADER_NAME;
                        break;
                    } else {
                        throw new RuntimeException("request parse error, near header, expect LF but get [" + read + "].");
                    }
                }

                if (read == CR) {
                    this.handler.onHeaderValue(this.byteBuffer.flush());
                    this.headerValueEndFlag = true;
                } else {
                    this.byteBuffer.append(read);
                }
                break;
            case PART_DATA:
                if (this.partDataEndFlag) {
                    this.partDataEndFlag = false;
                    if (read == LF) {
                        this.index = 0;
                        this.state = HEADER_NAME;
                        break;
                    } else {
                        throw new RuntimeException("request parse error, near header, expect LF but get [" + read + "].");
                    }
                }

                if (this.allEndFlag) {
                    this.allEndFlag = false;
                    if (read == HYPHEN) {
                        this.state = END;
                        break;
                    } else {
                        throw new RuntimeException("request parse error, near header, expect HYPHEN but get [" + read + "].");
                    }
                }

                if (this.index < this.boundary.length) {
                    if (this.boundary[this.index] == read) {
                        this.byteBuffer.append(read);
                        this.index++;
                    } else {
                        if (this.index > 0) {
                            if (null != this.bufferedOutputStream) {
                                this.bufferedOutputStream.write(this.byteBuffer.flush());
                            } else {
                                this.partDataBuffer.append(this.byteBuffer.flush());
                            }
                            this.index = 0;
                            this.processRead(read);
                        } else {
                            if (null != this.bufferedOutputStream) {
                                this.bufferedOutputStream.write(read);
                            } else {
                                this.partDataBuffer.append(read);
                            }
                        }
                    }
                } else if (this.index == this.boundary.length) {
                    if (read == CR) {
                        this.handler.onNewPart();
                        this.partDataEndFlag = true;
                    } else if (read == HYPHEN) {
                        this.allEndFlag = true;
                    }

                    if (read == CR || read == HYPHEN) {
                        this.byteBuffer.flush();
                        if (null != this.bufferedOutputStream) {
                            this.bufferedOutputStream.flush();
                            this.bufferedOutputStream.close();
                            this.bufferedOutputStream = null;
                        } else {
                            this.handler.onPartData(this.partDataBuffer.flush());
                        }
                    }
                }
                break;
            case END:
                break;
            default:
                break;
        }
    }

}
