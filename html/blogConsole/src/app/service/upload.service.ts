import {Injectable} from "@angular/core";

@Injectable()
export class UploadService {

  private headers: any = {
    "X-Requested-With": "XMLHttpRequest"
  };

  private elements(form) {
    let fields = [];
    fields['readCount'] = 0;

    // gather INPUT elements
    let inputs = form.getElementsByTagName("INPUT");
    for (let l = inputs.length, i = 0; i < l; i++) {
      fields.push(inputs[i]);

      let type = inputs[i].getAttribute("type").toUpperCase();
      if (type === "FILE" && inputs[i].files.length > 0) {
        let reader = new FileReader;
        reader.onload = function (evt) {
          inputs[i].binary = evt.target['result'];
          // console.log(evt.target.result);
          fields['readCount'] -= 1;
        };
        reader.readAsBinaryString(inputs[i].files[0]);
        fields['readCount'] += 1;
      }
    }

    // gather SELECT elements
    let selects = form.getElementsByTagName("SELECT");
    for (let l = selects.length, i = 0; i < l; i++) {
      fields.push(selects[i]);
    }

    return fields;
  }

  private generateBoundary(): string {
    return "AJAX-----------------------" + (new Date).getTime();
  }

  private buildMessage(elements, boundary) {
    let CRLF = "\r\n";
    let parts = [];

    for (let i = 0, l = elements.length; i < l; i++) {
      let element = elements[i];
      let part = "";
      let type = "TEXT";

      if (element.nodeName.toUpperCase() === "INPUT") {
        type = element.getAttribute("type").toUpperCase();
      }

      if (type === "FILE" && element.files.length > 0) {
        let fieldName = element.name;
        let fileName = element.files[0].name;

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

    let request = "--" + boundary + CRLF;
    request += parts.join("--" + boundary + CRLF);
    request += "--" + boundary + "--" + CRLF;

    return request;
  }

  send(form, url, cb) {
    let boundary = this.generateBoundary();
    let xhr = new XMLHttpRequest;

    xhr.open("POST", url, true);
    xhr.onreadystatechange = function () {
      if (xhr.readyState === 4) {
        cb(xhr.responseText);
      }
    };
    let contentType = "multipart/form-data; boundary=" + boundary;
    xhr.setRequestHeader("Content-Type", contentType);

    for (let header in this.headers) {
      xhr.setRequestHeader(header, this.headers[header]);
    }

    // here's our data letiable that we talked about earlier
    let elements = this.elements(form);
    let self = this;
    (function cb() {
      if (elements['readCount']) {
        setTimeout(cb, 1000);
        return;
      }

      let data = self.buildMessage(elements, boundary);

      if (!xhr['sendAsBinary']) {
        xhr['sendAsBinary'] = function (datastr) {
          function byteValue(x) {
            return x.charCodeAt(0) & 0xff;
          }

          let ords = Array.prototype.map.call(datastr, byteValue);
          let ui8a = new Uint8Array(ords);
          this.send(ui8a.buffer);
        }
      }
      // finally send the request as binary data
      xhr['sendAsBinary'](data);
    })();
  }

}
