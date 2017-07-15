package hanlder;

import org.apache.http.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rest.RestHelper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class HttpFileHandler implements HttpRequestHandler {

    private static final Logger logger = LogManager.getLogger(HttpFileHandler.class);

    private final String docRoot;
    private final String urlRoot;
    private final String fallback;

    public HttpFileHandler(String docRoot, String urlRoot, String fallback) {
        super();
        this.docRoot = docRoot;
        this.urlRoot = urlRoot;
        this.fallback = fallback;
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {

        if (RestHelper.isInvalidMethod(httpRequest)) {
            throw new MethodNotSupportedException(RestHelper.getMethod(httpRequest) + " method not supported");
        }

        logger.info("Incoming entity content (bytes): " + RestHelper.getBodyAsBytes(httpRequest).length);

        String decodedUrl = RestHelper.getDecodedUrl(httpRequest);
        File file = new File(this.docRoot, decodedUrl.substring(this.urlRoot.length()));
        this.processFile(httpResponse, httpContext, file);
    }

    private void processFile(HttpResponse httpResponse, HttpContext httpContext, final File file) {

        if (!file.exists()) {

            if (!"".equals(this.fallback)) {
                File fallbackFile = new File(this.docRoot, this.fallback);
                if (fallbackFile.exists()) {
                    this.processFile(httpResponse, httpContext, fallbackFile);
                    return;
                }
            }

            httpResponse.setStatusCode(HttpStatus.SC_NOT_FOUND);
            StringEntity entity = new StringEntity(
                    "<html><body><h1>File" + file.getPath() +
                            " not found</h1></body></html>",
                    ContentType.create("text/html", "UTF-8"));
            httpResponse.setEntity(entity);
            logger.info("File " + file.getPath() + " not found");

        } else if (file.isDirectory()) {

            File indexFile = new File(file.getAbsolutePath(), "index.html");
            this.processFile(httpResponse, httpContext, indexFile);

        } else if (!file.canRead()) {

            httpResponse.setStatusCode(HttpStatus.SC_FORBIDDEN);
            StringEntity entity = new StringEntity(
                    "<html><body><h1>Access denied</h1></body></html>",
                    ContentType.create("text/html", "UTF-8"));
            httpResponse.setEntity(entity);
            logger.info("Cannot read file " + file.getPath());

        } else {
            HttpCoreContext coreContext = HttpCoreContext.adapt(httpContext);
            HttpConnection conn = coreContext.getConnection(HttpConnection.class);
            httpResponse.setStatusCode(HttpStatus.SC_OK);
            FileEntity body = new FileEntity(file, ContentType.create("text/html", (Charset) null));
            httpResponse.setEntity(body);
            logger.info(conn + ": serving file " + file.getPath());
        }

    }

}
