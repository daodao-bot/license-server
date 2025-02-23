export function emptyToNull(obj: object): object {
  return Object.keys(obj).reduce((acc, key) => {
    const value = obj[key];
    if (value === "" || value === undefined || value === null) {
      acc[key] = null;
    } else {
      acc[key] = value;
    }
    return acc;
  }, {} as object);
}
