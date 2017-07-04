/**
 * @param DOMNode form
 */
var Uploader = function (form) {
  this.form = form;
};

Uploader.prototype = {
  /**
   * @param Object HTTP headers to send to the server, the key is the
   * header name, the value is the header value
   */
  headers: {
    "X-Requested-With": "XMLHttpRequest"
  },

  /**
   * @return Array of DOMNode elements
   */
  elements: function () {
    var fields = [];
    fields.readCount = 0;

    // gather INPUT elements
    var inputs = this.form.getElementsByTagName("INPUT");
    for (var l = inputs.length, i = 0; i < l; i++) {
      fields.push(inputs[i]);

      var type = inputs[i].getAttribute("type").toUpperCase();
      if (type === "FILE" && inputs[i].files.length > 0) {
        reader = new FileReader;
        reader.onload = function (evt) {
          inputs[i].binary = evt.target.result;
          // console.log(evt.target.result);
          fields.readCount -= 1;
        };
        reader.readAsBinaryString(inputs[i].files[0]);
        fields.readCount += 1;
      }
    }

    // gather SELECT elements
    var selects = this.form.getElementsByTagName("SELECT");
    for (var l = selects.length, i = 0; i < l; i++) {
      fields.push(selects[i]);
    }

    return fields;
  },

  /**
   * @return String A random string
   */
  generateBoundary: function () {
    return "AJAX-----------------------" + (new Date).getTime();
  },

  /**
   * Constructs the message as discussed in the section about form
   * data transmission over HTTP
   *
   * @param Array elements
   * @param String boundary
   * @return String
   */
  buildMessage: function (elements, boundary) {
    var CRLF = "\r\n";
    var parts = [];

    for (var i = 0, l = elements.length; i < l; i++) {
      var element = elements[i];
      var part = "";
      var type = "TEXT";

      if (element.nodeName.toUpperCase() === "INPUT") {
        type = element.getAttribute("type").toUpperCase();
      }

      if (type === "FILE" && element.files.length > 0) {
        var fieldName = element.name;
        var fileName = element.files[0].name;

        /*
         * Content-Disposition header contains name of the field
         * used to upload the file and also the name of the file as
         * it was on the user's computer.
         */
        part += 'Content-Disposition: form-data; ';
        part += 'name="' + fieldName + '"; ';
        part += 'filename="' + fileName + '"' + CRLF;

        /*
         * Content-Type header contains the mime-type of the file
         * to send. Although we could build a map of mime-types
         * that match certain file extensions, we'll take the easy
         * approach and send a general binary header:
         * application/octet-stream
         */
        part += "Content-Type: application/octet-stream";
        part += CRLF + CRLF; // marks end of the headers part

        /*
         * File contents read as binary data, obviously
         */
        part += element.binary + CRLF;
      } else {
        /*
         * In case of non-files fields, Content-Disposition
         * contains only the name of the field holding the data.
         */
        part += 'Content-Disposition: form-data; ';
        part += 'name="' + element.name + '"' + CRLF + CRLF;

        /*
         * Field value
         */
        part += element.value + CRLF;
      }

      parts.push(part);
    }

    var request = "--" + boundary + CRLF;
    request += parts.join("--" + boundary + CRLF);
    request += "--" + boundary + "--" + CRLF;

    return request;
  },

  /**
   * @return null
   */
  send: function () {
    var boundary = this.generateBoundary();
    var xhr = new XMLHttpRequest;

    xhr.open("POST", this.form.action, true);
    xhr.onreadystatechange = function () {
      if (xhr.readyState === 4) {
        alert(xhr.responseText);
      }
    };
    var contentType = "multipart/form-data; boundary=" + boundary;
    xhr.setRequestHeader("Content-Type", contentType);

    for (var header in this.headers) {
      xhr.setRequestHeader(header, this.headers[header]);
    }

    // here's our data variable that we talked about earlier
    var elements = this.elements();
    var self = this;
    (function cb() {
      if (elements.readCount) {
        setTimeout(cb, 1000);
        return;
      }

      var data = self.buildMessage(elements, boundary);

      if (!xhr.sendAsBinary) {
        xhr.sendAsBinary = function (datastr) {
          function byteValue(x) {
            return x.charCodeAt(0) & 0xff;
          }
          var ords = Array.prototype.map.call(datastr, byteValue);
          var ui8a = new Uint8Array(ords);
          this.send(ui8a.buffer);
        }
      }
      // finally send the request as binary data
      xhr.sendAsBinary(data);
    })();
  }
};