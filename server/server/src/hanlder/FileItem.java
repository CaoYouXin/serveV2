package hanlder;

import java.io.File;
import java.util.*;

public class FileItem {

    private boolean isFile;
    private List<FileItemHeader> headers = new ArrayList<>();
    private File file;
    private String partValue;

    @Override
    public String toString() {
        return "FileItem{" +
                "isFile=" + isFile +
                ", headers=" + Arrays.toString(headers.toArray()) +
                ", file=" + file +
                ", partValue='" + partValue + '\'' +
                '}';
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getPartValue() {
        return partValue;
    }

    public void setPartValue(String partValue) {
        this.partValue = partValue;
    }

    public void addHeader(FileItemHeader fileItemHeader) {
        this.headers.add(fileItemHeader);
    }

    public FileItemHeader getLastHeader() {
        if (this.headers.size() > 0) {
            return this.headers.get(this.headers.size() - 1);
        }
        return null;
    }

    static class FileItemHeader {
        private String name;
        private String value;
        private String firstValue;
        private Map<String, String> kvs = new HashMap<>();

        @Override
        public String toString() {
            StringJoiner sj = new StringJoiner("; ", "{", "}");
            for (String key : kvs.keySet()) {
                sj.add(key + "=" + kvs.get(key));
            }
            return "FileItemHeader{" +
                    "name='" + name + '\'' +
                    ", value='" + value + '\'' +
                    ", firstValue='" + firstValue + '\'' +
                    ", kvs=" + sj.toString() +
                    '}';
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public String setValue(String value) {
            String ret = null;
            this.value = value;
            int indexOf = value.indexOf(';');
            if (-1 == indexOf) {
                this.setFirstValue(value);
                return null;
            }

            this.setFirstValue(value.substring(0, indexOf));

            int indexOfEqual = value.indexOf('=', indexOf + 2);
            while (indexOfEqual != -1) {
                String key = value.substring(indexOf + 2, indexOfEqual);

                indexOf = value.indexOf(';', indexOf + 2);
                if (indexOf != -1) {
                    String vv = value.substring(indexOfEqual + 2, indexOf - 1);
                    this.kvs.put(key, vv);
                    if ("filename".equals(key)) {
                        ret = vv;
                    }
                } else {
                    String vv = value.substring(indexOfEqual + 2, value.length() - 1);
                    this.kvs.put(key, vv);
                    if ("filename".equals(key)) {
                        ret = vv;
                    }
                }

                indexOfEqual = value.indexOf('=', indexOfEqual + 1);
            }
            return "".equals(ret) ? null : ret;
        }

        public String getFirstValue() {
            return firstValue;
        }

        public void setFirstValue(String firstValue) {
            this.firstValue = firstValue;
        }

        public Map<String, String> getKvs() {
            return kvs;
        }

        public void setKvs(Map<String, String> kvs) {
            this.kvs = kvs;
        }
    }

}
