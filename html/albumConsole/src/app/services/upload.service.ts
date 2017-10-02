import { Injectable } from '@angular/core';
import { API } from './api.const';

@Injectable()
export class UploadService {

  constructor() { }

  private toUTF8Array(str): Array<any> {
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
  }

  private toByteArray(str): Array<any> {
    function byteValue(x) {
      return x.charCodeAt(0) & 0xff;
    }

    return Array.prototype.map.call(str, byteValue);
  }

  private generateBoundary(): string {
    return "AJAX-----------------------" + (new Date()).getTime();
  }

  private buildFile(fileName, fileBinary) {
    const CRLF = "\r\n";
    return [
      ...this.toUTF8Array('Content-Disposition: form-data; name="' + performance.now() + '"; filename="' + fileName + '"' + CRLF),
      ...this.toByteArray("Content-Type: application/octet-stream" + CRLF + CRLF),
      ...this.toByteArray(fileBinary + CRLF)
    ];
  }

  private buildXHR(url, boundary, onprogress, onsuc) {
    const xhr = new XMLHttpRequest();

    xhr.open("POST", url, true);
    xhr.onreadystatechange = () => {
      if (xhr.readyState === 4) {
        onsuc();
      }
    };

    xhr.upload.onprogress = (event) => {
      if (event.lengthComputable) {
        /*event.total是需要传输的总字节数，event.load是已经传输的字节数，如果event.lengthComputable!=true,则event.total=0*/
        let percent = event.loaded / event.total;
        onprogress(percent);
      }
    };

    const contentType = "multipart/form-data; boundary=" + boundary;
    xhr.setRequestHeader("Content-Type", contentType);

    xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");

    return xhr;
  }

  uploadSoloFile(fileName, fileBinary, uploadPath, onprogress, onsuc) {
    const CRLF = "\r\n";
    const boundary = this.generateBoundary();
    const xhr = this.buildXHR(API.getAPI("upload/code")(uploadPath), boundary, onprogress, onsuc);

    xhr.send(new Uint8Array([
      ...this.toByteArray("--" + boundary + CRLF),
      ...this.buildFile(fileName, fileBinary),
      ...this.toByteArray("--" + boundary + "--" + CRLF)
    ]).buffer);
  }

}
