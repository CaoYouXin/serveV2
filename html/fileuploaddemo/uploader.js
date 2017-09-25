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

  readLog: {},

  fileLoadedHandler: function (field, key) {
    var self = this;

    function realHandler(field, key, evt) {
      field.binary = evt.target.result;
      self.readLog[key] = true;
    }

    return function (evt) {
      realHandler(field, key, evt);
    };
  },

  /**
   * @return Array of DOMNode elements
   */
  elements: function () {
    var fields = [];

    // gather INPUT elements
    var inputs = this.form.getElementsByTagName("INPUT");
    for (var l = inputs.length, i = 0; i < l; i++) {

      var type = inputs[i].getAttribute("type").toUpperCase();
      if (type === "FILE") {

        for (var len = inputs[i].files.length, j = 0; j < len; j++) {
          var field = inputs[i].files[j];

          fields.push(field);

          var reader = new FileReader();
          var key = performance.now();
          reader.onload = this.fileLoadedHandler(field, key);
          reader.readAsBinaryString(field);
          this.readLog[key] = false;
        }
      }
    }

    return fields;
  },

  /**
   * @return String A random string
   */
  generateBoundary: function () {
    return "AJAX-----------------------" + (new Date()).getTime();
  },

  toUTF8Array: function (str) {
    var utf8 = [];
    for (var i = 0; i < str.length; i++) {
      var charcode = str.charCodeAt(i);
      if (charcode < 0x80) utf8.push(charcode);
      else if (charcode < 0x800) {
        utf8.push(0xc0 | (charcode >> 6),
          0x80 | (charcode & 0x3f));
      }
      else if (charcode < 0xd800 || charcode >= 0xe000) {
        utf8.push(0xe0 | (charcode >> 12),
          0x80 | ((charcode >> 6) & 0x3f),
          0x80 | (charcode & 0x3f));
      }
      // surrogate pair
      else {
        i++;
        // UTF-16 encodes 0x10000-0x10FFFF by
        // subtracting 0x10000 and splitting the
        // 20 bits of 0x0-0xFFFFF into two halves
        charcode = 0x10000 + (((charcode & 0x3ff) << 10)
          | (str.charCodeAt(i) & 0x3ff))
        utf8.push(0xf0 | (charcode >> 18),
          0x80 | ((charcode >> 12) & 0x3f),
          0x80 | ((charcode >> 6) & 0x3f),
          0x80 | (charcode & 0x3f));
      }
    }
    return utf8;
  },

  toByteArray: function (str) {
    function byteValue(x) {
      return x.charCodeAt(0) & 0xff;
    }

    return Array.prototype.map.call(str, byteValue);
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
    var request = this.toByteArray("--" + boundary + CRLF);

    for (var i = 0, l = elements.length; i < l; i++) {
      var element = elements[i];

      var fieldName = performance.now();
      var fileName = element.name;

      /*
       * Content-Disposition header contains name of the field
       * used to upload the file and also the name of the file as
       * it was on the user's computer.
       */
      request = request.concat(this.toUTF8Array('Content-Disposition: form-data; name="' + fieldName + '"; filename="' + fileName + '"' + CRLF));

      /*
       * Content-Type header contains the mime-type of the file
       * to send. Although we could build a map of mime-types
       * that match certain file extensions, we'll take the easy
       * approach and send a general binary header:
       * application/octet-stream
       */
      request = request.concat(this.toByteArray("Content-Type: application/octet-stream" + CRLF + CRLF)); // marks end of the headers part

      /*
       * File contents read as binary data, obviously
       */
      request = request.concat(this.toByteArray(element.binary + CRLF));

      if (i !== l - 1) {
        request = request.concat(this.toByteArray("--" + boundary + CRLF));
      }
    }

    request = request.concat(this.toByteArray("--" + boundary + "--" + CRLF));

    return request;
  },

  /**
   * @return null
   */
  send: function () {
    var boundary = this.generateBoundary();
    var xhr = new XMLHttpRequest();

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
      if (!Object.keys(self.readLog).every(function (key) { return self.readLog[key]; })) {
        setTimeout(cb, 1000);
        return;
      }

      var data = self.buildMessage(elements, boundary);

      // finally send the request as binary data
      var ui8a = new Uint8Array(data);
      xhr.send(ui8a.buffer);
    })();
  }
};