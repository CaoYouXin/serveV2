export class ServiceUtils {
  static makeExist(subjects: any, key: string, cb) {
    if (!subjects[key]) {
      subjects[key] = cb();
    }
  }
}