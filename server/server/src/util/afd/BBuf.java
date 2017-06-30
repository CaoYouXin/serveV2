package util.afd;

public class BBuf {

    private byte[] byteBuffer;
    private int byteBufferLen;

    public BBuf(int capacity) {
        this.byteBuffer = new byte[capacity];
    }

    public byte[] flush() {
        byte[] buffer = new byte[this.byteBufferLen];
        System.arraycopy(this.byteBuffer, 0, buffer, 0, this.byteBufferLen);

        this.byteBufferLen = 0;
        this.byteBuffer = new byte[8 * 1024];

        return buffer;
    }

    public void append(byte b) {
        if (this.byteBufferLen == this.byteBuffer.length) {
            byte[] newBuffer = new byte[this.byteBuffer.length * 2];
            System.arraycopy(this.byteBuffer, 0, newBuffer, 0, this.byteBufferLen);
            this.byteBuffer = newBuffer;
        }

        this.byteBuffer[this.byteBufferLen] = b;
        this.byteBufferLen++;
    }

    public void append(byte[] bs) {
        if (this.byteBufferLen + bs.length >= this.byteBuffer.length) {
            byte[] newBuffer = new byte[Math.max(
                    this.byteBuffer.length * 2,
                    this.byteBufferLen + bs.length)];
            System.arraycopy(this.byteBuffer, 0, newBuffer, 0, this.byteBufferLen);
            this.byteBuffer = newBuffer;
        }

        System.arraycopy(this.byteBuffer, this.byteBufferLen, bs, 0, bs.length);
        this.byteBufferLen++;
    }

}
