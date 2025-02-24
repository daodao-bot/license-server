import security from "@/utils/license/security";

const SERVER: string = "http://localhost:8080";
const NO_AUTHORIZATION_URI: string[] = ["/api/user/login"];
const NO_SECURITY_URI: string[] = ["/api/user/login", "/api/admin/config"];

function aesKey(license: string): string {
  return license
    .split("")
    .reduce((acc, char, idx) => (idx % 2 === 0 ? acc + char : acc), "");
}

function aesIv(license: string): string {
  return license
    .split("")
    .reduce((acc, char, idx) => (idx % 2 === 1 ? acc + char : acc), "");
}

export function encrypt(license: string, plains: string): any {
  const AES_KEY = aesKey(license);
  const AES_IV = aesIv(license);
  let cipher = "";
  if (plains) {
    cipher = security.aesEncrypt(plains, AES_KEY, AES_IV);
  }
  return cipher;
}

export function decrypt(license: string, cipher: string): any {
  const AES_KEY = aesKey(license);
  const AES_IV = aesIv(license);
  let plains = "";
  if (cipher) {
    plains = security.aesDecrypt(cipher, AES_KEY, AES_IV);
  }
  return plains;
}

const license = {
  SERVER,
  NO_AUTHORIZATION_URI,
  NO_SECURITY_URI,
  encrypt,
  decrypt
};

export default license;
